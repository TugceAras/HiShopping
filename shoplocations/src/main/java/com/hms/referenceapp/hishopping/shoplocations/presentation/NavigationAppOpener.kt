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
package com.hms.referenceapp.hishopping.shoplocations.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.hms.lib.commonmobileservices.mapkit.model.CommonLatLng
import com.hms.referenceapp.hishopping.core.Device
import com.hms.referenceapp.hishopping.core.MobileServiceType
import com.hms.referenceapp.hishopping.shoplocations.utils.saveData
import com.huawei.hms.analytics.HiAnalytics

class NavigationAppOpener(private val context: Context) {

    fun open(commonLatLng: CommonLatLng): OpenNavigationResult {
        try {
            val intentUri = when (Device.getMobileServiceType(context, MobileServiceType.GMS)) {
                MobileServiceType.GMS -> {
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=${commonLatLng.lat},${commonLatLng.lng}")
                }
                MobileServiceType.HMS -> {
                    when {
                        isPackageInstalled("com.huawei.maps.app") ->
                            Uri.parse("mapapp://navigation?daddr=${commonLatLng.lat},${commonLatLng.lng}")
                        isPackageInstalled("ru.yandex.yandexmaps") ->
                            Uri.parse("yandexmaps://maps.yandex.ru/?pt=${commonLatLng.lng},${commonLatLng.lat}&z=12&l=map")
                        else ->
                            Uri.parse("https://www.google.com/maps/search/?api=1&query=${commonLatLng.lat},${commonLatLng.lng}")

                    }
                }
                MobileServiceType.NON -> {
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=${commonLatLng.lat},${commonLatLng.lng}")
                }
            }
            val intent = Intent(
                Intent.ACTION_VIEW, intentUri
            )
            context.startActivity(intent)
            HiAnalytics.getInstance(context).saveData(
                "NavigationOpen",
                "location",
                commonLatLng.toString()
            )
            return OpenNavigationResult()
        } catch (e: Exception) {
            return OpenNavigationResult(false, e)
        }
    }

    private fun isPackageInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    data class OpenNavigationResult(val opened: Boolean = true, val exception: Exception? = null)
}