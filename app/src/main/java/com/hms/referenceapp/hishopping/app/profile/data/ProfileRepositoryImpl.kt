package com.hms.referenceapp.hishopping.app.profile.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.app.profile.domain.ProfileRepository
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val getUserDataSource:
            BaseResultFlowAsyncDataSource<User,String>
) : ProfileRepository {
    override suspend fun getUser(userId: String): Flow<ResultData<User>> {
        return getUserDataSource.getResult(userId)
    }
}