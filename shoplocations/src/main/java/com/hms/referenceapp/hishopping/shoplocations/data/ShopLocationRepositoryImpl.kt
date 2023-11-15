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
package com.hms.referenceapp.hishopping.shoplocations.data

import com.hms.lib.commonmobileservices.core.*
import com.hms.lib.commonmobileservices.core.handleSuccess
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.data.model.SameProduct
import com.hms.referenceapp.hishopping.data.model.StoreEntity
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.GetStoresByProductIdRequest
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.ShopLocation
import com.hms.referenceapp.hishopping.shoplocations.domain.repository.ShopLocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ShopLocationRepositoryImpl @Inject constructor(
    private val shopLocationDataSource: ShopLocationDataSource,
    private val productDataSource: ProductDataSource,
    private val storesDataSource: StoresDataSource,
    private val getSameProductsDataSource: GetSameProductsDataSource
) : ShopLocationRepository {

    override suspend fun getStores(): Flow<ResultData<List<StoreEntity>>> {
        return storesDataSource.getResult()
    }

    override suspend fun getSameProducts(
        getStoresByProductIdRequest: GetStoresByProductIdRequest
    ): Flow<ResultData<List<SameProduct>>> {
        return getSameProductsDataSource.getResult(getStoresByProductIdRequest)
    }

    override suspend fun getLocations(userLocation: Pair<Double, Double>):
            ResultData<List<ShopLocation>> {
        return shopLocationDataSource.getShopLocations(userLocation)
    }

    override suspend fun getProductShopLocations(params: Triple<Double, Double, Int>?):
            ResultData<List<ShopLocation>> {

        var product: Product? = null
        productDataSource.getProductById(params!!.third).handleSuccess {
            product = it.data
        }

        return product?.let {
            shopLocationDataSource.getShopLocationsByIds(
                params.first to params.second,
                it.availableStores
            )
        } ?: ResultData.Success(listOf())
    }
}