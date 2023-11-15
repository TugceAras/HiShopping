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
package com.hms.referenceapp.hishopping.data.datasource.ram

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.base.extensions.resultFlow
import com.hms.referenceapp.hishopping.data.datasource.StoreDataSource
import com.hms.referenceapp.hishopping.data.model.Store
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreRamDataSource @Inject constructor(private val ramDb: RamDb) : StoreDataSource {

    override suspend fun getAll(): Flow<ResultData<List<Store>>> {
        return resultFlow {
            ResultData.Success(ramDb.stores)
        }
    }

    override suspend fun loadAllByIds(StoreIds: IntArray): Flow<ResultData<List<Store>>> {
        return resultFlow {
            ResultData.Success(ramDb.stores.filter { StoreIds.contains(it.id) })
        }
    }

    override suspend fun findByName(query: String): Flow<ResultData<Store>> {
        return resultFlow {
            ResultData.Success(ramDb.stores.firstOrNull {
                it.visibleName.contains(query)
            })
        }
    }

    override suspend fun insertAll(stores: List<Store>): Flow<ResultData<Unit>> {
        return resultFlow {
            ramDb.stores.addAll(stores)
            ResultData.Success()
        }
    }

    override suspend fun delete(Store: Store): Flow<ResultData<Unit>> {
        return resultFlow {
            ramDb.stores.firstOrNull { it.id == Store.id }?.let {
                ramDb.stores.remove(it)
            }
            ResultData.Success()
        }
    }

    override suspend fun loadById(StoreId: Int): Flow<ResultData<Store>> {
        return resultFlow {
            ResultData.Success(ramDb.stores.firstOrNull {
                it.id == StoreId
            })
        }
    }

}