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
package com.hms.referenceapp.hishopping.couriertrack.domain

import com.hms.referenceapp.hishopping.base.BaseResultFlowAsyncUseCase
import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.couriertrack.domain.model.CourierPosition
import com.hms.referenceapp.hishopping.couriertrack.domain.model.CourierPositionRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CourierPositionUseCase @Inject constructor(private val courierPositionRepository: CourierPositionRepository) :
    BaseResultFlowAsyncUseCase<CourierPosition, CourierPositionRequest> {
    override suspend fun execute(params: CourierPositionRequest?): Flow<ResultData<CourierPosition>> {
        return courierPositionRepository.getCourierPosition(params!!)
    }
}