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
import com.hms.referenceapp.hishopping.app.details.comments.domain.CommentsRepositoryApp
import com.hms.referenceapp.hishopping.data.model.Comment
import com.hms.referenceapp.hishopping.data.model.LikeDislikeHolder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Qualifier

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CommentsDataClass {

    @get:Binds
    abstract val UpsertCommentDataSource.upsertCommentsDataSource: BaseResultFlowAsyncDataSource<Unit, Comment>

    @get:Binds
    abstract val GetCommentsDataSource.getCommentsDataSource: BaseResultFlowAsyncDataSource<List<Comment>, Int>

    @get:Binds
    abstract val GetUserLikeDislikeDataSource.getUserLikeDislikeDataSource: BaseResultFlowAsyncDataSource<LikeDislikeHolder, Unit>

    @get:[Binds UserLikeListDataSource]
    abstract val SetUserLikeListDataSource.setUserLikeListDataSource: BaseResultFlowAsyncDataSource<Unit, List<Int>>

    @get:[Binds UserDislikeListDataSource]
    abstract val SetUserDislikeListDataSource.setUserDislikeListDataSource: BaseResultFlowAsyncDataSource<Unit, List<Int>>

    @get:Binds
    abstract val CommentsRepositoryAppImpl.repo: CommentsRepositoryApp

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserLikeListDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserDislikeListDataSource
