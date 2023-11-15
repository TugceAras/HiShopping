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
package com.hms.referenceapp.hishopping.address.presentation.addresses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.identity.model.UserAddressResponse
import com.hms.referenceapp.hishopping.address.R
import com.hms.referenceapp.hishopping.address.domain.mapper.IdentityAddressMapper
import com.hms.referenceapp.hishopping.address.domain.usecase.DeleteAddressUseCase
import com.hms.referenceapp.hishopping.address.domain.usecase.GetAllAddressesUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.core.UIText
import com.hms.referenceapp.hishopping.data.model.UserAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getAllAddressesUseCase: GetAllAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
) : BaseViewModel() {

    private val mapper = IdentityAddressMapper

    private val _identityAddress = MutableLiveData<UserAddress?>()
    val identityAddress: LiveData<UserAddress?>
        get() = _identityAddress

    private val _addresses = MutableLiveData<ResultData<List<UserAddress>>>()
    val addresses: LiveData<ResultData<List<UserAddress>>>
        get() = _addresses

    private val _uiState = MutableLiveData<AddressesViewState>()
    val uiState:LiveData<AddressesViewState> get() = _uiState

    fun setAddress(address: UserAddressResponse?) {
        address?.let {
            this._identityAddress.value = mapper.map(address)
        }?: run { this._identityAddress.value = null }
    }

    fun fetchAddresses(userId:String) = viewModelScope.launch {
        getAllAddressesUseCase.execute(userId).collect{ result ->
            when(result){
                is ResultData.Success -> {
                    _uiState.value = AddressesViewState(addressList = result.data)
                }
                is ResultData.Loading -> {
                    _uiState.value = AddressesViewState(loading = true)
                }
                is ResultData.Failed -> {
                    result.error?.let {
                        _uiState.value = AddressesViewState(error = UIText.StringText(it))
                    }?: run {
                        _uiState.value = AddressesViewState(error = UIText.ResourceText(R.string.unexpected_error))
                    }
                }
            }
        }
    }

    fun deleteAddress(address: UserAddress) = viewModelScope.launch {
        deleteAddressUseCase.execute(address).collect{ result ->
            when(result){
                is ResultData.Success -> {
                    _uiState.value = _uiState.value?.copy(loading = false, error = null)
                    address.userId?.let { fetchAddresses(it) }
                }
                is ResultData.Loading -> {_uiState.value = _uiState.value?.copy(loading = true)}
                is ResultData.Failed -> {
                    result.error?.let {
                        _uiState.value = _uiState.value?.copy(loading = false, error = UIText.StringText(it))
                    }?: run {
                        _uiState.value = _uiState.value?.copy(loading = false, error = UIText.ResourceText(R.string.unexpected_error))
                    }
                }
            }
        }
    }
}