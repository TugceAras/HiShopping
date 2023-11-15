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
package com.hms.referenceapp.hishopping.couriertrack.data

import com.hms.lib.commonmobileservices.mapkit.model.CommonLatLng
import com.hms.referenceapp.hishopping.couriertrack.data.CourierDataConstants.Companion.REFRESH_PERIOD_MSECS
import com.hms.referenceapp.hishopping.couriertrack.domain.model.CourierPosition
import kotlinx.coroutines.delay
import javax.inject.Inject

class CourierDataLocalSource @Inject constructor(private val courierPositionCalculator: CourierPositionCalculator) {

    var isRunning = true

    suspend fun getPositionConstantly(
        initialPosition: CommonLatLng, userLocation: CommonLatLng, orderId: String, distance: Int,
        callback: (courierPosition: CourierPosition) -> Unit
    ) {
        while (isRunning) {
            delay(REFRESH_PERIOD_MSECS)
            if (isRunning) callback.invoke(
                courierPositionCalculator
                    .getPosition(initialPosition, userLocation, orderId,distance)
            )
        }
    }
}