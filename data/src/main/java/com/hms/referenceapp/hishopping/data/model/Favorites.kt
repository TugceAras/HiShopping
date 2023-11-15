package com.hms.referenceapp.hishopping.data.model

data class Favorites(
    val id: String,
    val userId: String,
    val productId: String,
    val productPhotoUrl: String,
    val productTitle: String,
    val productPrice: String,
    val productOldPrice: String
)