package com.hms.referenceapp.hishopping.app.myorders.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import com.hms.referenceapp.hishopping.data.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrdersDataSource @Inject constructor(private val orderRepository: OrderRepository) :
        BaseResultFlowAsyncDataSource<List<@JvmSuppressWildcards OrderEntity>,String>{
    override suspend fun getResult(params: String?): Flow<ResultData<List<OrderEntity>>> {
        return orderRepository.getAllOrders(params!!)
    }
}