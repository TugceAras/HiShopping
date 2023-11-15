package com.hms.referenceapp.hishopping.app.profileedit.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.app.profileedit.domain.ProfileEditRepository
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileEditRepositoryImpl @Inject constructor(
    private val updateUserDataSource:
            BaseResultFlowAsyncDataSource<Unit,User>
) : ProfileEditRepository{
    override suspend fun updateUser(user: User): Flow<ResultData<Unit>> {
        return updateUserDataSource.getResult(user)
    }
}