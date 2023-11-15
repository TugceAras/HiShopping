package com.hms.referenceapp.hishopping.data.mapper

import com.hms.referenceapp.hishopping.core.EntityMapper
import com.hms.referenceapp.hishopping.data.model.CategoryEntity
import com.hms.referenceapp.hishopping.data.clouddb.model.Category as CategoryCloud

object CategoryMapper: EntityMapper<CategoryEntity, CategoryCloud> {

    override fun toEntity(model: CategoryCloud): CategoryEntity {
        return  CategoryEntity(
            id = model.id,
            name = model.name,
            photoUrl = model.photoUrl
        )
    }

    override fun fromEntity(entity: CategoryEntity): CategoryCloud {
        return  CategoryCloud().apply {
            id = entity.id
            name = entity.name
            photoUrl = entity.photoUrl
        }
    }

}