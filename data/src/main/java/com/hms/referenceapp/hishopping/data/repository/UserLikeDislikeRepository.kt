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
package com.hms.referenceapp.hishopping.data.repository

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.datasource.UsersLikeDislikeDataSource
import com.hms.referenceapp.hishopping.data.model.LikeDislikeHolder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserLikeDislikeRepository {
    suspend fun getUsersLikeDislikeList(): Flow<ResultData<LikeDislikeHolder>>
    suspend fun setUsersLikeList(likedCommentId: Int, likeCounter: Int): Flow<ResultData<Unit>>
    suspend fun setUsersDislikeList(
        dislikedCommentId: Int,
        dislikeCounter: Int
    ): Flow<ResultData<Unit>>
}

class UserLikeDislikeRepositoryImpl @Inject constructor(private val dataSource: UsersLikeDislikeDataSource) :
    UserLikeDislikeRepository {

    override suspend fun setUsersLikeList(
        likedCommentId: Int,
        likeCounter: Int
    ): Flow<ResultData<Unit>> {
        return dataSource.setUsersLikeList(likedCommentId, likeCounter)
    }

    override suspend fun getUsersLikeDislikeList(): Flow<ResultData<LikeDislikeHolder>> {
        return dataSource.getUsersLikeDislikeList()
    }

    override suspend fun setUsersDislikeList(
        dislikedCommentId: Int,
        dislikeCounter: Int
    ): Flow<ResultData<Unit>> {
        return dataSource.setUsersDislikeList(dislikedCommentId, dislikeCounter)
    }
}