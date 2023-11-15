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
package com.hms.referenceapp.hishopping.app.details.comments.domain

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.Comment
import com.hms.referenceapp.hishopping.data.model.LikeDislikeHolder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Qualifier

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CommentDomainModule {

    @get:Binds
    abstract val GetCommentsUseCase.commentUseCase: BaseResultFlowAsyncUseCase<List<Comment>, Int>

    @get:Binds
    abstract val UpsertCommentUseCase.upsert: BaseResultFlowAsyncUseCase<Unit, Comment>

    @get:Binds
    abstract val GetUserLikeDislikeListUseCase.userLikeDislikeUseCase: BaseRetrieveResultFlowAsyncUseCase<LikeDislikeHolder>

    @get:[Binds UserLikeListUseCase]
    abstract val SetUserLikeListUseCase.userLikeUseCase: BaseResultFlowAsyncUseCase<Unit, List<Int>>

    @get:[Binds UserDislikeListUseCase]
    abstract val SetUserDislikeListUseCase.userDislikeUseCase: BaseResultFlowAsyncUseCase<Unit, List<Int>>

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserLikeListUseCase

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserDislikeListUseCase
