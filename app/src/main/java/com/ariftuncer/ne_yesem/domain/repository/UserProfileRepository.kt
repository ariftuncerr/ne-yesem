package com.ariftuncer.ne_yesem.domain.repository
// domain/repository/UserProfileRepository.kt
import com.ne_yesem.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun get(uid: String): UserProfile?
    suspend fun update(uid: String, name: String?, phone: String?): UserProfile
    fun getCurrentUser(): UserProfile?
    suspend fun changePassword(currentPassword: String, newPassword: String): Pair<String, Boolean>
}
