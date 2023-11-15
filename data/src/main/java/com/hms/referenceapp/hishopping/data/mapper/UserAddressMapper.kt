package com.hms.referenceapp.hishopping.data.mapper

import com.hms.referenceapp.hishopping.core.EntityMapper
import com.hms.referenceapp.hishopping.data.model.UserAddress
import com.hms.referenceapp.hishopping.data.clouddb.model.UserAddress as UserAddressCloud

object UserAddressMapper: EntityMapper<UserAddress,UserAddressCloud> {
    override fun toEntity(model: UserAddressCloud): UserAddress {
        return UserAddress(
            id = model.id,
            userId = model.userId,
            addressLine = model.addressLine,
            country = model.country,
            province = model.province,
            buildingNo = model.buildingNo,
            aptNo = model.aptNo,
            latitude = model.latitude,
            longitude = model.longitude,
            title = model.title
        )
    }

    override fun fromEntity(entity: UserAddress): UserAddressCloud {
        return UserAddressCloud().apply {
            id = entity.id
            userId = entity.userId
            addressLine = entity.addressLine
            country = entity.country
            province = entity.province
            buildingNo = entity.buildingNo
            aptNo = entity.aptNo
            latitude = entity.latitude
            longitude = entity.longitude
            title = entity.title
        }
    }
}