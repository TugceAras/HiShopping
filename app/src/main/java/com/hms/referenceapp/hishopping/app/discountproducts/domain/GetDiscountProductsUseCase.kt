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
package com.hms.referenceapp.hishopping.app.discountproducts.domain

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscountProductsUseCase @Inject constructor(private val discountProductsRepository: DiscountProductsRepository) :
    BaseResultFlowAsyncUseCase<List<@JvmSuppressWildcards Product>, DiscountProductsRequest> {

    override suspend fun execute(params: DiscountProductsRequest?): Flow<ResultData<List<Product>>> {
        return discountProductsRepository.getDiscountProducts(params!!)
    }
}