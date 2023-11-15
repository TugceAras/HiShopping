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
package com.hms.referenceapp.hishopping.shoplocations.domain.repository

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.model.SameProduct
import com.hms.referenceapp.hishopping.data.model.StoreEntity
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.GetStoresByProductIdRequest
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.ShopLocation
import kotlinx.coroutines.flow.Flow

interface ShopLocationRepository {

    suspend fun getStores(): Flow<ResultData<List<StoreEntity>>>

    suspend fun getSameProducts(
        getStoresByProductIdRequest: GetStoresByProductIdRequest
    ): Flow<ResultData<List<SameProduct>>>

    suspend fun getLocations(userLocation: Pair<Double, Double>): ResultData<List<ShopLocation>>

    suspend fun getProductShopLocations(params: Triple<Double, Double, Int>?): ResultData<List<ShopLocation>>
}