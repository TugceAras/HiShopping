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
package com.hms.referenceapp.hishopping.data.repository

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.clouddb.CloudDbDataSource
import com.hms.referenceapp.hishopping.data.datasource.ProductDataSource
import com.hms.referenceapp.hishopping.data.mapper.*
import com.hms.referenceapp.hishopping.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

interface ProductRepository {
    suspend fun getAll(): Flow<ResultData<List<Product>>>
    suspend fun getSameProducts(brandId: String, title: String): Flow<ResultData<List<SameProduct>>>
    suspend fun loadAllByIds(productIds: IntArray): Flow<ResultData<List<Product>>>
    suspend fun loadById(productId: Int): Flow<ResultData<Product>>
    suspend fun findByTitle(first: String, last: String): Flow<ResultData<Product>>
    suspend fun insertAll(products: List<Product>): Flow<ResultData<Unit>>
    suspend fun delete(product: Product): Flow<ResultData<Unit>>
    suspend fun loadAllByCategory(categoryKeyWordsList: List<String>): Flow<ResultData<List<Product>>>
    suspend fun searchProductName(searchKeyWord: String): Flow<ResultData<List<Product>>>
    suspend fun getArProducts(): Flow<ResultData<List<Product>>>
    suspend fun getCategoriesWithProducts(): Flow<ResultData<Map<CategoryEntity, List<ProductEntity>>>>
    suspend fun getFavoriteProducts(userId: String): Flow<ResultData<List<Favorites>>>
    suspend fun deleteFromFavorites(favoriteProduct: Favorites): Flow<ResultData<Unit>>
}

class ProductRepositoryImpl @Inject constructor(private val dataSource: ProductDataSource, private val cloudDbDataSource: CloudDbDataSource) : ProductRepository {

    override suspend fun getAll(): Flow<ResultData<List<Product>>> {
        return dataSource.getAll()
    }

    override suspend fun getSameProducts(
        brandId: String,
        title: String
    ): Flow<ResultData<List<SameProduct>>> = flow {
        emit(ResultData.Loading())
        try {
            val result = cloudDbDataSource.getSameProducts(brandId, title)
            emit(
                ResultData.Success(
                    result.map {
                        SameProductMapper.toSameProduct(it)
                    }
                )
            )
        }catch (e:Exception){
            emit(ResultData.Failed(e.message))
        }
    }

    override suspend fun loadAllByIds(productIds: IntArray): Flow<ResultData<List<Product>>> {
        return dataSource.loadAllByIds(productIds)
    }

    override suspend fun loadById(productId: Int): Flow<ResultData<Product>> {
        return dataSource.loadById(productId)
    }

    override suspend fun findByTitle(first: String, last: String): Flow<ResultData<Product>> {
        return dataSource.findByTitle(first, last)
    }

    override suspend fun insertAll(products: List<Product>): Flow<ResultData<Unit>> {
        return dataSource.insertAll(products)
    }

    override suspend fun delete(product: Product): Flow<ResultData<Unit>> {
        return dataSource.delete(product)
    }

    override suspend fun loadAllByCategory(categoryKeyWordsList: List<String>): Flow<ResultData<List<Product>>> {
        return dataSource.loadAllByCategory(categoryKeyWordsList)
    }

    override suspend fun searchProductName(searchKeyWord: String): Flow<ResultData<List<Product>>> {
        return dataSource.searchProduct(searchKeyWord)
    }

    override suspend fun getArProducts(): Flow<ResultData<List<Product>>> {
        return dataSource.getArProducts()
    }

    override suspend fun getCategoriesWithProducts(): Flow<ResultData<Map<CategoryEntity, List<ProductEntity>>>> = flow {
        emit(ResultData.Loading())
        try {
            val categoryWithProducts = HashMap<CategoryEntity, List<ProductEntity>>()
            val categories = cloudDbDataSource.getCategories()
            categories.forEach { category ->
                val categoryEntity = CategoryMapper.toEntity(category)
                val products = cloudDbDataSource.getProductsByCategory(category.id, limit = 5)

                val productEntityList = products.map {
                    val productImages = cloudDbDataSource.getProductImages(it.id).map { image ->
                        ProductImageMapper.toEntity(image)
                    }
                    ProductMapper.toEntity(it, category = categoryEntity, images = productImages)
                }
                categoryWithProducts[categoryEntity] = productEntityList
            }
            emit(ResultData.Success(categoryWithProducts))
        }catch (e: Exception) {
            emit(ResultData.Failed(error = e.message))
        }
    }

    override suspend fun getFavoriteProducts(userId: String): Flow<ResultData<List<Favorites>>> = flow {
        emit(ResultData.Loading())
        try {
            val favorites = mutableListOf<Favorites>()
            val cloudDbFavorites = cloudDbDataSource.getFavoriteProducts(userId)
            cloudDbFavorites.forEach {
                favorites.add(
                    FavoriteMapper.toEntity(it)
                )
            }
            emit(ResultData.Success(favorites))
        }catch (e:Exception){
            ResultData.Failed(error = e.message)
        }
    }

    override suspend fun deleteFromFavorites(favoriteProduct: Favorites): Flow<ResultData<Unit>> = flow {
        emit(ResultData.Loading())
        try {
            val cloudDbFavoriteObject = FavoriteMapper.fromEntity(favoriteProduct)
            cloudDbDataSource.deleteFromFavorites(cloudDbFavoriteObject)
            emit(ResultData.Success())
        }catch (e:Exception){
            emit(ResultData.Failed(error = e.message))
        }
    }
}