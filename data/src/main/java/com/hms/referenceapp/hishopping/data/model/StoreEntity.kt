package com.hms.referenceapp.hishopping.data.model

data class StoreEntity(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val addressLine: String?,
    val photoUrl: String?,
)
