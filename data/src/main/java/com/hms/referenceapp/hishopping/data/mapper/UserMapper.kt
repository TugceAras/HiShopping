package com.hms.referenceapp.hishopping.data.mapper

import com.hms.referenceapp.hishopping.core.EntityMapper
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.data.clouddb.model.User as UserCloud

object UserMapper : EntityMapper<User,UserCloud> {
    override fun toEntity(model: UserCloud): User {
        return User(
            id = model.id,
            fullName = model.fullName,
            email = model.email,
            photoUrl= model.photoUrl,
            serviceType = model.serviceType,
            providerType = model.providerType
        )
    }

    override fun fromEntity(entity: User): UserCloud {
        return UserCloud().apply {
            id = entity.id
            fullName = entity.fullName
            email = entity.email
            photoUrl = entity.photoUrl
            serviceType = entity.serviceType
            providerType = entity.providerType
        }
    }
}