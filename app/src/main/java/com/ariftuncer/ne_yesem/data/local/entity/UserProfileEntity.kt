package com.neyesem.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val uid: String,
    val name: String?,
    val phoneNumber: String?,
    val updatedAt: Long
)
