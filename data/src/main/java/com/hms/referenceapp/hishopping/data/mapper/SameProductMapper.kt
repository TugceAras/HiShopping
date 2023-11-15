package com.hms.referenceapp.hishopping.data.mapper

import com.hms.referenceapp.hishopping.data.model.*
import com.hms.referenceapp.hishopping.data.clouddb.model.Product

object SameProductMapper {

    fun toSameProduct(
        model: Product,
    ): SameProduct {
        return SameProduct(
            productId = model.id,
            storeId = model.storeId
        )
    }
}