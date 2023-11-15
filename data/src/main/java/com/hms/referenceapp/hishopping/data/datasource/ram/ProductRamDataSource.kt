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

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.base.extensions.resultFlow
import com.hms.referenceapp.hishopping.data.datasource.ProductDataSource
import com.hms.referenceapp.hishopping.data.model.Product
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class ProductRamDataSource @Inject constructor(private val ramDb: RamDb) : ProductDataSource {

    override suspend fun getAll(): Flow<ResultData<List<Product>>> {
        return resultFlow {
            ResultData.Success(ramDb.products)
        }
    }

    override suspend fun loadAllByIds(productIds: IntArray): Flow<ResultData<List<Product>>> {
        return resultFlow {
            ResultData.Success(ramDb.products.filter { productIds.contains(it.id) })
        }
    }

    override suspend fun findByTitle(first: String, last: String): Flow<ResultData<Product>> {
        return resultFlow {
            ResultData.Success(ramDb.products.firstOrNull {
                it.title.contains(first)
                        && it.description.contains(last)
            })
        }
    }

    override suspend fun insertAll(products: List<Product>): Flow<ResultData<Unit>> {
        return resultFlow {
            ramDb.products.addAll(products)
            ResultData.Success()
        }
    }

    override suspend fun delete(product: Product): Flow<ResultData<Unit>> {
        return resultFlow {
            ramDb.products.firstOrNull { it.id == product.id }?.let {
                ramDb.products.remove(it)
            }
            ResultData.Success()
        }
    }

    override suspend fun loadAllByCategory(categoryKeyWordsList: List<String>): Flow<ResultData<List<Product>>> {
        return resultFlow {
            ResultData.Success(ramDb.products.filter {
                categoryKeyWordsList.contains(it.category.toLowerCase(Locale.ROOT))
            })
        }
    }

    override suspend fun loadById(productId: Int): Flow<ResultData<Product>> {
        return resultFlow {
            ResultData.Success(ramDb.products.firstOrNull {
                it.id == productId
            })
        }
    }

    override suspend fun searchProduct(searchKeyWord: String): Flow<ResultData<List<Product>>> {
        return resultFlow {
            ResultData.Success(ramDb.products.filter {
                it.title.toLowerCase(Locale.ROOT).contains(searchKeyWord)
            })
        }
    }

    override suspend fun getArProducts(): Flow<ResultData<List<Product>>> {
        return resultFlow {
            ResultData.Success(ramDb.products.asSequence().filter { it.arSupported }.toList())
        }
    }

}