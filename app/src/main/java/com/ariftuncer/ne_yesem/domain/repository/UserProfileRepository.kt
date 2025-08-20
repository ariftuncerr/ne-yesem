package com.ariftuncer.ne_yesem.domain.repository
// domain/repository/UserProfileRepository.kt
import com.ne_yesem.domain.model.UserProfile

interface UserProfileRepository {
    /** null dönerse profil dokümanı henüz yok demektir */
    suspend fun get(uid: String): UserProfile?

    /** Sadece verilen alanları günceller, diğerlerine dokunmaz */
    suspend fun update(uid: String, name: String?, phone: String?): UserProfile

    fun getCurrentUser(): UserProfile?

    /** FirebaseAuth şifre değiştirme */
    suspend fun changePassword(currentPassword: String, newPassword: String): Pair<String, Boolean>
}
