package com.hms.referenceapp.hishopping.app.favorites.data

import com.hms.referenceapp.hishopping.app.favorites.domain.FavoritesRepository
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.Favorites
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class FavoritesDataModule {

    @get:Binds
    abstract val FavoriteRepositoryImpl.repo: FavoritesRepository

    @get:Binds
    abstract val FavoriteProductsDataSource.dataSource: BaseResultFlowAsyncDataSource<@JvmSuppressWildcards List<Favorites>,String>
}