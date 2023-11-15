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

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.core.handleSuccess
import com.hms.referenceapp.hishopping.data.repository.StoreRepository
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.ShopLocation
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.toShopLocation
import kotlinx.coroutines.flow.reduce
import javax.inject.Inject

class ShopLocationDataSource @Inject constructor(private val storeRepository: StoreRepository) {

    suspend fun getShopLocations(userLocation: Pair<Double, Double>):
            ResultData<List<ShopLocation>> {
        return storeRepository.getAll().reduce { _, value ->
            value
        }.handleSuccess {
            it.data?.map {
                it.toShopLocation(
                    it.location.lat + userLocation.first to
                            it.location.lng + userLocation.second
                )
            }
        }
    }

    suspend fun getShopLocationsByIds(userLocation: Pair<Double, Double>, ids: List<Int>):
            ResultData<List<ShopLocation>> {
        return storeRepository.loadAllByIds(ids.toIntArray()).reduce { _, value ->
            value
        }.handleSuccess {
            it.data?.let {
                it.map {
                    it.toShopLocation(
                        it.location.lat + userLocation.first to
                                it.location.lng + userLocation.second
                    )
                }
            } ?: listOf()
        }
    }
}