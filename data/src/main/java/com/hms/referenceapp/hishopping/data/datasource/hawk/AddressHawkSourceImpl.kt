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
package com.hms.referenceapp.hishopping.data.datasource.hawk

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.base.extensions.resultFlow
import com.hms.referenceapp.hishopping.data.datasource.AddressDataSource
import com.hms.referenceapp.hishopping.data.model.UserAddress
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddressHawkSourceImpl @Inject constructor() :
    AddressDataSource {
    override fun getAll(): Flow<ResultData<List<UserAddress>>> {
        return resultFlow {
            ResultData.Success(
                (Hawk.get<MutableList<UserAddress>>(ADDRESS_SP_KEY) ?: mutableListOf())
            )
        }
    }

    override fun getByTitle(title: String): Flow<ResultData<UserAddress>> {
        return resultFlow {
            ResultData.Success(
                (Hawk.get<MutableList<UserAddress>>(ADDRESS_SP_KEY)
                    ?: mutableListOf()).firstOrNull {
                    it.title == title
                })
        }
    }

    override suspend fun add(address: UserAddress) {
        val list = Hawk.get<MutableList<UserAddress>>(ADDRESS_SP_KEY) ?: mutableListOf()
        list.indexOfFirst {
            it.title ==
                    address.title
        }.takeIf { it != -1 }?.let {
            //card is found: updating
            list[it] = address
        } ?: run {
            list.add(0, address)
        }
        Hawk.put(ADDRESS_SP_KEY, list)
    }

    override suspend fun remove(address: UserAddress) {
        val list = Hawk.get<MutableList<UserAddress>>(ADDRESS_SP_KEY) ?: mutableListOf()
        list.firstOrNull {
            it.title == address.title
        }?.let {
            list.remove(it)
        }
        Hawk.put(ADDRESS_SP_KEY, list)
    }

    override suspend fun update(address: UserAddress) {
        val list = Hawk.get<MutableList<UserAddress>>(ADDRESS_SP_KEY) ?: mutableListOf()

        list.indexOfFirst {
            it.title ==
                    address.title
        }.takeIf { it != -1 }?.let {
            list[it] = address
        } ?: run {
            list.add(0, address)
        }
        Hawk.put(ADDRESS_SP_KEY, list)
    }

    companion object {
        const val ADDRESS_SP_KEY = "ADDRESS_SP_KEY"
    }

}