package com.hms.referenceapp.hishopping.app.categories.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.CategoryEntity
import com.hms.referenceapp.hishopping.data.model.ProductEntity
import com.hms.referenceapp.hishopping.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Mustafa Kemal Özdemir on 3/24/2022.
 */
class GetCategoriesWithProductsUseCase @Inject constructor(private val productRepository: ProductRepository):
    BaseResultFlowAsyncUseCase<Map<CategoryEntity, List<ProductEntity>>, Unit> {

    override suspend fun execute(params: Unit?): Flow<ResultData<Map<CategoryEntity, List<ProductEntity>>>> {
        return  productRepository.getCategoriesWithProducts()
    }
}