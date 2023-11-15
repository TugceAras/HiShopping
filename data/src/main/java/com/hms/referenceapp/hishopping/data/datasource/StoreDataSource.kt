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
package com.hms.referenceapp.hishopping.data.datasource

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.model.Store
import kotlinx.coroutines.flow.Flow

interface StoreDataSource {
    suspend fun getAll(): Flow<ResultData<List<Store>>>
    suspend fun loadAllByIds(StoreIds: IntArray): Flow<ResultData<List<Store>>>
    suspend fun loadById(StoreId: Int): Flow<ResultData<Store>>
    suspend fun findByName(nameQuery: String): Flow<ResultData<Store>>
    suspend fun insertAll(Stores: List<Store>): Flow<ResultData<Unit>>
    suspend fun delete(Store: Store): Flow<ResultData<Unit>>
}