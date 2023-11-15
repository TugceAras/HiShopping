// See the License for the specific language governing permissions and
// limitations under the License.
//
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.referenceapp.hishopping.data.repository.address

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.clouddb.CloudDbDataSource
import com.hms.referenceapp.hishopping.data.datasource.AddressDataSource
import com.hms.referenceapp.hishopping.data.mapper.UserAddressMapper
import com.hms.referenceapp.hishopping.data.model.UserAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(private val dataSource: AddressDataSource,private val cloudDbDataSource: CloudDbDataSource) :
    AddressRepository {

    override fun getAllAddresses(userId: String): Flow<ResultData<List<UserAddress>>> = flow {
        emit(ResultData.Loading())
        try {
            val result = cloudDbDataSource.getUserAddresses(userId)
            val userAddresses = result.map { UserAddressMapper.toEntity(it) }
            emit(ResultData.Success(userAddresses))
        }catch (e:Exception){
            emit(ResultData.Failed(e.message))
        }
    }

    override fun getByTitle(title: String): Flow<ResultData<UserAddress>> {
        return dataSource.getByTitle(title)
    }

    override suspend fun addAddress(address: UserAddress) = flow {
        emit(ResultData.Loading())
        try {
            cloudDbDataSource.addAddress(
                UserAddressMapper.fromEntity(address)
            )
            emit(ResultData.Success(Unit))
        }catch (e:Exception){
            emit(ResultData.Failed(e.message))
        }
    }

    override suspend fun removeAddress(address: UserAddress) = flow {
        emit(ResultData.Loading())
        try {
            cloudDbDataSource.deleteAddress(
                UserAddressMapper.fromEntity(address)
            )
            emit(ResultData.Success(Unit))
        }catch (e:Exception){
            emit(ResultData.Failed(e.message))
        }
    }

    override suspend fun update(address: UserAddress) {
        dataSource.update(address)
    }

}