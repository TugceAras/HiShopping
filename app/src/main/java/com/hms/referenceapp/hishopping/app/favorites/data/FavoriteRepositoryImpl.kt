package com.hms.referenceapp.hishopping.app.favorites.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.app.favorites.domain.FavoritesRepository
import com.hms.referenceapp.hishopping.data.model.Favorites
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteProductsDataSource: FavoriteProductsDataSource,
    private val deleteFavoriteDataSource: DeleteFavoriteDataSource
) : FavoritesRepository {
    override suspend fun getFavoriteProducts(userId: String): Flow<ResultData<List<Favorites>>> {
        return favoriteProductsDataSource.getResult(userId)
    }

    override suspend fun deleteFromProducts(favorites: Favorites): Flow<ResultData<Unit>> {
        return deleteFavoriteDataSource.getResult(favorites)
    }
}