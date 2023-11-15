package com.hms.referenceapp.hishopping.app.profile.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUser(userId: String):
            Flow<ResultData<User>>
}