package com.hms.referenceapp.hishopping.shoplocations.domain.usecase

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncUseCase
import com.hms.referenceapp.hishopping.data.model.StoreEntity
import com.hms.referenceapp.hishopping.shoplocations.domain.repository.ShopLocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(private val shopLocationRepository: ShopLocationRepository) :
        BaseRetrieveResultFlowAsyncUseCase<List<@JvmSuppressWildcards StoreEntity>> {
    override suspend fun execute(): Flow<ResultData<List<StoreEntity>>> {
        return shopLocationRepository.getStores()
    }
}