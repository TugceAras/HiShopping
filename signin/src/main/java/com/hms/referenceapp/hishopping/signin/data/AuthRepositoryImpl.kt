package com.hms.referenceapp.hishopping.signin.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.signin.domain.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val upsertUserDataSource: BaseResultFlowAsyncDataSource<Unit,User>,
    private val isUserExistDataSource: BaseResultFlowAsyncDataSource<Boolean,String>
) : AuthRepository {
    override suspend fun upsertUser(user: User): Flow<ResultData<Unit>> {
        return upsertUserDataSource.getResult(user)
    }

    override suspend fun isUserExist(userId: String): Flow<ResultData<Boolean>> {
        return isUserExistDataSource.getResult(userId)
    }
}