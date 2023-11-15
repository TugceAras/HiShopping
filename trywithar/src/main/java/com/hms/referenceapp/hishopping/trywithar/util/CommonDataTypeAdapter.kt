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
package com.hms.referenceapp.hishopping.trywithar.util

import com.google.gson.*
import com.hms.lib.commonmobileservices.scene.arview.model.ArViewParams
import com.hms.lib.commonmobileservices.scene.common.CommonData
import com.hms.lib.commonmobileservices.scene.common.ViewType
import com.hms.lib.commonmobileservices.scene.faceview.model.FaceViewParams
import com.hms.lib.commonmobileservices.scene.sceneview.model.SceneViewParams
import java.lang.reflect.Type

class CommonDataTypeAdapter
    : JsonDeserializer<CommonData> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CommonData? {
        val jObject = json as JsonObject
        val typeObj = jObject["viewType"]

        if (typeObj != null) {
            when (typeObj.asString) {

                ViewType.AR_VIEW.name -> return context!!.deserialize(json, ArViewParams::class.java)

                ViewType.FACE_VIEW.name -> return context!!.deserialize(json, FaceViewParams::class.java)

                ViewType.SCENE_VIEW.name -> return context!!.deserialize(json, SceneViewParams::class.java)

            }
        }
        return null
    }

    private fun getObjectClass(className: String?): Class<*> {
        try {
            return Class.forName(className.toString())
        } catch (e: ClassNotFoundException) {
            throw JsonParseException(e)
        }
    }
}