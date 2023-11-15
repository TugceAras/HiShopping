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
package com.hms.referenceapp.hishopping.app.categories.presentation

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.app.categories.domain.CategoryProductsRequest
import com.hms.referenceapp.hishopping.app.categories.domain.GetCategoriesWithProductsUseCase
import com.hms.referenceapp.hishopping.app.categories.presentation.model.CategoryCommand
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.core.SingleLiveData
import com.hms.referenceapp.hishopping.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoryProductsUseCase: BaseResultFlowAsyncUseCase<List<@JvmSuppressWildcards Product>, CategoryProductsRequest>,
    private val getCategoriesWithProductsUseCase: GetCategoriesWithProductsUseCase,
) : BaseViewModel() {

    val commands = SingleLiveData<CategoryCommand>()

    fun getCategoryProducts(keywords: List<String>) = liveData {
        emit(ResultData.Loading())
        getCategoryProductsUseCase.execute(CategoryProductsRequest(keywords)).collect {
            emit(it)
        }
    }

    fun getCategoryProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            getCategoriesWithProductsUseCase.execute().collect {
                withContext(Dispatchers.Main) {
                    when(it) {
                        is ResultData.Loading ->  setCommand(CategoryCommand.ToggleLoadingDialog(true))
                        is ResultData.Success -> {
                            setCommand(CategoryCommand.ToggleLoadingDialog(false))
                            setCommand(CategoryCommand.LoadCategories(it.data!!))
                        }
                        is ResultData.Failed -> Unit
                    }
                }
            }
        }
    }

    fun setCommand(command: CategoryCommand) {
        commands.value = command
    }
}