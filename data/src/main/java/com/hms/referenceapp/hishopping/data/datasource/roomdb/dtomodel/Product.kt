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
package com.hms.referenceapp.hishopping.data.datasource.roomdb.dtomodel

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Products")
@Parcelize
data class Product(
    @PrimaryKey var id: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "price") val price: Double?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "videoUrl") val videoUrl: String?
) : Parcelable

fun Product.toProduct(): com.hms.referenceapp.hishopping.data.model.Product {
    return com.hms.referenceapp.hishopping.data.model.Product(
        arSupported = false,
        availableStores = listOf(),
        category = category!!,
        description = description!!,
        id = id!!,
        image = listOf(),
        price = price!!,
        title = title!!,
        videoUrl = videoUrl!!
    )
}

fun com.hms.referenceapp.hishopping.data.model.Product.toProduct(): Product {
    return Product(
        category = category!!,
        description = description!!,
        id = id!!,
        price = price!!,
        title = title!!,
        videoUrl = videoUrl!!
    )
}