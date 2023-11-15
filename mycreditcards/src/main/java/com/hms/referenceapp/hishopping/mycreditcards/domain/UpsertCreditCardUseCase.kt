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
package com.hms.referenceapp.hishopping.mycreditcards.domain

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.data.model.CreditCard
import com.hms.referenceapp.hishopping.data.repository.CreditCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpsertCreditCardUseCase @Inject constructor(private val creditCardRepository: CreditCardRepository) :
    BaseResultFlowAsyncUseCase<CreditCard, CreditCard> {
    override suspend fun execute(params: CreditCard?): Flow<ResultData<CreditCard>> =
        creditCardRepository.upsert(params!!)

}