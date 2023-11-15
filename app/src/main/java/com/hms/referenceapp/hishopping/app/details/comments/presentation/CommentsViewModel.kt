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
package com.hms.referenceapp.hishopping.app.details.comments.presentation

import javax.inject.Inject
import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.app.details.comments.domain.UserDislikeListUseCase
import com.hms.referenceapp.hishopping.app.details.comments.domain.UserLikeListUseCase
import com.hms.referenceapp.hishopping.data.model.Comment
import com.hms.referenceapp.hishopping.data.model.LikeDislikeHolder
import com.hms.referenceapp.hishopping.data.model.CartItem
import com.hms.referenceapp.hishopping.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getCommentUseCase:
    BaseResultFlowAsyncUseCase<List<Comment>, Int>,
    private val upsertCommentUseCase:
    BaseResultFlowAsyncUseCase<Unit, Comment>,
    private val getUserLikeDislikeUseCase:
    BaseRetrieveResultFlowAsyncUseCase<LikeDislikeHolder>,
    @UserLikeListUseCase private val setUserLikeListUseCase:
    BaseResultFlowAsyncUseCase<Unit, List<Int>>,
    @UserDislikeListUseCase private val setUserDislikeListUseCase:
    BaseResultFlowAsyncUseCase<Unit, List<Int>>,
    private val getProductDetailUseCase:
    BaseResultFlowAsyncUseCase<Product, Int>,
    private val addToBasketUseCase: BaseResultFlowAsyncUseCase<CartItem, CartItem>,
) :
    BaseViewModel() {

    fun getCommentsById(productId: Int) =
        createResultLiveDataFromBaseFlowUseCase(getCommentUseCase, productId)

    fun upsertComments(comment: Comment) =
        createResultLiveDataFromBaseFlowUseCase(upsertCommentUseCase, comment)

    fun getUsersLikeList() =
        createResultLiveDataFromBaseRetrieveFlowUseCase(getUserLikeDislikeUseCase)

    fun setUsersLikeList(likedCommentId: Int, likedCounter: Int) =
        createResultLiveDataFromBaseFlowUseCase(
            setUserLikeListUseCase,
            listOf(likedCommentId, likedCounter)
        )

    fun setUsersDislikeList(dislikedCommentId: Int, dislikedCounter: Int) =
        createResultLiveDataFromBaseFlowUseCase(
            setUserDislikeListUseCase,
            listOf(dislikedCommentId, dislikedCounter)
        )

    fun getProductDetail(productId: Int) =
        createResultLiveDataFromBaseFlowUseCase(getProductDetailUseCase, productId)

    fun addProductToBasket(basket: CartItem) =
        createResultLiveDataFromBaseFlowUseCase(addToBasketUseCase, basket)

}


