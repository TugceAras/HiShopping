package com.hms.referenceapp.hishopping.shoplocations.data

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseRetrieveResultFlowAsyncDataSource
import com.hms.referenceapp.hishopping.data.model.StoreEntity
import com.hms.referenceapp.hishopping.data.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoresDataSource @Inject constructor(private val storeRepository: StoreRepository) :
        BaseRetrieveResultFlowAsyncDataSource<List<@JvmSuppressWildcards StoreEntity>> {
    override suspend fun getResult(): Flow<ResultData<List<StoreEntity>>> {
        return storeRepository.getStores()
    }
}