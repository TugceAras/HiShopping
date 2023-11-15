package com.hms.referenceapp.hishopping.app.profileedit.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileEditRepository {
    suspend fun updateUser(user:User):
            Flow<ResultData<Unit>>
}