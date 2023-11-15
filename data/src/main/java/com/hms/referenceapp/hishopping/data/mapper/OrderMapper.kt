package com.hms.referenceapp.hishopping.data.mapper

import com.hms.referenceapp.hishopping.core.EntityMapper
import com.hms.referenceapp.hishopping.data.model.Order
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import com.hms.referenceapp.hishopping.data.clouddb.model.Order as OrderCloud

object OrderMapper : EntityMapper<OrderEntity,OrderCloud> {
    override fun toEntity(model: OrderCloud): OrderEntity {
        return OrderEntity(
            id = model.id,
            productId = model.productId,
            userId = model.userId,
            userLat = model.userLat,
            userLng = model.userLng,
            addressId = model.addressId,
            address = model.address,
            orderStatus = if (model.status == 0) Order.OrderStatus.TRANSPORT else Order.OrderStatus.DELIVERED,
            date = model.date,
            storeId = model.storeId,
            storeLat = model.storeLat,
            storeLng = model.storeLng,
            productTitle = model.productTitle,
            productPhotoUrl = model.productPhotoUrl
        )
    }

    override fun fromEntity(entity: OrderEntity): OrderCloud {
        return OrderCloud().apply {
            id = entity.id
            productId = entity.productId
            userId = entity.userId
            userLat = entity.userLat
            userLng = entity.userLng
            addressId = entity.addressId
            address = entity.address
            status = if (entity.orderStatus == Order.OrderStatus.TRANSPORT) 0 else 1
            date = entity.date
            storeId = entity.storeId
            storeLat = entity.storeLat
            storeLng = entity.storeLng
            productTitle = entity.productTitle
            productPhotoUrl = entity.productPhotoUrl
        }
    }
}