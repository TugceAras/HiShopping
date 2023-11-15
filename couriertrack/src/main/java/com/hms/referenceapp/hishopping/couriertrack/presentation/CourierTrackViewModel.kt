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
package com.hms.referenceapp.hishopping.couriertrack.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.mapkit.model.CommonLatLng
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.couriertrack.domain.CourierPositionUseCase
import com.hms.referenceapp.hishopping.couriertrack.domain.UpdateOrderUseCase
import com.hms.referenceapp.hishopping.couriertrack.domain.model.CourierPositionRequest
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourierTrackViewModel @Inject constructor(
    private val courierPositionUseCase: CourierPositionUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase
) : BaseViewModel() {

    private val _uiState = MutableLiveData<CourierTrackViewState>()
    val uiState: LiveData<CourierTrackViewState> get() = _uiState

    fun getCourierPosition(orderId: String, userPosition: CommonLatLng, storePosition: CommonLatLng) =
        createResultLiveDataFromBaseFlowUseCase(
            courierPositionUseCase,
            CourierPositionRequest(userPosition, storePosition, orderId)
        )

    fun makeOrderDelivered(orderEntity: OrderEntity) = viewModelScope.launch {
        _uiState.value = CourierTrackViewState(loading = true)
        updateOrderUseCase.execute(orderEntity).collect {resultData ->
            when (resultData) {
                is ResultData.Success -> {
                    _uiState.value = CourierTrackViewState(orderUpdated = true)
                }
                is ResultData.Failed -> {
                    resultData.error?.let { _uiState.value = CourierTrackViewState(errorMessage = it) }
                }
            }
        }
    }

}