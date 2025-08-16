package com.ariftuncer.ne_yesem.domain.repository

import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ne_yesem.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // LOCAL (Room) — password tutmayız
    suspend fun ensureLocalProfile(uid: String, name: String?, phone: String?): AppResult<UserProfile>
    suspend fun upsertLocal(profile: UserProfile): AppResult<Unit>
    suspend fun getLocal(): AppResult<UserProfile?>
    fun observeLocal(): Flow<UserProfile?>

    // REMOTE (Firestore) — yedek/cihaz değişimi için
    suspend fun ensureRemoteProfile(uid: String, name: String?, phone: String?): AppResult<Unit>
    suspend fun pullRemoteToLocal(uid: String): AppResult<UserProfile?>
}