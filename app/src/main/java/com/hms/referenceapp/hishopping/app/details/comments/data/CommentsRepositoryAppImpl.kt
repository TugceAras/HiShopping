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
package com.hms.referenceapp.hishopping.app.details.comments.data

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncDataSource
import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.app.details.comments.domain.CommentsRepositoryApp
import com.hms.referenceapp.hishopping.data.model.Comment
import com.hms.referenceapp.hishopping.data.model.LikeDislikeHolder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentsRepositoryAppImpl @Inject constructor(
    private val getCommentsDataSource:
    BaseResultFlowAsyncDataSource<@JvmSuppressWildcards List<Comment>, Int>,
    private val upsertCommentsDataSource:
    BaseResultFlowAsyncDataSource<Unit, @JvmSuppressWildcards Comment>,
    private val getUserLikeDislikeDataSource:
    BaseResultFlowAsyncDataSource<@JvmSuppressWildcards LikeDislikeHolder, @JvmSuppressWildcards Unit>,
    @UserLikeListDataSource private val setUserLikeListDataSource:
    BaseResultFlowAsyncDataSource<Unit, @JvmSuppressWildcards List<Int>>,
    @UserDislikeListDataSource private val setUserDislikeListDataSource:
    BaseResultFlowAsyncDataSource<Unit, @JvmSuppressWildcards List<Int>>
) :
    CommentsRepositoryApp {

    override suspend fun getComments(query: Int): Flow<ResultData<List<Comment>>> {
        return getCommentsDataSource.getResult(query)
    }

    override suspend fun upsertComments(query: Comment): Flow<ResultData<Unit>> {
        return upsertCommentsDataSource.getResult(query)
    }

    override suspend fun getUsersLikeDislikeList(): Flow<ResultData<LikeDislikeHolder>> {
        return getUserLikeDislikeDataSource.getResult()
    }

    override suspend fun setUsersLikeList(
        likedCommentId: Int,
        likeCounter: Int
    ): Flow<ResultData<Unit>> {
        return setUserLikeListDataSource.getResult(arrayListOf(likedCommentId, likeCounter))
    }

    override suspend fun setUsersDislikeList(
        dislikedCommentId: Int,
        dislikeCounter: Int
    ): Flow<ResultData<Unit>> {
        return setUserDislikeListDataSource.getResult(
            arrayListOf(
                dislikedCommentId,
                dislikeCounter
            )
        )
    }
}