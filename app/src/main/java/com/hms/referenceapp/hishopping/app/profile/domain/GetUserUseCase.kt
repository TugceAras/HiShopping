package com.hms.referenceapp.hishopping.app.profile.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) :
        BaseResultFlowAsyncUseCase<User,String>{
    override suspend fun execute(params: String?): Flow<ResultData<User>> {
        return userRepository.getUser(params!!)
    }
}