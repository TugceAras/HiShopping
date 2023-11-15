package com.hms.referenceapp.hishopping.signin.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsUserExistDataSource @Inject constructor(private val userRepository: UserRepository) :
        BaseResultFlowAsyncDataSource<Boolean,String>{
    override suspend fun getResult(params: String?): Flow<ResultData<Boolean>> {
        return userRepository.isUserExist(params!!)
    }
}