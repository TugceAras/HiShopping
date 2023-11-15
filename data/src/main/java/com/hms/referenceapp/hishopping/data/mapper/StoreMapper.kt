package com.hms.referenceapp.hishopping.data.mapper

import com.hms.referenceapp.hishopping.core.EntityMapper
import com.hms.referenceapp.hishopping.data.model.StoreEntity
import com.hms.referenceapp.hishopping.data.clouddb.model.Store as StoreCloud

object StoreMapper : EntityMapper<StoreEntity,StoreCloud> {
    override fun toEntity(model: StoreCloud): StoreEntity {
        return StoreEntity(
            id = model.id,
            name = model.name,
            lat = model.latitude,
            lon = model.longitude,
            addressLine = model.address,
            photoUrl = model.photoUrl
        )
    }

    override fun fromEntity(entity: StoreEntity): StoreCloud {
        return StoreCloud().apply {
            id = entity.id
            name = entity.name
            latitude = entity.lat
            longitude = entity.lon
            address = entity.addressLine
            photoUrl = entity.photoUrl
        }
    }
}