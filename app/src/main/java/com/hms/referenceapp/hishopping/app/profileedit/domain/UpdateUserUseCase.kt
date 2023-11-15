package com.hms.referenceapp.hishopping.app.profileedit.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class UpdateUserUseCase @Inject constructor(private val userRepository: UserRepository) :
        BaseResultFlowAsyncUseCase<Unit,User>{
    override suspend fun execute(params: User?): Flow<ResultData<Unit>> {
        return userRepository.upsertUser(params!!)
    }
}