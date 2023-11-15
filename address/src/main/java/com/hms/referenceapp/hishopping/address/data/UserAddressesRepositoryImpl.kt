package com.hms.referenceapp.hishopping.address.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.address.domain.UserAddressRepository
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.UserAddress
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserAddressesRepositoryImpl @Inject constructor(
    private val getAllAddressesDataSource: BaseResultFlowAsyncDataSource<List<UserAddress>,String>,
    private val addAddressDataSource: BaseResultFlowAsyncDataSource<Unit,UserAddress>,
    private val deleteAddressDataSource: BaseResultFlowAsyncDataSource<Unit,UserAddress>
) : UserAddressRepository {
    override suspend fun getUserAddresses(userId: String): Flow<ResultData<List<UserAddress>>> {
        return getAllAddressesDataSource.getResult(userId)
    }

    override suspend fun addAddress(userAddress: UserAddress): Flow<ResultData<Unit>> {
        return addAddressDataSource.getResult(userAddress)
    }

    override suspend fun deleteAddress(userAddress: UserAddress): Flow<ResultData<Unit>> {
        return deleteAddressDataSource.getResult(userAddress)
    }
}