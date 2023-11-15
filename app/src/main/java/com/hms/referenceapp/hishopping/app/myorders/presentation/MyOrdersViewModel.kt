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
package com.hms.referenceapp.hishopping.app.myorders.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.core.handleSuccess
import com.hms.referenceapp.hishopping.app.myorders.domain.GetAllOrdersUseCase
import com.hms.referenceapp.hishopping.app.myorders.domain.UpsertOrderUseCase
import com.hms.referenceapp.hishopping.app.myorders.domain.model.OrderViewItem
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.Order
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val getMyOrdersUseCase: BaseRetrieveResultFlowAsyncUseCase<@JvmSuppressWildcards List<@JvmSuppressWildcards OrderViewItem>>,
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val upsertOrderUseCase: UpsertOrderUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<OrdersViewState>()
    val uiState: LiveData<OrdersViewState> get() = _uiState

    fun getMyOrders() = liveData {
        emit(ResultData.Loading())
        getMyOrdersUseCase.execute().collect {
            emit(it.handleSuccess {
                it.data?.sortedWith { o1, _ -> if (o1!!.order.orderStatus == Order.OrderStatus.TRANSPORT) -1 else 1 }
            })
        }
    }

    fun getAllOrders(userId:String) = viewModelScope.launch {
        _uiState.value = OrdersViewState(loading = false)
        getAllOrdersUseCase.execute(userId).collect { resultData ->
            when(resultData){
                is ResultData.Success -> {
                    resultData.data?.let { _uiState.value = OrdersViewState(orderList = it) }
                }
                is ResultData.Failed -> {
                    _uiState.value = OrdersViewState(error = resultData.error)
                }
            }
        }
    }

    fun upsertOrder(orderEntity: OrderEntity) = viewModelScope.launch {
        upsertOrderUseCase.execute(orderEntity).collect{resultData->
            when(resultData){
                is ResultData.Success -> {
                    Log.d("upsertOrder","SUCCESS")
                }
                is ResultData.Failed -> {
                    Log.d("upsertOrder","FAILURE")
                }
            }
        }
    }
}