package com.hms.referenceapp.hishopping.app.favorites.presentation

import com.hms.referenceapp.hishopping.data.model.Favorites

data class FavoritesViewState(
    val favoriteProductList: List<Favorites>? = null,
    val loading: Boolean = false,
    val error: Boolean = false,
    val errorMessage: String? = null
)
