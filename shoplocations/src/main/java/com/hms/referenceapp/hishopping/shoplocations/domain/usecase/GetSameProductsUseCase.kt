package com.hms.referenceapp.hishopping.shoplocations.domain.usecase

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.SameProduct
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.GetStoresByProductIdRequest
import com.hms.referenceapp.hishopping.shoplocations.domain.repository.ShopLocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSameProductsUseCase @Inject constructor(
    private val shopLocationRepository: ShopLocationRepository
) : BaseResultFlowAsyncUseCase<List<@JvmSuppressWildcards SameProduct>,GetStoresByProductIdRequest> {
    override suspend fun execute(params: GetStoresByProductIdRequest?): Flow<ResultData<List<SameProduct>>> {
        return shopLocationRepository.getSameProducts(params!!)
    }
}