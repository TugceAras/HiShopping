package com.hms.referenceapp.hishopping.couriertrack.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import com.hms.referenceapp.hishopping.data.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateOrderDataSource @Inject constructor(private val orderRepository: OrderRepository) :
    BaseResultFlowAsyncDataSource<Unit, OrderEntity> {
    override suspend fun getResult(params: OrderEntity?): Flow<ResultData<Unit>> {
        return orderRepository.upsertOrder(params!!)
    }
}