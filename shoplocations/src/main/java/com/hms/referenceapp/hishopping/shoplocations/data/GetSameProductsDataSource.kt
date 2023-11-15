package com.hms.referenceapp.hishopping.shoplocations.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.SameProduct
import com.hms.referenceapp.hishopping.data.repository.ProductRepository
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.GetStoresByProductIdRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSameProductsDataSource @Inject constructor(
    private val productsRepository: ProductRepository
) : BaseResultFlowAsyncDataSource<List<@JvmSuppressWildcards SameProduct>,GetStoresByProductIdRequest> {
    override suspend fun getResult(params: GetStoresByProductIdRequest?): Flow<ResultData<List<SameProduct>>> {
        return productsRepository.getSameProducts(params!!.brandId,params!!.title)
    }
}