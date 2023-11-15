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

import androidx.room.*
import com.hms.referenceapp.hishopping.data.datasource.roomdb.dtomodel.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Products")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM Products WHERE id IN (:productId)")
    suspend fun loadAllByIds(productId: IntArray): List<Product>

    @Query("SELECT * FROM Products WHERE id = :productId")
    suspend fun loadById(productId: Int): Product

    @Query("SELECT * FROM Products WHERE category LIKE '%' || :categoryKeyWordsList || '%'")
    suspend fun loadAllByCategories(categoryKeyWordsList: List<String>): List<Product>

    @Query("SELECT * FROM Products WHERE title LIKE '%' || :searchKeyWord || '%'")
    suspend fun searchProductName(searchKeyWord: String): List<Product>

    @Query(
        "SELECT * FROM Products WHERE title LIKE :first AND " +
                "description LIKE :last LIMIT 1"
    )
    suspend fun findByTitle(first: String, last: String): Product

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Delete
    suspend fun delete(product: Product)
}