package com.hms.referenceapp.hishopping.app.favorites.presentation

import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.app.favorites.domain.DeleteFavoriteUseCase
import com.hms.referenceapp.hishopping.app.favorites.domain.GetFavoriteProductsUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.core.SingleLiveData
import com.hms.referenceapp.hishopping.data.model.Favorites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : BaseViewModel() {

    val viewState = SingleLiveData<FavoritesViewState>()

    fun getFavoriteProducts(userId: String) = viewModelScope.launch {
        getFavoriteProductsUseCase.execute(userId).collect{
            when(it){
                is ResultData.Success -> { viewState.value = FavoritesViewState(favoriteProductList = it.data) }
                is ResultData.Loading -> { viewState.value = FavoritesViewState(loading = true) }
                is ResultData.Failed -> { viewState.value = FavoritesViewState(error = true, errorMessage = it.error) }
            }
        }
    }

    fun deleteFromFavorites(favorites: Favorites) = viewModelScope.launch {
        deleteFavoriteUseCase.execute(favorites).collect{
            when(it){
                is ResultData.Success -> {
                    viewState.value = viewState.value?.copy(loading = false, error = false)
                    getFavoriteProducts(favorites.userId)
                }
                is ResultData.Loading -> { viewState.value = viewState.value?.copy(loading = true, error = false) }
                is ResultData.Failed -> { viewState.value = viewState.value?.copy(
                    loading = false,
                    error = true,
                    errorMessage = it.error
                )}
            }
        }
    }
}