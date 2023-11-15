package com.hms.referenceapp.hishopping.app.profileedit.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class UpdateUserDataSource @Inject constructor(private val userRepository: UserRepository) :
        BaseResultFlowAsyncDataSource<Unit,User>{
    override suspend fun getResult(params: User?): Flow<ResultData<Unit>> {
        return userRepository.upsertUser(params!!)
    }
}