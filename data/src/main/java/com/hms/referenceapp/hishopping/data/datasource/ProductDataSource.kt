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
package com.hms.referenceapp.hishopping.data.datasource

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductDataSource {
    suspend fun getAll(): Flow<ResultData<List<Product>>>
    suspend fun loadAllByIds(productIds: IntArray): Flow<ResultData<List<Product>>>
    suspend fun findByTitle(first: String, last: String): Flow<ResultData<Product>>
    suspend fun insertAll(products: List<Product>): Flow<ResultData<Unit>>
    suspend fun delete(product: Product): Flow<ResultData<Unit>>
    suspend fun loadAllByCategory(categoryKeyWordsList: List<String>): Flow<ResultData<List<Product>>>
    suspend fun loadById(productId: Int): Flow<ResultData<Product>>
    suspend fun searchProduct(searchKeyWord: String): Flow<ResultData<List<Product>>>
    suspend fun getArProducts(): Flow<ResultData<List<Product>>>
}


