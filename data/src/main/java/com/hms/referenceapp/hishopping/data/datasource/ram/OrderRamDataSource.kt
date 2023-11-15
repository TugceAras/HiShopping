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
import com.hms.referenceapp.hishopping.data.datasource.OrderDataSource
import com.hms.referenceapp.hishopping.data.model.Order
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRamDataSource @Inject constructor(private val ramDb: RamDb) : OrderDataSource {

    override suspend fun getAll(): Flow<ResultData<List<Order>>> {
        return resultFlow {
            ResultData.Success(ramDb.orders)
        }
    }

    override suspend fun upsertOrder(order: Order): Flow<ResultData<Order>> {
        return resultFlow {
            ramDb.orders.find { order.id == it.id }?.let {
                val index = ramDb.orders.indexOf(it)
                ramDb.orders[index] = order
            } ?: let {
                val lastId = ramDb.orders.takeIf { it.isNotEmpty() }?.last()?.id ?: 1
                ramDb.orders.add(order.apply { id = lastId + 1 })
            }
            ResultData.Success(order)
        }
    }

    override suspend fun makeOrderDelivered(orderId: Int): Flow<ResultData<Order>> {
        return resultFlow {
            ramDb.orders.find { orderId == it.id }?.let {
                it.orderStatus = Order.OrderStatus.DELIVERED
                ResultData.Success(it)
            } ?: ResultData.Failed()
        }
    }

}