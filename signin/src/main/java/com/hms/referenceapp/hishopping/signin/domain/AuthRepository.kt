package com.hms.referenceapp.hishopping.signin.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.model.User
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    suspend fun upsertUser(user: User): Flow<ResultData<Unit>>
    suspend fun isUserExist(userId:String): Flow<ResultData<Boolean>>
}