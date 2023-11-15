package com.hms.referenceapp.hishopping.app.myorders.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllOrdersUseCase @Inject constructor(private val myOrdersRepository: MyOrdersRepository):
        BaseResultFlowAsyncUseCase<@JvmSuppressWildcards List<OrderEntity>,String>{
    override suspend fun execute(params: String?): Flow<ResultData<List<OrderEntity>>> {
        return myOrdersRepository.getAllOrders(params!!)
    }
}