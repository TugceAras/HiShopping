package com.hms.referenceapp.hishopping.address.domain

import com.hms.referenceapp.hishopping.address.domain.usecase.AddAddressUseCase
import com.hms.referenceapp.hishopping.address.domain.usecase.GetAllAddressesUseCase
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.UserAddress
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AddressDomainModule {

    @get:Binds
    abstract val GetAllAddressesUseCase.getAddressesUseCase:
            BaseResultFlowAsyncUseCase<List<UserAddress>,String>

    @get:Binds
    abstract val AddAddressUseCase.addAddressUseCase:
            BaseResultFlowAsyncUseCase<Unit,UserAddress>
}