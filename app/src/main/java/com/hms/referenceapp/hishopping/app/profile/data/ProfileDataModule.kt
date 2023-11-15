package com.hms.referenceapp.hishopping.app.profile.data

import com.hms.referenceapp.hishopping.app.profile.domain.ProfileRepository
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.User
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ProfileDataModule {

    @get:Binds
    abstract val GetUserDataSource.dataSource:
            BaseResultFlowAsyncDataSource<User,String>

    @get:Binds
    abstract val ProfileRepositoryImpl.repo:
            ProfileRepository
}