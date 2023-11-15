package com.hms.referenceapp.hishopping.core

/**
 * Created by Mustafa Kemal Özdemir on 3/24/2022.
 */
interface EntityMapper<E, M> {
    fun toEntity(model: M): E
    fun fromEntity(entity: E): M
}