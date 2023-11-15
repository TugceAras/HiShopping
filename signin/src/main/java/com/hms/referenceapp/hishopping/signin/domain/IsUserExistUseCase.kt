package com.hms.referenceapp.hishopping.signin.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsUserExistUseCase @Inject constructor(private val authRepository: AuthRepository) :
        BaseResultFlowAsyncUseCase<Boolean,String>{
    override suspend fun execute(params: String?): Flow<ResultData<Boolean>> {
        return authRepository.isUserExist(params!!)
    }
}