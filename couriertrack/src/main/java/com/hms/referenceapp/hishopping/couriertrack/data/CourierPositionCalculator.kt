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
import com.hms.referenceapp.hishopping.couriertrack.data.CourierDataConstants.Companion.ARRIVED_DISTANCE
import com.hms.referenceapp.hishopping.couriertrack.data.CourierDataConstants.Companion.DISTANCE_COEFFICIENT
import com.hms.referenceapp.hishopping.couriertrack.data.CourierDataConstants.Companion.DISTANCE_ZONE_COEFFICIENT
import com.hms.referenceapp.hishopping.couriertrack.data.CourierDataConstants.Companion.RANDOM_MOVE_DISTANCE
import com.hms.referenceapp.hishopping.couriertrack.domain.model.CourierPosition
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.random.Random

class CourierPositionCalculator @Inject constructor(
    private val courierCurrentPositionDataSource: CourierCurrentPositionDataSource
) {

    fun getPosition(
        initialPosition: CommonLatLng,
        userLocation: CommonLatLng,
        orderId: String,
        distance: Int
    ): CourierPosition {
        if (courierCurrentPositionDataSource.getCurrentPosition(orderId) == null)
            courierCurrentPositionDataSource.setCurrentPosition(orderId, initialPosition)

        val lastPosition: CommonLatLng =
            courierCurrentPositionDataSource.getCurrentPosition(orderId)!!

        val latAbsoluteValue = (lastPosition.lat - userLocation.lat).absoluteValue
        val lngAbsoluteValue = (lastPosition.lng - userLocation.lng).absoluteValue

        val latArrived = latAbsoluteValue <= ARRIVED_DISTANCE
        val lngArrived = lngAbsoluteValue <= ARRIVED_DISTANCE

        val inLatZone = latAbsoluteValue <= ARRIVED_DISTANCE * DISTANCE_COEFFICIENT * distance * DISTANCE_ZONE_COEFFICIENT
        val inLngZone = lngAbsoluteValue <= ARRIVED_DISTANCE * DISTANCE_COEFFICIENT * distance * DISTANCE_ZONE_COEFFICIENT

        if (latArrived && lngArrived) return CourierPosition(lastPosition, true)

        val nextPosLat = if (latArrived) lastPosition.lat else {
            if (inLatZone){
                if (lastPosition.lat > userLocation.lat)
                    lastPosition.lat - Random.nextDouble(RANDOM_MOVE_DISTANCE)
                else lastPosition.lat + Random.nextDouble(RANDOM_MOVE_DISTANCE)
            }else{
                if (lastPosition.lat > userLocation.lat)
                    lastPosition.lat - Random.nextDouble(RANDOM_MOVE_DISTANCE)*distance*DISTANCE_COEFFICIENT
                else lastPosition.lat + Random.nextDouble(RANDOM_MOVE_DISTANCE)*distance*DISTANCE_COEFFICIENT
            }
        }

        val nextPosLng = if (lngArrived) lastPosition.lng else {
            if (inLngZone){
                if (lastPosition.lng > userLocation.lng)
                    lastPosition.lng - Random.nextDouble(RANDOM_MOVE_DISTANCE)
                else lastPosition.lng + Random.nextDouble(RANDOM_MOVE_DISTANCE)
            }else{
                if (lastPosition.lng > userLocation.lng)
                    lastPosition.lng - Random.nextDouble(RANDOM_MOVE_DISTANCE)*distance*DISTANCE_COEFFICIENT
                else lastPosition.lng + Random.nextDouble(RANDOM_MOVE_DISTANCE)*distance*DISTANCE_COEFFICIENT
            }
        }

        courierCurrentPositionDataSource.setCurrentPosition(
            orderId,
            CommonLatLng(nextPosLat, nextPosLng)
        )

        return CourierPosition(CommonLatLng(nextPosLat, nextPosLng))
    }

}