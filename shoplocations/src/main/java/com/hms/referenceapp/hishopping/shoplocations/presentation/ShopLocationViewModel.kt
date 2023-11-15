// See the License for the specific language governing permissions and
// limitations under the License.
//
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.referenceapp.hishopping.shoplocations.presentation

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.SameProduct
import com.hms.referenceapp.hishopping.data.model.StoreEntity
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.GetStoresByProductIdRequest
import com.hms.referenceapp.hishopping.shoplocations.domain.usecase.GetSameProductsUseCase
import com.hms.referenceapp.hishopping.shoplocations.domain.usecase.GetStoresUseCase
import com.hms.referenceapp.hishopping.shoplocations.presentation.model.StoreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopLocationViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase,
    private val getSameProductsUseCase: GetSameProductsUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<ShopLocationViewState>()
    val uiState: LiveData<ShopLocationViewState> get() = _uiState

    fun getStores(userLocation: Location,locationType: LocationType) = viewModelScope.launch {
        _uiState.value = ShopLocationViewState(loading = true)
        getStoresUseCase.execute().collect{ resultData ->
            when(resultData){
                is ResultData.Success -> {
                    resultData.data?.let { storeEntityList ->
                        val storeItemList = getAllStoreItems(userLocation, storeEntityList)
                        when(locationType){
                            LocationType.NEAREST_SHOPS -> _uiState.value = ShopLocationViewState(storeItemList = storeItemList)
                            LocationType.PRODUCT_SHOPS -> getSameProducts(userLocation,storeEntityList)
                        }
                    }
                }
                is ResultData.Failed -> {
                    _uiState.value = ShopLocationViewState(error = resultData.error)
                }
            }
        }
    }

    private fun getSameProducts(
        userLocation: Location,
        storeEntityList: List<StoreEntity>
    ) = viewModelScope.launch {
        getSameProductsUseCase.execute(GetStoresByProductIdRequest(
            "12",
            "WD 4TB Gaming Drive Works with Playstation 4 Portable External Hard Drive"
        )).collect { result ->
            when (result){
                is ResultData.Success -> {
                    result.data?.let { sameProductList ->
                        _uiState.value = ShopLocationViewState(
                            storeItemList = getStoreItemsFilteredById(
                                userLocation,
                                storeEntityList,
                                sameProductList
                            )
                        )
                    }
                }
                is ResultData.Failed -> { _uiState.value = ShopLocationViewState(error = result.error) }
            }
        }
    }

    private fun getAllStoreItems(userLocation: Location,storeEntityList: List<StoreEntity>):List<StoreItem>{
        return storeEntityList.map { storeEntity ->
            val storeLocation = Location("storeLocation")
            storeLocation.latitude = storeEntity.lat
            storeLocation.longitude = storeEntity.lon
            val distance = userLocation.distanceTo(storeLocation)
            mapStoreEntityToStoreItem(storeEntity,distance)
        }.sortedBy { it.distance }
    }

    private fun getStoreItemsFilteredById(
        userLocation: Location,
        storeEntityList: List<StoreEntity>,
        sameProducts: List<SameProduct>
    ):List<StoreItem>{
        return storeEntityList.map { storeEntity ->
            val storeLocation = Location("storeLocation")
            storeLocation.latitude = storeEntity.lat
            storeLocation.longitude = storeEntity.lon
            val distance = userLocation.distanceTo(storeLocation)
            mapStoreEntityToStoreItem(storeEntity,distance)
        }.filter { storeItem ->
            sameProducts.any { it.storeId == storeItem.id }
        }.sortedBy { it.distance }
    }

    private fun mapStoreEntityToStoreItem(storeEntity: StoreEntity,distance:Float): StoreItem{
        return StoreItem(
            storeEntity.id,
            storeEntity.name,
            storeEntity.lat,
            storeEntity.lon,
            storeEntity.addressLine,
            storeEntity.photoUrl,
            distance,
            if (distance < 1000) "$distance m"
            else "${String.format("%.1f", distance/1000)} km"
        )
    }
}