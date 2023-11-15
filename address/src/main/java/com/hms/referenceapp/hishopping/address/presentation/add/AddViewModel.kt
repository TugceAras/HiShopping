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
package com.hms.referenceapp.hishopping.address.presentation.add

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.address.R
import com.hms.referenceapp.hishopping.address.domain.usecase.AddAddressUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.core.UIText
import com.hms.referenceapp.hishopping.data.model.UserAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addAddressUseCase: AddAddressUseCase
) : BaseViewModel() {
    private val _uiState = MutableLiveData<AddAddressViewState>()
    val uiState: LiveData<AddAddressViewState> get() = _uiState

    private val _address = MutableLiveData<UserAddress>()
    val address: LiveData<UserAddress>
        get() = _address

    fun setAddress(address: UserAddress) {
        _address.value = address
    }

    fun addAddress(userAddress: UserAddress,context:Context) = viewModelScope.launch {
        try {
            _uiState.value = AddAddressViewState(loading = true)
            validateAddress(userAddress,context)?.let { validatedAddress->
                addAddressUseCase.execute(validatedAddress).collect{ result ->
                    when(result){
                        is ResultData.Success -> { _uiState.value = AddAddressViewState(success = true) }
                        is ResultData.Failed -> {
                            result.error?.let {
                                _uiState.value = AddAddressViewState(error = UIText.StringText(it))
                            }?: run { _uiState.value = AddAddressViewState(error = UIText.ResourceText(R.string.unexpected_error)) }
                        }
                    }
                }
            }?: run { _uiState.value = AddAddressViewState(error = UIText.ResourceText(R.string.unverified_address)) }
        }catch (e:Exception){
            e.message?.let {
                _uiState.value = AddAddressViewState(error = UIText.StringText(it))
            }?: run { _uiState.value = AddAddressViewState(error = UIText.ResourceText(R.string.unexpected_error)) }
        }
    }

    private fun validateAddress(userAddress: UserAddress,context:Context):UserAddress?{
        if (userAddress.longitude != null && userAddress.latitude!=null && !userAddress.title.isNullOrEmpty()
            && !userAddress.addressLine.isNullOrEmpty() && !userAddress.country.isNullOrEmpty() && !userAddress.aptNo.isNullOrEmpty()
            && !userAddress.buildingNo.isNullOrEmpty() && !userAddress.province.isNullOrEmpty()){
            return userAddress
        }
        if (userAddress.title.isNullOrEmpty() || userAddress.addressLine.isNullOrEmpty()
            || userAddress.country.isNullOrEmpty() || userAddress.aptNo.isNullOrEmpty()
            || userAddress.buildingNo.isNullOrEmpty() || userAddress.province.isNullOrEmpty()
        ){
           return null
        }
        if (userAddress.longitude == null || userAddress.latitude == null){
            val locationName = userAddress.addressLine+userAddress.province+userAddress.country
            val locationAddress = Geocoder(context, Locale.getDefault()).getFromLocationName(locationName,1)
            return if (locationAddress.size > 0){
                if (locationAddress[0].latitude != null || locationAddress[0].longitude != null){
                    userAddress.copy(longitude = locationAddress[0].longitude, latitude = locationAddress[0].latitude)
                }else null
            }else null
        }
        return null
    }
}