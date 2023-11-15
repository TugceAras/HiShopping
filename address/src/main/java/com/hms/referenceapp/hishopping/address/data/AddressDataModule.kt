package com.hms.referenceapp.hishopping.address.data

import com.hms.referenceapp.hishopping.address.domain.UserAddressRepository
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.UserAddress
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AddressDataModule {

    @get:Binds
    abstract val AddAddressDataSource.addAddressDataSource:
            BaseResultFlowAsyncDataSource<Unit,UserAddress>

    @get:Binds
    abstract val GetAllAddressesDataSource.getAddressesDataSource:
            BaseResultFlowAsyncDataSource<List<UserAddress>,String>

    @get:Binds
    abstract val UserAddressesRepositoryImpl.repo:
            UserAddressRepository
}