package com.hms.referenceapp.hishopping.data.clouddb

import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.data.clouddb.model.*
import com.huawei.agconnect.cloud.database.CloudDBZone

interface CloudDbDataSource {

    suspend fun initialize(): ResultData<CloudDBZone>

    suspend fun getBrands(): List<Brand>
    suspend fun getSameProducts(brandId: String,title: String): List<Product>
    suspend fun getProductsByCategory(categoryId: String, limit: Int = 1000): List<Product>
    suspend fun getStoreById(storeId: String): Store
    suspend fun getCategories(): List<Category>
    suspend fun getBrandById(brandId: String): Brand
    suspend fun getProductImages(productId: String): List<ProductImages>
    suspend fun getFavoriteProducts(userId: String): List<Favorites>
    suspend fun deleteFromFavorites(favoriteProduct: Favorites)
    suspend fun upsertUser(user: User)
    suspend fun isUserExist(userId:String): Boolean
    suspend fun getUser(userId: String): User
    suspend fun getUserAddresses(userId: String): List<UserAddress>
    suspend fun addAddress(userAddress: UserAddress)
    suspend fun deleteAddress(userAddress: UserAddress)
    suspend fun getOrders(userId: String): List<Order>
    suspend fun upsertOrder(order: Order)
    suspend fun getStores(): List<Store>
}