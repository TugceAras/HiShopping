package com.hms.referenceapp.hishopping.data.mapper

import com.hms.referenceapp.hishopping.core.EntityMapper
import com.hms.referenceapp.hishopping.data.clouddb.model.ProductImages
import com.hms.referenceapp.hishopping.data.model.ProductImageEntity

object ProductImageMapper: EntityMapper<ProductImageEntity, ProductImages> {

    override fun toEntity(model: ProductImages): ProductImageEntity {
        return  ProductImageEntity(
            model.id,
            model.productId,
            model.photoUrl
        )
    }

    override fun fromEntity(entity: ProductImageEntity): ProductImages {
        return ProductImages().apply {
            id = entity.id
            productId = entity.productId
            photoUrl = entity.url
        }
    }

}