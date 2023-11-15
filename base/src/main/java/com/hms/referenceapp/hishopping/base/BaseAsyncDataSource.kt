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

import com.hms.lib.commonmobileservices.core.ResultData
import kotlinx.coroutines.flow.Flow


interface BaseAsyncDataSource<T, P> {
    suspend fun getResult(params: P? = null): T
}

interface BaseRetrieveAsyncDataSource<T> {
    suspend fun getResult(): T
}

interface BaseResultAsyncDataSource<T, P> {
    suspend fun getResult(params: P? = null): ResultData<T>
}

interface BaseRetrieveResultAsyncDataSource<T> {
    suspend fun getResult(): ResultData<T>
}

interface BaseResultFlowAsyncDataSource<T, P> {
    suspend fun getResult(params: P? = null): Flow<ResultData<T>>
}

interface BaseRetrieveResultFlowAsyncDataSource<T> {
    suspend fun getResult(): Flow<ResultData<T>>
}