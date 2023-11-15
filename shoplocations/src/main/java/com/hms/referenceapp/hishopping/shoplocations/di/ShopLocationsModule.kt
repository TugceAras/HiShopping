// See the License for the specific language governing permissions and
// limitations under the License.
//
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.referenceapp.hishopping.shoplocations.di

import com.hms.referenceapp.hishopping.data.repository.ProductRepository
import com.hms.referenceapp.hishopping.data.repository.StoreRepository
import com.hms.referenceapp.hishopping.shoplocations.data.ProductDataSource
import com.hms.referenceapp.hishopping.shoplocations.data.ShopLocationDataSource
import com.hms.referenceapp.hishopping.shoplocations.data.ShopLocationRepositoryImpl
import com.hms.referenceapp.hishopping.shoplocations.domain.repository.ShopLocationRepository
import com.hms.referenceapp.hishopping.shoplocations.domain.usecase.GetNearestProductShopsUseCase
import com.hms.referenceapp.hishopping.shoplocations.domain.usecase.GetNearestShopsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object ShopLocationsModule {

    @Provides
    fun provideGetNearestShopsUseCase(provide: ShopLocationRepository) = GetNearestShopsUseCase(provide)

    @Provides
    fun provideGetNearestProductShopsUseCase(provide: ShopLocationRepository) = GetNearestProductShopsUseCase(provide)

    @Provides
    fun provideShopLocationRepository(provide: ShopLocationRepositoryImpl): ShopLocationRepository = provide

    @Provides
    fun provideProductDataSource(provide: ProductRepository) = ProductDataSource(provide)

    @Provides
    fun provideShopLocationDataSource(provide: StoreRepository) = ShopLocationDataSource(provide)

}