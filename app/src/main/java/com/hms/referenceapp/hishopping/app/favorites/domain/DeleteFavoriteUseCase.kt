package com.hms.referenceapp.hishopping.app.favorites.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.Favorites
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    BaseResultFlowAsyncUseCase<Unit, Favorites>{
    override suspend fun execute(params: Favorites?): Flow<ResultData<Unit>> {
        return favoritesRepository.deleteFromProducts(params!!)
    }
}