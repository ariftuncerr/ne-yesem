package com.neyesem.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neyesem.data.local.dao.UserProfileDao
import com.neyesem.data.local.entity.UserProfileEntity

@Database(
    entities = [UserProfileEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
}
