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
package com.hms.referenceapp.hishopping.mycreditcards.presentation

import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.CreditCard
import com.hms.referenceapp.hishopping.mycreditcards.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyCreditCardsViewModel @Inject constructor(
    private val deleteCreditCardUseCase: DeleteCreditCardUseCase,
    private val deleteCreditCardByCardNumberUseCase: DeleteCreditCardByCardNumberUseCase,
    private val getAllCreditCardsUseCase: GetAllCreditCardsUseCase,
    private val getCreditCardByCardNumberUseCase: GetCreditCardByCardNumberUseCase,
    private val upsertCreditCardUseCase: UpsertCreditCardUseCase

) : BaseViewModel() {

    fun getAllCreditCards() =
        createResultLiveDataFromBaseRetrieveFlowUseCase(getAllCreditCardsUseCase)

    fun deleteCreditCard(creditCard: CreditCard) =
        createResultLiveDataFromBaseFlowUseCase(deleteCreditCardUseCase, creditCard)

    fun deleteCreditCardByCardNumber(creditCardNumber: String) =
        createResultLiveDataFromBaseFlowUseCase(
            deleteCreditCardByCardNumberUseCase,
            creditCardNumber
        )

    fun getCreditCardByCardNumber(creditCardNumber: String) =
        createResultLiveDataFromBaseFlowUseCase(getCreditCardByCardNumberUseCase, creditCardNumber)

    fun upsertCreditCard(creditCard: CreditCard) =
        createResultLiveDataFromBaseFlowUseCase(upsertCreditCardUseCase, creditCard)

}

