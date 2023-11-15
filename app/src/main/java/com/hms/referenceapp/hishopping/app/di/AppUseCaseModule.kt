package com.hms.referenceapp.hishopping.app.di

import com.hms.referenceapp.hishopping.app.categories.domain.GetCategoriesWithProductsUseCase
import com.hms.referenceapp.hishopping.data.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

/**
 * Created by Mustafa Kemal Özdemir on 3/24/2022.
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
object AppUseCaseModule {

    @Provides
    fun provideGetCategoriesWithProductsUseCase(provide: ProductRepository) = GetCategoriesWithProductsUseCase(provide)
}