package com.hms.referenceapp.hishopping.address.domain

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.model.UserAddress
import kotlinx.coroutines.flow.Flow

interface UserAddressRepository {
    suspend fun getUserAddresses(userId:String):
            Flow<ResultData<List<UserAddress>>>
    suspend fun addAddress(userAddress: UserAddress):
            Flow<ResultData<Unit>>
    suspend fun deleteAddress(userAddress: UserAddress):
            Flow<ResultData<Unit>>
}