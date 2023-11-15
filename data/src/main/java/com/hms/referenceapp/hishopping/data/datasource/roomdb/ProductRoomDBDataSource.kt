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
package com.hms.referenceapp.hishopping.data.datasource.roomdb

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.base.extensions.resultFlow
import com.hms.referenceapp.hishopping.data.datasource.ProductDataSource
import com.hms.referenceapp.hishopping.data.datasource.roomdb.dtomodel.toProduct
import com.hms.referenceapp.hishopping.data.model.Product
import kotlinx.coroutines.flow.Flow


class ProductDataSourceImpl(private val productDao: ProductDao) : ProductDataSource {

    override suspend fun getAll(): Flow<ResultData<List<Product>>> {
        return resultFlow {
            ResultData.Success(productDao.getAll().map { it.toProduct() })
        }
    }

    override suspend fun loadAllByIds(productIds: IntArray): Flow<ResultData<List<Product>>> {
        return resultFlow {
            ResultData.Success(productDao.loadAllByIds(productIds).map { it.toProduct() })
        }
    }

    override suspend fun loadById(productId: Int): Flow<ResultData<Product>> {
        return resultFlow {
            ResultData.Success(productDao.loadById(productId).toProduct())
        }
    }

    override suspend fun searchProduct(searchKeyWord: String): Flow<ResultData<List<Product>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getArProducts(): Flow<ResultData<List<Product>>> {
        TODO("Not yet implemented")
    }

    override suspend fun findByTitle(first: String, last: String): Flow<ResultData<Product>> {
        return resultFlow {
            ResultData.Success(productDao.findByTitle(first, last).toProduct())
        }
    }

    override suspend fun insertAll(products: List<Product>): Flow<ResultData<Unit>> {
        return resultFlow {
            productDao.insertAll(products.map { it.toProduct() })
            ResultData.Success()
        }
    }

    override suspend fun delete(product: Product): Flow<ResultData<Unit>> {
        return resultFlow {
            productDao.delete(product.toProduct())
            ResultData.Success()
        }
    }

    override suspend fun loadAllByCategory(categoryKeyWordsList: List<String>): Flow<ResultData<List<Product>>> {
        return resultFlow {
            ResultData.Success(
                productDao.loadAllByCategories(categoryKeyWordsList).map { it.toProduct() })
        }
    }

}

