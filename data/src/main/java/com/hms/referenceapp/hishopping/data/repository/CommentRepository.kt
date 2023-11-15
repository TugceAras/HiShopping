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
import com.hms.referenceapp.hishopping.data.datasource.CommentDataSource
import com.hms.referenceapp.hishopping.data.model.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CommentRepository {
    suspend fun getAll(): Flow<ResultData<List<Comment>>>
    suspend fun loadAllByIds(CommentIds: IntArray): Flow<ResultData<List<Comment>>>
    suspend fun loadById(CommentId: Int): Flow<ResultData<Comment>>
    suspend fun loadByProductId(ProductId: Int): Flow<ResultData<List<Comment>>>
    suspend fun findByName(nameQuery: String): Flow<ResultData<Comment>>
    suspend fun upsert(Comments: Comment): Flow<ResultData<Unit>>
    suspend fun delete(Comment: Comment): Flow<ResultData<Unit>>
}

class CommentRepositoryImpl @Inject constructor(private val dataSource: CommentDataSource) : CommentRepository {

    override suspend fun getAll(): Flow<ResultData<List<Comment>>> {
        return dataSource.getAll()
    }

    override suspend fun loadAllByIds(CommentIds: IntArray): Flow<ResultData<List<Comment>>> {
        return dataSource.loadAllByIds(CommentIds)
    }

    override suspend fun loadById(CommentId: Int): Flow<ResultData<Comment>> {
        return dataSource.loadById(CommentId)
    }

    override suspend fun loadByProductId(ProductId: Int): Flow<ResultData<List<Comment>>> {
        return dataSource.loadByProductId(ProductId)
    }

    override suspend fun findByName(nameQuery: String): Flow<ResultData<Comment>> {
        return dataSource.findByTitle(nameQuery)
    }


    override suspend fun upsert(Comments: Comment): Flow<ResultData<Unit>> {
        return dataSource.upsert(Comments)
    }

    override suspend fun delete(Comment: Comment): Flow<ResultData<Unit>> {
        return dataSource.delete(Comment)
    }


}