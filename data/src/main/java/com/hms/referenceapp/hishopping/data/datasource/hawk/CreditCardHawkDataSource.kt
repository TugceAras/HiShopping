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
import com.hms.referenceapp.hishopping.data.datasource.CreditCardDataSource
import com.hms.referenceapp.hishopping.data.model.CreditCard
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreditCardHawkDataSource @Inject constructor() : CreditCardDataSource {
    override suspend fun getAll(): Flow<ResultData<List<CreditCard>>> {
        return resultFlow {
            ResultData.Success(Hawk.get<MutableList<CreditCard>>(CREDIT_CARD_LIST_SP_KEY))
        }
    }

    override suspend fun getCreditCardByCardNumber(cardNumber: String): Flow<ResultData<CreditCard?>> {
        return resultFlow {
            ResultData.Success(Hawk.get<MutableList<CreditCard>>(CREDIT_CARD_LIST_SP_KEY)
                .firstOrNull
                { it.cardNumber == cardNumber })
        }
    }

    override suspend fun upsert(creditCard: CreditCard): Flow<ResultData<CreditCard>> {
        return resultFlow {
            val list = Hawk.get<MutableList<CreditCard>>(CREDIT_CARD_LIST_SP_KEY) ?: mutableListOf()
            list.indexOfFirst {
                it.cardNumber ==
                        creditCard.cardNumber
            }.takeIf { it != -1 }?.let {
                //card is found: updating
                list[it] = creditCard
            } ?: run {
                list.add(0, creditCard)
            }
            val result = Hawk.put(CREDIT_CARD_LIST_SP_KEY, list)
            if (result)
                ResultData.Success(creditCard)
            else
                ResultData.Failed()
        }
    }

    override suspend fun delete(creditCard: CreditCard): Flow<ResultData<Any>> {
        val list = Hawk.get<MutableList<CreditCard>>(CREDIT_CARD_LIST_SP_KEY) ?: mutableListOf()
        list.firstOrNull {
            it.cardNumber == creditCard.cardNumber
        }?.let {
            list.remove(it)
        }
        Hawk.put(CREDIT_CARD_LIST_SP_KEY, list)
        return resultFlow {
            ResultData.Success(Any())
        }
    }

    override suspend fun delete(cardNumber: String): Flow<ResultData<Any>> {
        val list = Hawk.get<MutableList<CreditCard>>(CREDIT_CARD_LIST_SP_KEY) ?: mutableListOf()
        list.firstOrNull {
            it.cardNumber == cardNumber
        }?.let {
            list.remove(it)
        }
        Hawk.put(CREDIT_CARD_LIST_SP_KEY, list)
        return resultFlow {
            ResultData.Success(Any())
        }
    }

    companion object {
        const val CREDIT_CARD_LIST_SP_KEY = "CREDIT_CARD_LIST_SP_KEY"
    }
}