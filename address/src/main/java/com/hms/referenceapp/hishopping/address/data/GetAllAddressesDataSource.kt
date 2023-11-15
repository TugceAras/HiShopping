package com.hms.referenceapp.hishopping.address.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.UserAddress
import com.hms.referenceapp.hishopping.data.repository.address.AddressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAddressesDataSource @Inject constructor(
    private val addressRepository:AddressRepository
): BaseResultFlowAsyncDataSource<List<@JvmSuppressWildcards UserAddress>,String>{
    override suspend fun getResult(params: String?): Flow<ResultData<List<UserAddress>>> {
        return addressRepository.getAllAddresses(params!!)
    }
}