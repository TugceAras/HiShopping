package com.hms.referenceapp.hishopping.data.mapper

import com.hms.referenceapp.hishopping.core.EntityMapper
import com.hms.referenceapp.hishopping.data.model.Favorites
import com.hms.referenceapp.hishopping.data.clouddb.model.Favorites as FavoritesCloud

object FavoriteMapper : EntityMapper<Favorites,FavoritesCloud> {

    override fun toEntity(model: FavoritesCloud): Favorites {
        return Favorites(
            id = model.id,
            userId = model.userId,
            productId = model.productId,
            productPhotoUrl = model.productPhotoUrl,
            productTitle = model.productTitle,
            productPrice = model.productPrice,
            productOldPrice = model.productOldPrice
        )
    }

    override fun fromEntity(entity: Favorites): com.hms.referenceapp.hishopping.data.clouddb.model.Favorites {
        return FavoritesCloud().apply {
            id = entity.id
            userId = entity.userId
            productId = entity.productId
            productPhotoUrl = entity.productPhotoUrl
            productTitle = entity.productTitle
            productPrice = entity.productPrice
            productOldPrice = entity.productOldPrice
        }
    }

}