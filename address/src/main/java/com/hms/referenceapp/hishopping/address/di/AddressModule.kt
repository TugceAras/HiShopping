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
package com.hms.referenceapp.hishopping.address.di

import com.hms.referenceapp.hishopping.address.domain.usecase.*
import com.hms.referenceapp.hishopping.data.repository.address.AddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object AddressModule {

    @Provides
    fun provideDeleteAddressUseCase(repository: AddressRepository) =
        DeleteAddressUseCase(repository)

    @Provides
    fun provideGetAddressByTitleUseCase(repository: AddressRepository) =
        GetAddressByTitleUseCase(repository)

}
