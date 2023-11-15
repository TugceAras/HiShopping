package com.hms.referenceapp.hishopping.app.favorites.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.model.Favorites
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun getFavoriteProducts(userId: String) :
            Flow<ResultData<List<Favorites>>>

    suspend fun deleteFromProducts(favorites: Favorites) :
            Flow<ResultData<Unit>>
}