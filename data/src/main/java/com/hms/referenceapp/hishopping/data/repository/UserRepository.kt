package com.hms.referenceapp.hishopping.data.repository

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.clouddb.CloudDbDataSource
import com.hms.referenceapp.hishopping.data.mapper.UserMapper
import com.hms.referenceapp.hishopping.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface UserRepository {
    suspend fun upsertUser(user: User):Flow<ResultData<Unit>>
    suspend fun isUserExist(userId:String):Flow<ResultData<Boolean>>
    suspend fun getUser(userId: String):Flow<ResultData<User>>
}

class UserRepositoryImpl @Inject constructor(private val cloudDbDataSource:CloudDbDataSource) : UserRepository{
    override suspend fun upsertUser(user: User): Flow<ResultData<Unit>> = flow {
        emit(ResultData.Loading())
        try {
            cloudDbDataSource.upsertUser(
                UserMapper.fromEntity(user)
            )
            emit(ResultData.Success(Unit))
        }catch (e:Exception){
            emit(ResultData.Failed(error = e.message))
        }
    }

    override suspend fun isUserExist(userId: String): Flow<ResultData<Boolean>> = flow {
        emit(ResultData.Loading())
        try {
            val result = cloudDbDataSource.isUserExist(userId)
            emit(ResultData.Success(result))
        }catch (e:Exception){
            emit(ResultData.Failed(error = e.message))
        }
    }

    override suspend fun getUser(userId: String): Flow<ResultData<User>> = flow {
        emit(ResultData.Loading())
        try {
            val result = cloudDbDataSource.getUser(userId)
            emit(ResultData.Success(UserMapper.toEntity(result)))
        }catch (e:Exception){
            emit(ResultData.Failed(e.message))
        }
    }
}