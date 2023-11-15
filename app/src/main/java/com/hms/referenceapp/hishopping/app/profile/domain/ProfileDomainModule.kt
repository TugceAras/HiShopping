package com.hms.referenceapp.hishopping.app.profile.domain

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.User
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ProfileDomainModule {

    @get:Binds
    abstract val GetUserUseCase.getUserUseCase:
            BaseResultFlowAsyncUseCase<User,String>
}