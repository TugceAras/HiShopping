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
package com.hms.referenceapp.hishopping.data.datasource.ram

import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.base.extensions.resultFlow
import com.hms.referenceapp.hishopping.data.datasource.CommentDataSource
import com.hms.referenceapp.hishopping.data.model.Comment
import kotlinx.coroutines.flow.Flow

class CommentRamDataSource(private val ramDb: RamDb) : CommentDataSource {

    override suspend fun getAll(): Flow<ResultData<List<Comment>>> {
        return resultFlow {
            ResultData.Success(ramDb.comments)
        }
    }

    override suspend fun loadAllByIds(CommentIds: IntArray): Flow<ResultData<List<Comment>>> {
        return resultFlow {
            ResultData.Success(ramDb.comments.filter { CommentIds.contains(it.id) })
        }
    }

    override suspend fun findByTitle(query: String): Flow<ResultData<Comment>> {
        return resultFlow {
            ResultData.Success(ramDb.comments.firstOrNull {
                it.title.contains(query)
            })
        }
    }

    override suspend fun upsert(comments: Comment): Flow<ResultData<Unit>> {
        return resultFlow {
            ramDb.comments.add(comments)
            ResultData.Success()
        }
    }

    override suspend fun delete(Comment: Comment): Flow<ResultData<Unit>> {
        return resultFlow {
            ramDb.comments.firstOrNull { it.id == Comment.id }?.let {
                ramDb.comments.remove(it)
            }
            ResultData.Success()
        }
    }

    override suspend fun loadById(CommentId: Int): Flow<ResultData<Comment>> {
        return resultFlow {
            ResultData.Success(ramDb.comments.firstOrNull {
                it.id == CommentId
            })
        }
    }

    override suspend fun loadByProductId(ProductId: Int): Flow<ResultData<List<Comment>>> {
        return resultFlow {
            val commentsByProductIdList: ArrayList<Comment> = arrayListOf()
            ramDb.comments.forEach {
                if (it.productId == ProductId) {
                    commentsByProductIdList.add(it)
                }
            }
            ResultData.Success(commentsByProductIdList)
        }
    }
}