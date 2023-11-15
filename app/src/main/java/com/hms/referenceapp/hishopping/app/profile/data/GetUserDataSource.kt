package com.hms.referenceapp.hishopping.app.profile.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataSource @Inject constructor(private val userRepository: UserRepository) :
        BaseResultFlowAsyncDataSource<User,String>{
    override suspend fun getResult(params: String?): Flow<ResultData<User>> {
        return userRepository.getUser(params!!)
    }
}