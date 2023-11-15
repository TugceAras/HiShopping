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
package com.hms.referenceapp.hishopping.app.myorders.domain

import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncUseCase
import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.app.myorders.domain.model.OrderViewItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyOrdersUseCase @Inject constructor(private val myOrdersRepository: MyOrdersRepository) :
    BaseRetrieveResultFlowAsyncUseCase<@JvmSuppressWildcards List<OrderViewItem>> {

    override suspend fun execute(): Flow<ResultData<List<OrderViewItem>>> {
        return myOrdersRepository.getMyOrders()
    }
}