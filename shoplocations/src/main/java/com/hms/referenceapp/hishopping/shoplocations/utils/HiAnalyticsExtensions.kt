package com.hms.referenceapp.hishopping.shoplocations.utils

import android.os.Bundle
import android.util.Log
import com.huawei.hms.analytics.HiAnalyticsInstance

inline fun <reified T> HiAnalyticsInstance.saveSingleData(
    eventKey: String,
    paramKey: String? = null,
    data: T
) {
    val bundle = Bundle()
    val paramKeyGen = paramKey ?: eventKey
    when (data) {
        is String -> bundle.putString(paramKeyGen, data)
        is Int -> bundle.putInt(paramKeyGen, data)
        is Long -> bundle.putLong(paramKeyGen, data)
        is Double -> bundle.putDouble(paramKeyGen, data)
        is Float -> bundle.putFloat(paramKeyGen, data)
        else -> {
            Log.i("ShoppingAnalytics", "Invalid type")
        }
    }
    onEvent(eventKey, bundle)
}

//To log data with key and variable number of features
//example: CommonAnalytics.saveData("ProductClick","price",20.99,"title","Gloves","seller","Wintermax")
fun HiAnalyticsInstance.saveData(eventKey: String, vararg keyDataPairs: Any) {
    val bundle = Bundle()
    keyDataPairs.forEachIndexed { index, value ->
        if (index % 2 == 0) {
            when (val data = keyDataPairs[index + 1]) {
                is String -> bundle.putString(value as String, data)
                is Int -> bundle.putInt(value as String, data)
                is Long -> bundle.putLong(value as String, data)
                is Double -> bundle.putDouble(value as String, data)
                is Float -> bundle.putFloat(value as String, data)
                else -> {
                    Log.i("ShoppingAnalytics", "Invalid type")
                }
            }
        }
    }
    onEvent(eventKey, bundle)
}