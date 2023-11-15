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
package com.hms.referenceapp.hishopping.productadvisor.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductAdvisorViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel() {

    private val _productLiveData = MutableLiveData<ResultData<List<Product>>>()
    val productLiveData: LiveData<ResultData<List<Product>>>
        get() = _productLiveData

    fun getAllByCategory(categoryKeyWordsList: List<String>) {
        viewModelScope.launch {
            productRepository.loadAllByCategory(categoryKeyWordsList).collect {
                _productLiveData.postValue(it)
            }
        }
    }
}