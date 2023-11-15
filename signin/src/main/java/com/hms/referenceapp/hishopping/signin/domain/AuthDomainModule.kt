package com.hms.referenceapp.hishopping.signin.domain

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.User
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AuthDomainModule {

    @get:Binds
    abstract val UpsertUserUseCase.upsertUseCase:
            BaseResultFlowAsyncUseCase<Unit,User>

    @get:Binds
    abstract val IsUserExistUseCase.userExistUseCase:
            BaseResultFlowAsyncUseCase<Boolean,String>
}