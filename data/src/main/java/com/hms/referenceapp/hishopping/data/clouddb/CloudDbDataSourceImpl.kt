package com.hms.referenceapp.hishopping.data.clouddb

import android.content.Context
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.core.CloudDbNotInitializedException
import com.hms.referenceapp.hishopping.data.clouddb.model.*
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by Mustafa Kemal Özdemir on 3/18/2022.
 */
class CloudDbDataSourceImpl @Inject constructor(@ApplicationContext private val context: Context) :
    CloudDbDataSource {
    private lateinit var mCloudDB: AGConnectCloudDB
    private var handler: CompletableDeferred<ResultData<CloudDBZone>>? = null
    private var cloudDBZone: CloudDBZone? = null

    override
    suspend fun initialize(): ResultData<CloudDBZone> {
        handler = CompletableDeferred<ResultData<CloudDBZone>>()
        AGConnectCloudDB.initialize(context)
        initializeCloudDB(context)
        initializeZone()
        handler?.let {
            return it.await()
        } ?: run {
            return ResultData.Failed()
        }
    }

    private fun initializeCloudDB(context: Context) {
        Timber.i("Initializing")
        val instance = AGConnectInstance.buildInstance(
            AGConnectOptionsBuilder().setRoutePolicy(
                AGCRoutePolicy.GERMANY
            ).build(context)
        )
        mCloudDB = AGConnectCloudDB.getInstance(instance, AGConnectAuth.getInstance())
        mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())
    }

    private fun initializeZone() {
        val mConfig = CloudDBZoneConfig(
            "HiShoppingDbZone",
            CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC
        )
        mConfig.persistenceEnabled = true
        val task = mCloudDB.openCloudDBZone2(mConfig, true)
        task.addOnSuccessListener {
            Timber.i("Open cloudDBZone success")
            cloudDBZone = it
            handler?.complete(ResultData.Success(it))
            // Add subscription after opening cloudDBZone success
        }.addOnFailureListener {
            handler?.complete(ResultData.Failed())
            Timber.w("Open cloudDBZone failed for " + it.message)
        }
    }

    override suspend fun getBrands(): List<Brand> {
        if (cloudDBZone == null) {
            throw Exception()
        }
        val result = CompletableDeferred<List<Brand>>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(Brand::class.java),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = mutableListOf<Brand>()
                    for (i in 0 until it.result.snapshotObjects.size()) {
                        data.add(it.result.snapshotObjects[i])
                    }
                    result.complete(data.toList())
                } else {
                    throw Exception("Operation failed")
                }
            }
        }
        return result.await()
    }

    override suspend fun getBrandById(brandId: String): Brand {
        val result = CompletableDeferred<Brand>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(Brand::class.java).equalTo("id", brandId),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener{
                if(it.isSuccessful) {
                    if(it.result.snapshotObjects.hasNext()) {
                        result.complete(it.result.snapshotObjects.next())
                    }
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getCategories(): List<Category> {
        val result = CompletableDeferred< List<Category>>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(Category::class.java),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener{
                if(it.isSuccessful) {
                    val cursor = it.result.snapshotObjects
                    val categories = mutableListOf<Category>()
                    while(cursor.hasNext()) {
                        categories.add(cursor.next())
                    }
                    result.complete(categories)
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getStoreById(storeId: String): Store {
        val result = CompletableDeferred<Store>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(Store::class.java).equalTo("id", storeId),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener{
                if(it.isSuccessful) {
                    if(it.result.snapshotObjects.hasNext()) {
                        result.complete(it.result.snapshotObjects.next())
                    }
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getSameProducts(brandId: String, title: String): List<Product> {
        val result = CompletableDeferred<List<Product>>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(Product::class.java)
                    .equalTo("brandId", brandId)
                    .equalTo("title",title),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener{
                if(it.isSuccessful) {
                    val cursor = it.result.snapshotObjects
                    val sameProducts = mutableListOf<Product>()
                    while(cursor.hasNext()) { sameProducts.add(cursor.next()) }
                    result.complete(sameProducts)
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getProductsByCategory(categoryId: String, limit: Int): List<Product> {
        val result = CompletableDeferred< List<Product>>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(Product::class.java).equalTo("categoryId", categoryId).limit(limit),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener{
                if(it.isSuccessful) {
                    val cursor = it.result.snapshotObjects
                    val products = mutableListOf<Product>()
                    while(cursor.hasNext()) {
                        products.add(cursor.next())
                    }
                    result.complete(products)
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getProductImages(productId: String): List<ProductImages> {
        val result = CompletableDeferred< List<ProductImages>>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(ProductImages::class.java).equalTo("productId", productId),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener{
                if(it.isSuccessful) {
                    val cursor = it.result.snapshotObjects
                    val images = mutableListOf<ProductImages>()
                    while(cursor.hasNext()) {
                        images.add(cursor.next())
                    }
                    result.complete(images)
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getFavoriteProducts(userId: String): List<Favorites> {
        val result = CompletableDeferred<List<Favorites>>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(Favorites::class.java).equalTo("userId",userId),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener {
                if (it.isSuccessful){
                    val cursor = it.result.snapshotObjects
                    val favorites = mutableListOf<Favorites>()
                    while (cursor.hasNext()){
                        favorites.add(cursor.next())
                    }
                    result.complete(favorites)
                }else throw it.exception
            }
        }?: run { throw CloudDbNotInitializedException() }
        return result.await()
    }

    override suspend fun deleteFromFavorites(favoriteProduct: Favorites) {
        val result = CompletableDeferred<Unit>()
        cloudDBZone?.let { dbZone ->
           dbZone.executeDelete(
               favoriteProduct
           ).addOnCompleteListener {
               if (it.isSuccessful)
                   result.complete(Unit)
               else throw it.exception
           }
        }?: run { throw CloudDbNotInitializedException() }
        return result.await()
    }

    override suspend fun upsertUser(user: User) {
        val result = CompletableDeferred<Unit>()
        cloudDBZone?.let { dDBZone ->
            dDBZone.executeUpsert(user).addOnCompleteListener {
                if (it.isSuccessful)
                    result.complete(Unit)
                else throw it.exception
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun isUserExist(userId: String): Boolean {
        val result = CompletableDeferred<Boolean>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(User::class.java).equalTo("id", userId),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener {
                if(it.isSuccessful) {
                    val cursor = it.result.snapshotObjects
                    val users = mutableListOf<User>()
                    while(cursor.hasNext()) {
                        users.add(cursor.next())
                    }
                    if (users.size>0) result.complete(true)
                    else result.complete(false)
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getUser(userId: String): User {
        val result = CompletableDeferred<User>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(User::class.java).equalTo("id", userId),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener {
                if(it.isSuccessful) {
                    val cursor = it.result.snapshotObjects
                    if (cursor.hasNext()){
                        result.complete(cursor.next())
                    }
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getUserAddresses(userId: String): List<UserAddress> {
        val result = CompletableDeferred< List<UserAddress>>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(UserAddress::class.java).equalTo("userId", userId),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener{
                if(it.isSuccessful) {
                    val cursor = it.result.snapshotObjects
                    val addresses = mutableListOf<UserAddress>()
                    while(cursor.hasNext()) {
                        addresses.add(cursor.next())
                    }
                    result.complete(addresses)
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun addAddress(userAddress: UserAddress) {
        val result = CompletableDeferred<Unit>()
        cloudDBZone?.let { dDBZone ->
            dDBZone.executeUpsert(userAddress).addOnCompleteListener {
                if (it.isSuccessful)
                    result.complete(Unit)
                else throw it.exception
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun deleteAddress(userAddress: UserAddress) {
        val result = CompletableDeferred<Unit>()
        cloudDBZone?.let { dDBZone ->
            dDBZone.executeDelete(userAddress).addOnCompleteListener {
                if (it.isSuccessful)
                    result.complete(Unit)
                else throw it.exception
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getOrders(userId: String): List<Order> {
        val result = CompletableDeferred< List<Order>>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(Order::class.java).equalTo("userId", userId),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener{
                if(it.isSuccessful) {
                    val cursor = it.result.snapshotObjects
                    val orders = mutableListOf<Order>()
                    while(cursor.hasNext()) {
                        orders.add(cursor.next())
                    }
                    result.complete(orders)
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun upsertOrder(order: Order) {
        val result = CompletableDeferred<Unit>()
        cloudDBZone?.let { dDBZone ->
            dDBZone.executeUpsert(order).addOnCompleteListener {
                if (it.isSuccessful)
                    result.complete(Unit)
                else throw it.exception
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }

    override suspend fun getStores(): List<Store> {
        val result = CompletableDeferred<List<Store>>()
        cloudDBZone?.let { dbZone ->
            dbZone.executeQuery(
                CloudDBZoneQuery.where(Store::class.java),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            ).addOnCompleteListener{
                if(it.isSuccessful) {
                    val cursor = it.result.snapshotObjects
                    val stores = mutableListOf<Store>()
                    while(cursor.hasNext()) {
                        stores.add(cursor.next())
                    }
                    result.complete(stores)
                }else {
                    throw it.exception
                }
            }
        }?: run {
            throw CloudDbNotInitializedException()
        }
        return result.await()
    }
}