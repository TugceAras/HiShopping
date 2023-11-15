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
import com.hms.referenceapp.hishopping.data.datasource.MyCartDataSource
import com.hms.referenceapp.hishopping.data.model.CartItem
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyCartHawkDataSourceImpl @Inject constructor() : MyCartDataSource {

    override suspend fun getAllCardItems(): Flow<ResultData<List<CartItem>>> {
        return resultFlow {
            ResultData.Success((Hawk.get<MutableList<CartItem>>(CART_SP_KEY) ?: mutableListOf()))
        }
    }

    override suspend fun addToBasket(cartItem: CartItem): Flow<ResultData<CartItem>> {
        val list = Hawk.get<MutableList<CartItem>>(CART_SP_KEY) ?: mutableListOf()
        val lastId = list.takeIf { it.isNotEmpty() }?.last()?.id ?: 1
        list.add(cartItem.apply { lastId })
        Hawk.put(CART_SP_KEY, list)
        return resultFlow {
            ResultData.Success(cartItem)
        }
    }

    override suspend fun deleteCartItem(list: List<CartItem>): Flow<ResultData<Any>> {
        val dbList = Hawk.get<MutableList<CartItem>>(CART_SP_KEY) ?: mutableListOf()
        list.forEach { itemToDelete ->
            dbList.find { itemToDelete.id == it.id }?.let { dbList.remove(it) }
        }
        Hawk.put(CART_SP_KEY, dbList)
        return resultFlow {
            ResultData.Success(Any())
        }
    }

    companion object {
        const val CART_SP_KEY = "CART_SP_KEY"
    }

}