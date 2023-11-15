package com.hms.referenceapp.hishopping.shoplocations.presentation.model

data class StoreItem(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val addressLine: String?,
    val photoUrl: String?,
    val distance: Float,
    val distanceText: String
)
