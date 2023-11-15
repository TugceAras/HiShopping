package com.hms.referenceapp.hishopping.trywithar.model

import android.os.Parcelable
import com.hms.lib.commonmobileservices.scene.common.CommonData
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class RenderParams(
    val id: Int,
    val productId: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val params: @RawValue CommonData
) : Parcelable
