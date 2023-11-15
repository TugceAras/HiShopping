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
package com.hms.referenceapp.hishopping.couriertrack.di

import com.hms.referenceapp.hishopping.couriertrack.data.*
import com.hms.referenceapp.hishopping.couriertrack.domain.CourierPositionRepository
import com.hms.referenceapp.hishopping.couriertrack.domain.CourierPositionUseCase
import com.hms.referenceapp.hishopping.couriertrack.domain.UpdateOrderUseCase
import com.hms.referenceapp.hishopping.data.repository.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object  CourierTrackModule {

    @Provides
    fun provideCourierPositionUseCase(provide: CourierPositionRepository) = CourierPositionUseCase(provide)

    @Provides
    fun provideUpdateOrderUseCase(provide: OrderRepository) = UpdateOrderUseCase(provide)

    @Provides
    fun provideCourierCurrentPositionDataSource() = CourierCurrentPositionDataSource()

    @Provides
    fun provideCourierDataLocalSource(provide: CourierPositionCalculator) = CourierDataLocalSource(provide)

    @Provides
    fun provideCourierPositionCalculator(provide: CourierCurrentPositionDataSource) = CourierPositionCalculator(provide)

    @Provides
    fun provideCourierPositionDataSource(provide: CourierDataLocalSource) = CourierPositionDataSource(provide)

    @Provides
    fun provideCourierPositionRepository(provide: CourierPositionRepositoryImpl): CourierPositionRepository = provide

}