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
package com.hms.referenceapp.hishopping.base.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.hms.lib.commonmobileservices.core.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun <T> resultLiveData(task: suspend () -> ResultData<T>): LiveData<ResultData<T>> =
    liveData(Dispatchers.IO) {
        emit(ResultData.Loading())
        try {
            emit(task.invoke())
        } catch (e: Exception) {
            emit(ResultData.Failed(e.message))
        }
    }

fun <T> resultFlow(task: suspend () -> ResultData<T>): Flow<ResultData<T>> {
    return flow {
        emit(ResultData.Loading())
        try {
            emit(task.invoke())
        } catch (e: Exception) {
            emit(ResultData.Failed(e.message))
        }
    }
}

fun <T>Flow<ResultData<T>>.createResultLiveDataFromFlow() : LiveData<ResultData<T>> =
    liveData<ResultData<T>> {
        emit(ResultData.Loading())
        collect {
            emit(it)
        }
    }
