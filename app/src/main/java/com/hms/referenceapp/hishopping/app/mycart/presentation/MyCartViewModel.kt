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
package com.hms.referenceapp.hishopping.app.mycart.presentation

import androidx.lifecycle.liveData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.model.CartItem
import com.hms.referenceapp.hishopping.data.model.Order
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyCartViewModel @Inject constructor(
    private val getAllMyCartUseCase: BaseRetrieveResultFlowAsyncUseCase<@JvmSuppressWildcards List<CartItem>>,
    private val upsertOrderUseCase: BaseResultFlowAsyncUseCase<Unit, OrderEntity>,
    private val deleteCartItemUseCase: BaseResultFlowAsyncUseCase<Any, @JvmSuppressWildcards List<CartItem>>
): BaseViewModel(){

    fun getMyCarts() = liveData {
        emit(ResultData.Loading())
        getAllMyCartUseCase.execute().collect {
            emit(it)
        }
    }

    fun addOrders(myCardItems: List<CartItem>) = liveData {
        emit(null)
//        emit(ResultData.Loading())
//        var hasErrors=false
//        myCardItems.forEach {
//            upsertOrderUseCase.execute(Order(-1,it.productId!!,"address",Order.OrderStatus.TRANSPORT)).collect {
//                if(it is ResultData.Failed) hasErrors=true
//            }
//        }
//       emit(if(hasErrors) ResultData.Failed() else ResultData.Success())
    }

    fun deleteCartItems(list : List<CartItem>) = liveData {
        emit(ResultData.Loading())
        deleteCartItemUseCase.execute(list).collect {
            emit(it)
        }
    }
}