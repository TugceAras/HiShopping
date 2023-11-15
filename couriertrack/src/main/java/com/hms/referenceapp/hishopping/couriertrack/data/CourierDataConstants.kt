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

class CourierDataConstants {
    companion object {
        const val ARRIVED_DISTANCE: Double = 0.00001
        const val RANDOM_MOVE_DISTANCE: Double = 0.0001
        const val DISTANCE_COEFFICIENT:Int = 5
        const val DISTANCE_ZONE_COEFFICIENT:Int = 5
        const val RANDOM_INITIAL_DISTANCE_MIN: Double = 0.0008
        const val RANDOM_INITIAL_DISTANCE_MAX: Double = 0.002
        const val REFRESH_PERIOD_MSECS: Long = 1000L
    }
}