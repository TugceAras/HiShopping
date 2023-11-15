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
package com.hms.referenceapp.hishopping.data.model

import java.io.Serializable

data class UserAddress(
    val id: String? = null,
    val userId: String? = null,
    val addressLine: String? = null,
    val country: String? = null,
    val province: String? = null,
    val buildingNo: String? = null,
    val aptNo: String? = null,
    val latitude:Double? = null,
    val longitude:Double? = null,
    val title: String? = null
) : Serializable
