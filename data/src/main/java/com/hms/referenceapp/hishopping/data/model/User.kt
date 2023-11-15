package com.hms.referenceapp.hishopping.data.model

import java.io.Serializable

data class User(
    val id:String,
    val fullName:String,
    val email:String,
    val photoUrl:String,
    val serviceType:String,
    val providerType:String
):Serializable