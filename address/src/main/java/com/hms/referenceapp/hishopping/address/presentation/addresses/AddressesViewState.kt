package com.hms.referenceapp.hishopping.address.presentation.addresses

import com.hms.referenceapp.hishopping.core.UIText
import com.hms.referenceapp.hishopping.data.model.UserAddress

data class AddressesViewState(
    val addressList:List<UserAddress>? = null,
    val loading:Boolean = false,
    val error:UIText? = null
)
