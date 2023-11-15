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
package com.hms.referenceapp.hishopping.address.presentation.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.mapkit.model.CommonLatLng
import com.hms.referenceapp.hishopping.address.domain.mapper.GeoCodeAddressMapper
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.UserAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() :
    BaseViewModel() {

    private var _address = MutableLiveData<ResultData<UserAddress>>()
    private val mapper = GeoCodeAddressMapper

    val address: LiveData<ResultData<UserAddress>>
        get() = _address

    fun fetchAddress(context: Context, commonLatLng: CommonLatLng) {
        try {
            _address.postValue(ResultData.Loading())
            val address: List<Address>
            val geoCoder = Geocoder(context, Locale.getDefault())
            address = geoCoder.getFromLocation(commonLatLng.lat, commonLatLng.lng, 1)
            _address.postValue(ResultData.Success(mapper.map(address[0])))
        }catch (e:Exception){
            _address.postValue(ResultData.Failed())
        }

    }
}