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
package com.hms.referenceapp.hishopping.app.search.presentation

import javax.inject.Inject
import androidx.lifecycle.liveData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase:
    BaseResultFlowAsyncUseCase<@JvmSuppressWildcards List<Product>, String>
) :
    BaseViewModel() {

    fun getSearchProduct(productName: String) = liveData {
        emit(ResultData.Loading())
        searchUseCase.execute(productName).collect {
            emit(it)
        }
    }
}