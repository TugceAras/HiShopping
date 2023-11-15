package com.hms.referenceapp.hishopping.signin.data

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.signin.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AuthDataModule {

    @get:Binds
    abstract val UpsertUserDataSource.upserDataSource:
            BaseResultFlowAsyncDataSource<Unit,User>

    @get:Binds
    abstract val IsUserExistDataSource.userExistDataSource:
            BaseResultFlowAsyncDataSource<Boolean,String>

    @get:Binds
    abstract val AuthRepositoryImpl.repo:
            AuthRepository
}