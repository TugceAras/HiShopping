package com.hms.referenceapp.hishopping.shoplocations.presentation

import com.hms.referenceapp.hishopping.shoplocations.presentation.model.StoreItem

data class ShopLocationViewState(
    val storeItemList: List<StoreItem> = emptyList(),
    val loading: Boolean=false,
    val error: String?=null
)
