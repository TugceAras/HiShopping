package com.hms.referenceapp.hishopping.shoplocations.data

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.SameProduct
import com.hms.referenceapp.hishopping.data.model.StoreEntity
import com.hms.referenceapp.hishopping.shoplocations.domain.entity.GetStoresByProductIdRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ShopLocationDataModule {

    @get:Binds
    abstract val StoresDataSource.storeDataSource:
            BaseRetrieveResultFlowAsyncDataSource<List<StoreEntity>>

    @get:Binds
    abstract val GetSameProductsDataSource.sameProductsDataSource:
            BaseResultFlowAsyncDataSource<List<SameProduct>,GetStoresByProductIdRequest>
}