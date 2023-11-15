package com.hms.referenceapp.hishopping.app.favorites.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.Favorites
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(private val favoritesRepository: FavoritesRepository) :
        BaseResultFlowAsyncUseCase<@JvmSuppressWildcards List<Favorites>,String>{
    override suspend fun execute(params: String?): Flow<ResultData<List<Favorites>>> {
        return favoritesRepository.getFavoriteProducts(params!!)
    }
}