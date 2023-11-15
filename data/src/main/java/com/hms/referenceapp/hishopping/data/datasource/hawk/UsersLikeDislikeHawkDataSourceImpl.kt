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
package com.hms.referenceapp.hishopping.data.datasource.hawk

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.base.extensions.resultFlow
import com.hms.referenceapp.hishopping.data.datasource.UsersLikeDislikeDataSource
import com.hms.referenceapp.hishopping.data.model.LikeDislikeHolder
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersLikeDislikeHawkDataSourceImpl @Inject constructor() : UsersLikeDislikeDataSource {

    private fun getLikeList(): MutableList<Int> =
        (Hawk.get<MutableList<Int>>(LIKE_SP_KEY) ?: mutableListOf())

    private fun getDislikeList(): MutableList<Int> =
        (Hawk.get<MutableList<Int>>(DISLIKE_SP_KEY) ?: mutableListOf())

    companion object {
        const val LIKE_SP_KEY = "LIKE_SP_KEY"
        const val DISLIKE_SP_KEY = "DISLIKE_SP_KEY"
    }


    override suspend fun getUsersLikeDislikeList(): Flow<ResultData<LikeDislikeHolder>> {
        return resultFlow {
            ResultData.Success(LikeDislikeHolder(getLikeList(), getDislikeList()))
        }
    }

    override suspend fun setUsersLikeList(
        likedCommentId: Int,
        likeCounter: Int
    ): Flow<ResultData<Unit>> {
        return resultFlow {
            var updateIndex = -1
            val likedCommentIdList = getLikeList()
            likedCommentIdList.forEachIndexed { index, comment ->
                if (likedCommentId == comment) {
                    updateIndex = index
                }
            }
            if (updateIndex == -1) {
                likedCommentIdList.add(likedCommentId)
            } else {
                likedCommentIdList.removeAt(updateIndex)
            }
            if (Hawk.put(LIKE_SP_KEY, likedCommentIdList))
                ResultData.Success()
            else ResultData.Failed()
        }
    }


    override suspend fun setUsersDislikeList(
        dislikedCommentId: Int,
        dislikeCounter: Int
    ): Flow<ResultData<Unit>> {
        return resultFlow {
            var updateIndex = -1
            val dislikeCommentIdList = getDislikeList()
            dislikeCommentIdList.forEachIndexed { index, comment ->
                if (dislikedCommentId == comment) {
                    updateIndex = index
                }
            }
            if (updateIndex == -1) {
                dislikeCommentIdList.add(dislikedCommentId)
            } else {
                dislikeCommentIdList.removeAt(updateIndex)
            }
            if (Hawk.put(DISLIKE_SP_KEY, dislikeCommentIdList))
                ResultData.Success()
            else ResultData.Failed()
        }
    }
}