package com.hms.referenceapp.hishopping.app.favorites.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.Favorites
import com.hms.referenceapp.hishopping.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteProductsDataSource @Inject constructor(private val productRepository: ProductRepository) :
        BaseResultFlowAsyncDataSource<List<@JvmSuppressWildcards Favorites>,String>{
    override suspend fun getResult(params: String?): Flow<ResultData<List<Favorites>>> {
        return productRepository.getFavoriteProducts(params!!)
    }
}