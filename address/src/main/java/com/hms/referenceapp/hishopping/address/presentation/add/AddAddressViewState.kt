package com.hms.referenceapp.hishopping.address.presentation.add

import com.hms.referenceapp.hishopping.core.UIText

data class AddAddressViewState(
    val success:Boolean = false,
    val loading:Boolean = false,
    val error:UIText? = null
)
