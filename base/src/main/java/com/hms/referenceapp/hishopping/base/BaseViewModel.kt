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
package com.hms.referenceapp.hishopping.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hms.lib.commonmobileservices.core.ResultData
import kotlinx.coroutines.flow.Flow


abstract class BaseViewModel : ViewModel() {

    val loadingErrorState = MutableLiveData<ResultData<Any>>()

    fun handleTask(task: ResultData<Any>, callback: () -> Unit = {}) {
        loadingErrorState.value = task
        callback.invoke()
    }

    fun <T> createResultLiveDataFromBaseRetrieveFlowUseCase(
        baseRetrieveResultFlowAsyncUseCase:
        BaseRetrieveResultFlowAsyncUseCase<T>
    ): LiveData<ResultData<T>> = liveData {
        emit(ResultData.Loading())
        baseRetrieveResultFlowAsyncUseCase.execute().collect {
            emit(it)
        }
    }

    fun <T, P> createResultLiveDataFromBaseFlowUseCase(
        baseResultFlowAsyncUseCase: BaseResultFlowAsyncUseCase<T, P>,
        parameter: P?
    ): LiveData<ResultData<T>> = liveData {
        emit(ResultData.Loading())
        baseResultFlowAsyncUseCase.execute(parameter).collect {
            emit(it)
        }
    }

    fun <T> createResultLiveDataFromFlow(
        interceptor: ((ResultData<T>) -> Boolean)? = null,
        flowGenerator: suspend () -> Flow<ResultData<T>>
    ): LiveData<ResultData<T>> = liveData {
        emit(ResultData.Loading())
        flowGenerator.invoke().collect { resultData ->
            interceptor?.let {
                if (!it.invoke(resultData)) emit(resultData)
            } ?: kotlin.run {
                emit(resultData)
            }
        }
    }

}