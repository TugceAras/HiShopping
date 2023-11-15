package com.hms.referenceapp.hishopping.couriertrack.presentation

data class CourierTrackViewState(
    val orderUpdated:Boolean = false,
    val loading:Boolean = false,
    val errorMessage: String = ""
)