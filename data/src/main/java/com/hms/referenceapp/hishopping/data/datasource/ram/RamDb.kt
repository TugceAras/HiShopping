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
package com.hms.referenceapp.hishopping.data.datasource.ram

import android.content.Context
import com.hms.referenceapp.hishopping.data.R
import com.hms.referenceapp.hishopping.data.model.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type
import javax.inject.Inject

class RamDb @Inject constructor(private val context: Context) {
    val comments = arrayListOf<Comment>()
    val products = arrayListOf<Product>()
    val stores = arrayListOf<Store>()
    val brands = arrayListOf<Brand>()
    val campaigns = arrayListOf<Campaign>()
    val orders = arrayListOf<Order>()

    init {
        populateList(R.raw.products, products)
        populateList(R.raw.comments, comments)
        populateList(R.raw.stores, stores)
        populateList(R.raw.brands, brands)
        populateList(R.raw.campaigns, campaigns)
        populateList(R.raw.orders, orders)
    }

    private inline fun <reified T> populateList(
        jsonResource: Int,
        mutableList: ArrayList<T>
    ) {
        val jsonString =
            context.resources.openRawResource(jsonResource).bufferedReader()
                .use { it.readText() }

        val type: Type = Types.newParameterizedType(
            List::class.java, T::class.java
        )
        val adapter: JsonAdapter<List<T>> = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build().adapter(type)
        mutableList.addAll(adapter.fromJson(jsonString)!!.toList())
    }
}