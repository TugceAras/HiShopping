package com.hms.referenceapp.hishopping.address.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.UserAddress
import com.hms.referenceapp.hishopping.data.repository.address.AddressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddAddressDataSource @Inject constructor(
    private val addressRepository: AddressRepository
): BaseResultFlowAsyncDataSource<Unit,UserAddress> {
    override suspend fun getResult(params: UserAddress?): Flow<ResultData<Unit>> {
        return addressRepository.addAddress(params!!)
    }
}