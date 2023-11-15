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
import com.hms.referenceapp.hishopping.data.datasource.OrderDataSource
import com.hms.referenceapp.hishopping.data.mapper.OrderMapper
import com.hms.referenceapp.hishopping.data.model.Order
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface OrderRepository {
    suspend fun getAll(): Flow<ResultData<List<Order>>>
    suspend fun getAllOrders(userId: String): Flow<ResultData<List<OrderEntity>>>
    suspend fun upsertOrder(orderEntity: OrderEntity): Flow<ResultData<Unit>>
    suspend fun makeOrderDelivered(orderId: Int): Flow<ResultData<Order>>
}

class OrderRepositoryImpl @Inject constructor(
    private val cloudDbDataSource: CloudDbDataSource,
    private val orderDataSource: OrderDataSource
) : OrderRepository {
    override suspend fun getAll(): Flow<ResultData<List<Order>>> {
        return orderDataSource.getAll()
    }

    override suspend fun getAllOrders(userId:String): Flow<ResultData<List<OrderEntity>>> = flow {
        emit(ResultData.Loading())
        try {
            emit(
                ResultData.Success(
                    cloudDbDataSource.getOrders(userId).map {
                        OrderMapper.toEntity(it)
                    }
                )
            )
        }catch (e:Exception){
            emit(
                ResultData.Failed(e.message?:"")
            )
        }
    }

    override suspend fun upsertOrder(orderEntity: OrderEntity): Flow<ResultData<Unit>> = flow {
        emit(ResultData.Loading())
        try {
            emit(
                ResultData.Success(
                    cloudDbDataSource.upsertOrder(
                        OrderMapper.fromEntity(orderEntity)
                    )
                )
            )
        }catch (e:Exception){
            emit(
                ResultData.Failed(e.message?:"")
            )
        }
    }

    override suspend fun makeOrderDelivered(orderId: Int): Flow<ResultData<Order>> {
        return orderDataSource.makeOrderDelivered(orderId)
    }
}