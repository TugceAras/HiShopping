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
package com.hms.referenceapp.hishopping.shoplocations.domain.entity

import android.net.Uri
import com.hms.referenceapp.hishopping.data.model.Store

data class ShopLocation(
    val id: Int,
    val visibleName: String,
    val address: String,
    val location: Pair<Double, Double>,
    val shopImage: Uri
)

fun Store.toShopLocation(locationParam: Pair<Double, Double>? = null): ShopLocation {
    return ShopLocation(
        id,
        visibleName,
        address,
        locationParam ?: location.lat to location.lng,
        Uri.parse(shopImage)
    )
}