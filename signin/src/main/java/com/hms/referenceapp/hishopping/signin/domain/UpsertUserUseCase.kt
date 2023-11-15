package com.hms.referenceapp.hishopping.signin.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpsertUserUseCase @Inject constructor(private val authRepository: AuthRepository) :
        BaseResultFlowAsyncUseCase<Unit,User>{
    override suspend fun execute(params: User?): Flow<ResultData<Unit>> {
        return authRepository.upsertUser(params!!)
    }
}