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

import android.location.Location
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.mapkit.model.CommonLatLng
import com.hms.referenceapp.hishopping.couriertrack.domain.model.CourierPosition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CourierPositionDataSource @Inject constructor(private val courierDataLocalSource: CourierDataLocalSource) {

    @ExperimentalCoroutinesApi
    suspend fun getPosition(
        storeLocation: CommonLatLng,
        userLocation: CommonLatLng,
        orderId: String
    ): Flow<ResultData<CourierPosition>> {

        val userLatLng = Location("userLatLng")
        val storeLatLng = Location("storeLatLng")

        userLatLng.latitude = userLocation.lat
        userLatLng.longitude = userLocation.lng
        storeLatLng.latitude = storeLocation.lat
        storeLatLng.longitude = storeLocation.lng

        val distance = (userLatLng.distanceTo(storeLatLng)/1000).toInt()

        return callbackFlow {
            courierDataLocalSource.getPositionConstantly(storeLocation, userLocation, orderId, distance) {
                if (!isClosedForSend) trySend(ResultData.Success(it))
            }
            awaitClose {
                courierDataLocalSource.isRunning = false
            }
        }
    }
}