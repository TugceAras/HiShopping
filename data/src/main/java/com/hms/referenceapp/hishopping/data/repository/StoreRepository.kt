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
package com.hms.referenceapp.hishopping.data.repository

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.clouddb.CloudDbDataSource
import com.hms.referenceapp.hishopping.data.datasource.StoreDataSource
import com.hms.referenceapp.hishopping.data.mapper.StoreMapper
import com.hms.referenceapp.hishopping.data.mapper.UserAddressMapper
import com.hms.referenceapp.hishopping.data.model.Store
import com.hms.referenceapp.hishopping.data.model.StoreEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface StoreRepository {
    suspend fun getAll(): Flow<ResultData<List<Store>>>
    suspend fun getStores(): Flow<ResultData<List<StoreEntity>>>
    suspend fun loadAllByIds(StoreIds: IntArray): Flow<ResultData<List<Store>>>
    suspend fun loadById(StoreId: Int): Flow<ResultData<Store>>
    suspend fun findByName(nameQuery: String): Flow<ResultData<Store>>
    suspend fun insertAll(Stores: List<Store>): Flow<ResultData<Unit>>
    suspend fun delete(Store: Store): Flow<ResultData<Unit>>
}

class StoreRepositoryImpl @Inject constructor(
    private val cloudDbDataSource: CloudDbDataSource,
    private val dataSource: StoreDataSource
) : StoreRepository {

    override suspend fun getAll(): Flow<ResultData<List<Store>>> {
        return dataSource.getAll()
    }

    override suspend fun getStores(): Flow<ResultData<List<StoreEntity>>> = flow {
        emit(ResultData.Loading())
        try {
            val result = cloudDbDataSource.getStores()
            emit(
                ResultData.Success(
                    result.map { StoreMapper.toEntity(it) }
                )
            )
        }catch (e:Exception){
            emit(ResultData.Failed(e.message))
        }
    }

    override suspend fun loadAllByIds(StoreIds: IntArray): Flow<ResultData<List<Store>>> {
        return dataSource.loadAllByIds(StoreIds)
    }

    override suspend fun loadById(StoreId: Int): Flow<ResultData<Store>> {
        return dataSource.loadById(StoreId)
    }

    override suspend fun findByName(nameQuery: String): Flow<ResultData<Store>> {
        return dataSource.findByName(nameQuery)
    }


    override suspend fun insertAll(Stores: List<Store>): Flow<ResultData<Unit>> {
        return dataSource.insertAll(Stores)
    }

    override suspend fun delete(Store: Store): Flow<ResultData<Unit>> {
        return dataSource.delete(Store)
    }


}