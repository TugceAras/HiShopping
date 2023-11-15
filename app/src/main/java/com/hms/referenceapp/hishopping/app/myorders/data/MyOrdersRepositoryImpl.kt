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
package com.hms.referenceapp.hishopping.app.myorders.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.core.handleSuccessSuspend
import com.hms.referenceapp.hishopping.app.myorders.domain.MyOrdersRepository
import com.hms.referenceapp.hishopping.app.myorders.domain.model.OrderViewItem
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.Order
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import com.hms.referenceapp.hishopping.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyOrdersRepositoryImpl @Inject constructor(
    private val productRepository: ProductRepository,
    private val myOrdersDataSource: BaseRetrieveResultFlowAsyncDataSource<@JvmSuppressWildcards List<Order>>,
    private val upsertOrderDataSource: BaseResultFlowAsyncDataSource<Unit, OrderEntity>,
    private val ordersDataSource: BaseResultFlowAsyncDataSource<@JvmSuppressWildcards List<OrderEntity>,String>
) : MyOrdersRepository {
    override suspend fun getMyOrders(): Flow<ResultData<List<OrderViewItem>>> {
        return myOrdersDataSource.getResult().map {
            val finalList = mutableListOf<OrderViewItem>()
            it.handleSuccessSuspend {
                it.data?.forEach { order ->
                    productRepository.loadById(order.productId).collect {
                        it.handleSuccessSuspend {
                            finalList.add(OrderViewItem(order, it.data!!))
                        }
                    }
                }
                finalList
            }
        }
    }

    override suspend fun getAllOrders(userId:String): Flow<ResultData<List<OrderEntity>>> {
        return ordersDataSource.getResult(userId)
    }

    override suspend fun addOrder(orderEntity: OrderEntity): Flow<ResultData<Unit>> {
        return upsertOrderDataSource.getResult(orderEntity)
    }
}