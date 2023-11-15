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
package com.hms.referenceapp.hishopping.trywithar


import com.hms.referenceapp.hishopping.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TryWithArViewModel @Inject constructor() : BaseViewModel() {
/*    var repository: ProductRepository? = null
    private val _productDetailsLiveData = MutableLiveData<Product>()

    val productDetailsLiveData: LiveData<Product>
        get() = _productDetailsLiveData

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            repository?.loadById(productId)?.collect {
                when (it) {
                    is ResultData.Success -> {
                        if (null == it.data) loadProduct(productId)
                        else _productDetailsLiveData.value = it.data
                    }
                }
            }
        }
    }

 */
}