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
package com.hms.referenceapp.hishopping.productadvisor

import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import com.hms.lib.commonmobileservices.awareness.service.manager.IAwarenessAPI.Companion.BEHAVIOR_KEY
import com.hms.lib.commonmobileservices.awareness.service.manager.IAwarenessAPI.Companion.HEADSET_KEY
import com.hms.lib.commonmobileservices.awareness.service.manager.IAwarenessAPI.Companion.TIME_KEY
import com.hms.lib.commonmobileservices.awareness.service.manager.IAwarenessAPI.Companion.WEATHER_KEY

class DataHandlerService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mPreferences: SharedPreferences? = getSharedPreferences(
            SHARED_PREF_FILE, MODE_PRIVATE
        )
        intent?.let { it ->
            it.getIntArrayExtra(TIME_KEY)?.let { arr ->
                var timeValue = ""
                arr.forEach { value ->
                    timeValue += "$value-"
                }
                mPreferences?.edit()!!.putString(SHARED_PREF_TIME, timeValue).apply()
            }
            it.getIntArrayExtra(HEADSET_KEY)?.let { arr ->
                var headsetValue = mPreferences?.getString(SHARED_PREF_HEADSET, "-1") ?: "-1"
                arr.forEach { value ->
                    headsetValue += "$value-"
                }
                headsetValue += " / "
                if (headsetValue.length > 60) {
                    headsetValue = headsetValue.substring(headsetValue.length - 60);
                }
                mPreferences?.edit()!!.putString(SHARED_PREF_HEADSET, headsetValue).apply()
            }
            it.getIntArrayExtra(WEATHER_KEY)?.let { arr ->
                var weatherValue = ""
                arr.forEach { value ->
                    weatherValue += "$value-"
                }
                mPreferences?.edit()!!.putString(SHARED_PREF_WEATHER, weatherValue).apply()
            }
            it.getIntArrayExtra(BEHAVIOR_KEY)?.let { arr ->
                var behaviorValue = mPreferences?.getString(SHARED_PREF_BEHAVIOUR, "-1") ?: "-1"
                arr.forEach { value ->
                    behaviorValue += "$value-"
                }
                behaviorValue += " / "
                if (behaviorValue.length > 10) {
                    behaviorValue = behaviorValue.substring(behaviorValue.length - 10);
                }
                mPreferences?.edit()!!.putString(SHARED_PREF_BEHAVIOUR, behaviorValue).apply()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}