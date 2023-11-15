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
package com.hms.referenceapp.hishopping.mycreditcards.di

import com.hms.referenceapp.hishopping.data.repository.CreditCardRepository
import com.hms.referenceapp.hishopping.mycreditcards.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object MyCreditCardsModule {

    @Provides
    fun provideDeleteCreditCardByCardNumberUseCase(provide: CreditCardRepository) = DeleteCreditCardByCardNumberUseCase(provide)

    @Provides
    fun provideDeleteCreditCardUseCase(provide: CreditCardRepository) = DeleteCreditCardUseCase(provide)

    @Provides
    fun provideGetAllCreditCardsUseCase(provide: CreditCardRepository) = GetAllCreditCardsUseCase(provide)

    @Provides
    fun provideGetCreditCardByCardNumberUseCase(provide: CreditCardRepository) = GetCreditCardByCardNumberUseCase(provide)

    @Provides
    fun provideUpsertCreditCardUseCase(provide: CreditCardRepository) = UpsertCreditCardUseCase(provide)

}
