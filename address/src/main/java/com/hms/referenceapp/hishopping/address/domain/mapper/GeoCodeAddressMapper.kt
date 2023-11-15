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
package com.hms.referenceapp.hishopping.address.domain.mapper

import android.location.Address
import com.hms.referenceapp.hishopping.base.Mapper
import com.hms.referenceapp.hishopping.data.model.UserAddress
import javax.inject.Inject

object GeoCodeAddressMapper : Mapper<Address, UserAddress>() {
    override fun map(from: Address): UserAddress {
        return UserAddress(
            addressLine = getAddressLine(from.thoroughfare,from.subThoroughfare),
            province = from.adminArea,
            country = from.subAdminArea,
            buildingNo = from.subThoroughfare
        )
    }
    private fun getAddressLine(thoroughfare:String?,subThoroughfare:String?):String?{
        return if (thoroughfare != null && subThoroughfare != null)
            "${thoroughfare}, $subThoroughfare"
        else null
    }
}