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
package com.hms.referenceapp.hishopping.app.discountproducts.presentation

import javax.inject.Inject
import androidx.lifecycle.liveData
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.app.discountproducts.domain.DiscountProductsRequest
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect

@HiltViewModel
class DiscountProductsViewModel @Inject constructor(private val discountProductsUseCase:
                                                             BaseResultFlowAsyncUseCase<@JvmSuppressWildcards List<Product>, DiscountProductsRequest>
): BaseViewModel() {


    fun getDiscountProducts(size: Int) = liveData{
        emit(ResultData.Loading())
        discountProductsUseCase.execute(DiscountProductsRequest(size)).collect {
            emit(it)
        }
    }
}