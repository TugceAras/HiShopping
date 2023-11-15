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
package com.hms.referenceapp.hishopping.data.datasource

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentDataSource {
    suspend fun getAll(): Flow<ResultData<List<Comment>>>
    suspend fun loadAllByIds(commentIds: IntArray): Flow<ResultData<List<Comment>>>
    suspend fun loadById(commentId: Int): Flow<ResultData<Comment>>
    suspend fun loadByProductId(productId: Int): Flow<ResultData<List<Comment>>>
    suspend fun findByTitle(nameQuery: String): Flow<ResultData<Comment>>
    suspend fun upsert(comment: Comment): Flow<ResultData<Unit>>
    suspend fun delete(comment: Comment): Flow<ResultData<Unit>>
}