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
package com.hms.referenceapp.hishopping.app.details.addcomment.presentation

import javax.inject.Inject
import androidx.lifecycle.liveData
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.model.Comment
import com.hms.referenceapp.hishopping.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect

@HiltViewModel
class AddCommentViewModel @Inject constructor(
    private val getProductDetailUseCase:
    BaseResultFlowAsyncUseCase<@JvmSuppressWildcards Product, Int>,
    private val upsertCommentUseCase:
    BaseResultFlowAsyncUseCase<Unit, @JvmSuppressWildcards Comment>
) : BaseViewModel() {

    fun loadProductDetails(productId: Int) = liveData {
        emit(ResultData.Loading())
        getProductDetailUseCase.execute(productId).collect {
            emit(it)
        }
    }

    fun insertComments(comment: Comment?) = liveData {
        emit(ResultData.Loading())
        upsertCommentUseCase.execute(comment).collect {
            emit(it)
        }
    }
}