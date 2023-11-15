package com.hms.referenceapp.hishopping.app.profile

import com.hms.referenceapp.hishopping.data.model.User

data class ProfileViewState(
    val user:User,
    val loading:Boolean = false
)
