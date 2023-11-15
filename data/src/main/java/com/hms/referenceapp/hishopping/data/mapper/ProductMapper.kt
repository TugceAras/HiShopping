package com.hms.referenceapp.hishopping.data.mapper

import com.hms.referenceapp.hishopping.data.model.*
import com.hms.referenceapp.hishopping.data.clouddb.model.Product as ProductCloud

/**
 * Created by Mustafa Kemal Özdemir on 3/24/2022.
 */
object ProductMapper {

    fun toEntity(
        model: ProductCloud,
        brand: BrandEntity? = null,
        category: CategoryEntity? = null,
        store: StoreEntity? = null,
        images: List<ProductImageEntity>
    ): ProductEntity {
        return ProductEntity(
            id = model.id,
            price = model.price,
            title = model.title,
            oldPrice = model.oldPrice,
            videoUrl = model.videoUrl,
            arSupported = model.arSupported,
            description = model.description.get(),
            brand = brand,
            category = category,
            store = store,
            images = images
        )
    }

}