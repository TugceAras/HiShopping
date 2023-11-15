package com.hms.referenceapp.hishopping.shoplocations.domain.usecase

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.SameProduct
import com.hms.referenceapp.hishopping.data.model.StoreEntity
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.GetStoresByProductIdRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ShopLocationDomainModule {

    @get:Binds
    abstract val GetStoresUseCase.getStoresUseCase:
            BaseRetrieveResultFlowAsyncUseCase<List<StoreEntity>>

    @get:Binds
    abstract val GetSameProductsUseCase.getSameProductsUseCase:
            BaseResultFlowAsyncUseCase<List<SameProduct>,GetStoresByProductIdRequest>
}