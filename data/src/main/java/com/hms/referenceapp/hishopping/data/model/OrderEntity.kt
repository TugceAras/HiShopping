package com.hms.referenceapp.hishopping.data.model

import java.io.Serializable

data class OrderEntity(
    var id: String,
    val productId: String,
    var userId: String,
    var userLat: Double,
    var userLng: Double,
    val addressId: String,
    val address: String,
    var orderStatus: Order.OrderStatus,
    var date: Long? = null,
    var storeId: String,
    var storeLat: Double,
    var storeLng: Double,
    var productPhotoUrl:String,
    var productTitle:String,
): Serializable
