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
package com.hms.referenceapp.hishopping.app.discountproducts.data

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.lib.commonmobileservices.core.*
import com.hms.lib.commonmobileservices.core.handleSuccessSuspend
import com.hms.referenceapp.hishopping.app.discountproducts.domain.DiscountProductsRequest
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import kotlin.random.Random

class DiscountProductsDataSource @Inject constructor(private val productRepository: ProductRepository) :
    BaseResultFlowAsyncDataSource<List<@JvmSuppressWildcards Product>, DiscountProductsRequest> {
    override suspend fun getResult(params: DiscountProductsRequest?): Flow<ResultData<List<Product>>> {
        return productRepository.getAll().transform { products ->
            products.handleSuccessSuspend {
                val list = it.data!!.takeLast(params!!.size).apply {
                    forEach {
                        it.discountPercentage = Random.nextDouble(0.01, 0.31)
                    }
                }
                emit(ResultData.Success(list))
            }
        }
    }
}