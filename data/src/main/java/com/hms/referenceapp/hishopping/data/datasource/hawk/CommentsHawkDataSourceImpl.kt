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
import com.hms.referenceapp.hishopping.data.datasource.CommentDataSource
import com.hms.referenceapp.hishopping.data.model.Comment
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class CommentsHawkDataSourceImpl @Inject constructor() : CommentDataSource {

    private fun getList(): MutableList<Comment> =
        (Hawk.get<MutableList<Comment>>(COMMENTS_SP_KEY) ?: mutableListOf())

    override suspend fun getAll(): Flow<ResultData<List<Comment>>> {
        return resultFlow {
            ResultData.Success(getList())
        }
    }

    override suspend fun loadAllByIds(commentIds: IntArray): Flow<ResultData<List<Comment>>> {
        return resultFlow {
            ResultData.Success(getList().filter { commentIds.contains(it.id) })
        }
    }

    override suspend fun loadById(commentId: Int): Flow<ResultData<Comment>> {
        return resultFlow {
            ResultData.Success(getList().firstOrNull {
                it.id == commentId
            })
        }
    }

    override suspend fun loadByProductId(productId: Int): Flow<ResultData<List<Comment>>> {
        return resultFlow {
            val commentsByProductIdList: ArrayList<Comment> = arrayListOf()
            getList().forEach {
                if (it.productId == productId) {
                    commentsByProductIdList.add(it)
                }
            }
            ResultData.Success(commentsByProductIdList)
        }
    }

    override suspend fun findByTitle(nameQuery: String): Flow<ResultData<Comment>> {
        return resultFlow {
            ResultData.Success(getList().firstOrNull {
                it.title.contains(nameQuery)
            })
        }
    }

    override suspend fun upsert(comments: Comment): Flow<ResultData<Unit>> {
        return resultFlow {
            var updateIndex = -1
            val list = getList()
            list.forEachIndexed { index, comment ->
                if (comments.id == comment.id) {
                    updateIndex = index
                }
            }
            if (updateIndex == -1) {
                comments.id = AtomicInteger(list.size).incrementAndGet()
                list.add(comments)
            } else {
                list[updateIndex] = comments
            }
            if (Hawk.put(COMMENTS_SP_KEY, list))
                ResultData.Success()
            else ResultData.Failed()
        }
    }

    override suspend fun delete(comment: Comment): Flow<ResultData<Unit>> {
        val list = getList()
        list.firstOrNull {
            it.id == comment.id
        }?.let {
            list.remove(it)
        }
        return resultFlow {
            if (Hawk.put(COMMENTS_SP_KEY, list))
                ResultData.Success()
            else ResultData.Failed()
        }
    }

    companion object {
        const val COMMENTS_SP_KEY = "COMMENTS_SP_KEY"
    }
}