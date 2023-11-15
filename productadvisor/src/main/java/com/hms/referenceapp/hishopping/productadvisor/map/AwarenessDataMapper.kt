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
package com.hms.referenceapp.hishopping.productadvisor.map

import android.app.Activity
import android.content.SharedPreferences
import com.hms.lib.commonmobileservices.awareness.model.BehaviourDataValue
import com.hms.lib.commonmobileservices.awareness.model.HeadsetDataValue
import com.hms.lib.commonmobileservices.awareness.model.TimeDataValue
import com.hms.lib.commonmobileservices.awareness.model.WeatherDataValue
import com.hms.referenceapp.hishopping.productadvisor.*
import com.hms.referenceapp.hishopping.productadvisor.SHARED_PREF_BEHAVIOUR
import com.hms.referenceapp.hishopping.productadvisor.SHARED_PREF_FILE
import com.hms.referenceapp.hishopping.productadvisor.SHARED_PREF_HEADSET
import com.hms.referenceapp.hishopping.productadvisor.SHARED_PREF_TIME
import com.hms.referenceapp.hishopping.productadvisor.SHARED_PREF_WEATHER
import com.hms.referenceapp.hishopping.productadvisor.model.ProductType
import java.util.*

class AwarenessDataMapper(context: Activity) {
    private var mPreferences: SharedPreferences? = null

    init {
        mPreferences = context.getSharedPreferences(
            SHARED_PREF_FILE, android.app.Service.MODE_PRIVATE
        )
    }

    fun getSuggestedProducts(): ArrayList<ProductType> {
        val suggestedProductTypeArray: ArrayList<ProductType> = arrayListOf()
        timeMapper()?.let { suggestedProductTypeArray.add(it) }
        headsetMapper()?.let { suggestedProductTypeArray.add(it) }
        weatherMapper()?.let { suggestedProductTypeArray.add(it) }
        behaviorMapper()?.let { suggestedProductTypeArray.add(it) }
        return suggestedProductTypeArray
    }

    private fun timeMapper(): ProductType? {
        val timeValue = mPreferences?.getString(SHARED_PREF_TIME, "-1")
        var formalClothesCounter = 0
        var casualClothesCounter = 0
        val timeMapValue: String = timeValue!!.replace("-", "").replace("/", "").replace(" ", "")
        if (timeMapValue.isEmpty()) {
            return null
        }
        timeMapValue.forEach {
            if (TimeDataValue.valueOf(
                    it.toString().toInt()
                ) == TimeDataValue.TIME_CATEGORY_WEEKDAY
            ) {
                formalClothesCounter++
            } else if (TimeDataValue.valueOf(
                    it.toString().toInt()
                ) == TimeDataValue.TIME_CATEGORY_HOLIDAY
            ) {
                casualClothesCounter++
            }
        }
        return if (formalClothesCounter > casualClothesCounter) {
            ProductType.FORMER_CLOTHES
        } else {
            ProductType.CASUAL_CLOTHES
        }
    }

    private fun headsetMapper(): ProductType? {
        val headsetValue = mPreferences?.getString(SHARED_PREF_HEADSET, "-1")
        var headsetCounter = 0
        var nonHeadsetCounter = 0
        var replacedValues: String =
            headsetValue?.replace("-", "")!!.replace("/", "").replace(" ", "")
        if (replacedValues.isEmpty()) {
            return null
        }
        if (replacedValues.length > 10) {
            replacedValues = replacedValues.substring(replacedValues.length - 10);
        }

        replacedValues.forEach {
            if (HeadsetDataValue.valueOf(it.toString().toInt()) == HeadsetDataValue.HEADSET_TRUE) {
                headsetCounter++
            } else {
                nonHeadsetCounter++
            }
        }
        return if (headsetCounter > 0) {
            ProductType.HEADPHONE_PRODUCTS
        } else {
            null
        }
    }

