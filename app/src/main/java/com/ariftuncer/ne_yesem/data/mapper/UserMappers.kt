package com.neyesem.data.mapper

import com.ne_yesem.domain.model.UserProfile
import com.neyesem.data.local.entity.UserProfileEntity

object UserMappers {
    fun toEntity(domain: UserProfile, now: Long): UserProfileEntity =
        UserProfileEntity(
            uid = domain.uid,
            name = domain.name,
            phoneNumber = domain.phoneNumber,
            updatedAt = now
        )

    fun toDomain(entity: UserProfileEntity): UserProfile =
        UserProfile(
            uid = entity.uid,
            name = entity.name,
            phoneNumber = entity.phoneNumber
        )
}
