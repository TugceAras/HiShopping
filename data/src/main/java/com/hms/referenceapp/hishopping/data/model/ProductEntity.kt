package com.hms.referenceapp.hishopping.data.model

/**
 * Created by Mustafa Kemal Özdemir on 3/24/2022.
 */
data class ProductEntity(
    val id: String,
    val brand: BrandEntity?,
    val price: Double,
    val title: String,
    val oldPrice: Double?,
    val videoUrl: String?,
    val arSupported: Boolean,
    val category: CategoryEntity?,
    val store: StoreEntity?,
    val description: String,
    val images: List<ProductImageEntity>
)