    private fun weatherMapper(): ProductType? {
        val weatherValue = mPreferences?.getString(SHARED_PREF_WEATHER, "-1")
        val weathersDataArray: ArrayList<Int> = arrayListOf()
        var summerClothesCounter = 0
        var winterClothesCounter = 0
        var springClothesCounter = 0
        val replacedValues: String =
            weatherValue?.replace("-", "")/*.replace("/","")*/!!.replace(" ", "")
        var data = ""
        replacedValues.forEach {
            if (it != '/') {
                data += it
                weathersDataArray.add(it.toInt())
            } else {

                data = ""
            }
        }
        if (weathersDataArray.isEmpty()) {
            return null
        }
        weathersDataArray.forEach {
            if (WeatherDataValue.valueOf(it.toString().toInt()) == WeatherDataValue.WEATHER_HOT
                || WeatherDataValue.valueOf(it.toString().toInt()) == WeatherDataValue.WEATHER_DEARY
                || WeatherDataValue.valueOf(it.toString().toInt()) == WeatherDataValue.WEATHER_CLEAR
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_MOSTLY_CLEAR
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_MOSTLY_SUNNY
                || WeatherDataValue.valueOf(it.toString().toInt()) == WeatherDataValue.WEATHER_SUNNY
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_PARTLY_SUNNY
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_CLOUDS
            ) {
                summerClothesCounter++
            } else if (WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_COLD
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_FREEZING_RAIN
                || WeatherDataValue.valueOf(it.toString().toInt()) == WeatherDataValue.WEATHER_ICE
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_MOSTLY_CLOUDY_WITH_SNOW
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_MOSTLY_CLOUDY_WITH_SNOW_2
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_MOSTLY_CLOUDY_WITH_FLURRIES
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_MOSTLY_CLOUDY_WITH_FLURRIES_2
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_RAIN_AND_SNOW
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_PARTLY_CLOUDY_WITH_T_STORMS || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_SHOWERS
                || WeatherDataValue.valueOf(it.toString().toInt()) == WeatherDataValue.WEATHER_SLEET
                || WeatherDataValue.valueOf(it.toString().toInt()) == WeatherDataValue.WEATHER_SNOW
                || WeatherDataValue.valueOf(
                    it.toString().toInt()
                ) == WeatherDataValue.WEATHER_T_STORMS
            ) {
                winterClothesCounter++
            } else {
                springClothesCounter++
            }

        }
        return if (summerClothesCounter > winterClothesCounter) {
            if (summerClothesCounter > springClothesCounter) {
                ProductType.SUMMER_CLOTHES
            } else {
                ProductType.SPRING_CLOTHES
            }
        } else {
            if (winterClothesCounter > springClothesCounter) {
                ProductType.WINTER_CLOTHES
            } else {
                ProductType.SPRING_CLOTHES
            }
        }
    }

    private fun behaviorMapper(): ProductType? {
        val behaviorValue = mPreferences?.getString(SHARED_PREF_BEHAVIOUR, "-1")
        var sportClothesCounter = 0
        var officeItemCounter = 0
        var carItemCounter = 0
        val selectedRandomItemsArray: ArrayList<ProductType> = arrayListOf()
        var replacedValues: String =
            behaviorValue!!.replace("-", "").replace("/", "").replace(" ", "")
        if (replacedValues.isEmpty()) {
            return null
        }
        if (replacedValues.length > 10) {
            replacedValues = replacedValues.substring(replacedValues.length - 10);
        }
        replacedValues.forEach {
            when {
                BehaviourDataValue.valueOf(
                    it.toString().toInt()
                ) == BehaviourDataValue.BEHAVIOR_RUNNING -> {
                    sportClothesCounter++
                }
                BehaviourDataValue.valueOf(
                    it.toString().toInt()
                ) == BehaviourDataValue.BEHAVIOR_WALKING -> {
                    sportClothesCounter++
                }
                BehaviourDataValue.valueOf(
                    it.toString().toInt()
                ) == BehaviourDataValue.BEHAVIOR_STILL -> {
                    officeItemCounter++
                }
                BehaviourDataValue.valueOf(
                    it.toString().toInt()
                ) == BehaviourDataValue.BEHAVIOR_IN_VEHICLE -> {
                    carItemCounter++
                }
            }

        }
        when {
            0 < sportClothesCounter -> {
                selectedRandomItemsArray.add(ProductType.SPORT_CLOTHES)
            }
            0 < carItemCounter -> {
                selectedRandomItemsArray.add(ProductType.CAR_STUFF)
            }
            0 < officeItemCounter -> {
                selectedRandomItemsArray.add(ProductType.OFFICE_STUFF)
            }
            else -> {
                selectedRandomItemsArray.add(ProductType.SPORT_CLOTHES)
            }
        }
        return selectedRandomItemsArray[generateRandomValue(0, selectedRandomItemsArray.size - 1)]
    }

    fun suggestClothes(suggestedProductTypeArray: ArrayList<ProductType>): ProductType {
        return suggestedProductTypeArray[generateRandomValue(0, suggestedProductTypeArray.size - 1)]
    }

    private fun generateRandomValue(min: Int, max: Int): Int {
        return Random().nextInt(max - min + 1) + min
    }
}