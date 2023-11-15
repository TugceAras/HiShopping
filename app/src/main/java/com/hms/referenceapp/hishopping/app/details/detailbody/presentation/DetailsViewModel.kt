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
package com.hms.referenceapp.hishopping.app.details.detailbody.presentation

import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.core.handleSuccessSuspend
import com.hms.referenceapp.hishopping.app.details.comments.domain.UserDislikeListUseCase
import com.hms.referenceapp.hishopping.app.details.comments.domain.UserLikeListUseCase
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getProductDetailUseCase:
    BaseResultFlowAsyncUseCase<@JvmSuppressWildcards Product, Int>,
    private val basketUseCase: BaseResultFlowAsyncUseCase<CartItem, CartItem>,
    private val upsertCommentUseCase: BaseResultFlowAsyncUseCase<Unit, Comment>,
    private val getCommentsUseCase: BaseResultFlowAsyncUseCase<@JvmSuppressWildcards List<Comment>, Int>,
    private val getUserLikeDislikeUseCase: BaseRetrieveResultFlowAsyncUseCase<@JvmSuppressWildcards LikeDislikeHolder>,
    @UserLikeListUseCase private val setUserLikeListUseCase: BaseResultFlowAsyncUseCase<Unit, List<Int>>,
    @UserDislikeListUseCase private val setUserDislikeListUseCase: BaseResultFlowAsyncUseCase<Unit, List<Int>>
) : BaseViewModel() {

    private val _productDetailsLiveData = MutableLiveData<ResultData<Product>>()
    private val _productCommentsLiveData = MutableLiveData<ResultData<List<Comment>>>()

    val productDetailsLiveData: LiveData<ResultData<Product>>
        get() = _productDetailsLiveData

    val productCommentsLiveData: LiveData<ResultData<List<Comment>>>
        get() = _productCommentsLiveData

    var lastCommentsSize = 0

    fun loadProductDetails(productId: Int) {
        _productDetailsLiveData.value = ResultData.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            getProductDetailUseCase.execute(productId).collect {
                _productDetailsLiveData.postValue(it)
            }
        }
    }

    fun loadCommentsById(productId: Int) {
        _productCommentsLiveData.value = ResultData.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            getCommentsUseCase.execute(productId).collect {
                val res = it.handleSuccessSuspend {
                    lastCommentsSize = it.data?.size ?: 0
                    it.data
                }
                _productCommentsLiveData.postValue(res)
            }
        }
    }

    fun upsertComments(comment: Comment) =
        createResultLiveDataFromBaseFlowUseCase(upsertCommentUseCase, comment)

    fun addToBasket(cartItem: CartItem) =
        createResultLiveDataFromBaseFlowUseCase(basketUseCase, cartItem)

    fun getUsersLikeList() =
        createResultLiveDataFromBaseRetrieveFlowUseCase(getUserLikeDislikeUseCase)

    fun setUsersLikeList(likedCommentId: Int, likedCounter: Int) =
        createResultLiveDataFromBaseFlowUseCase(
            setUserLikeListUseCase,
            arrayListOf(likedCommentId, likedCounter)
        )

    fun setUsersDislikeList(dislikedCommentId: Int, dislikedCounter: Int) =
        createResultLiveDataFromBaseFlowUseCase(
            setUserDislikeListUseCase,
            arrayListOf(dislikedCommentId, dislikedCounter)
        )


}