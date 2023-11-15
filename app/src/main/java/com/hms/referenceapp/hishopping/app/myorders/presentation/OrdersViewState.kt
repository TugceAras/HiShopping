package com.hms.referenceapp.hishopping.app.myorders.presentation

import com.hms.referenceapp.hishopping.data.model.OrderEntity

data class OrdersViewState(
    val orderList:List<OrderEntity> = emptyList(),
    val loading:Boolean = false,
    val error:String?=null
)
