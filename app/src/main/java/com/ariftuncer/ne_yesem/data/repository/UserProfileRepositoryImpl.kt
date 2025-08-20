// data/repository/UserProfileRepositoryImpl.kt
package com.ariftuncer.ne_yesem.data.repository

import com.ariftuncer.ne_yesem.domain.repository.UserProfileRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ne_yesem.domain.model.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserProfileRepository {

    private fun doc(uid: String) = db.collection("users").document(uid)

    override suspend fun get(uid: String): UserProfile? {
        val snap = doc(uid).get().await()
        return snap.toObject(UserProfile::class.java)
    }

    override suspend fun update(uid: String, name: String?, phone: String?): UserProfile {
        // Sadece gelen alanları patch et
        val patch = buildMap<String, Any> {
            if (name != null) put("name", name)
            if (phone != null) put("phoneNumber", phone)
        }
        if (patch.isNotEmpty()) {
            doc(uid).set(patch, SetOptions.merge()).await()
        }
        // Güncel dökümana göre model döndür
        return get(uid) ?: UserProfile(
            email = "", // email dokümanda yoksa boş gelebilir; ViewModel auth’tan besler
            uid = uid,
            name = name,
            phoneNumber = phone
        )
    }
    override fun getCurrentUser(): UserProfile? {
        val u = auth.currentUser ?: return null
        return UserProfile(
            email = u.email.orEmpty(),
            uid = u.uid,
            name = null,
            phoneNumber = null
        )
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Pair<String, Boolean> {
        val user = auth.currentUser ?: return Pair("Oturum bulunamadı", false)
        val email = user.email ?: return Pair("Kullanıcı email bulunamadı", false)

        return try {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            user.reauthenticate(credential).await()
            user.updatePassword(newPassword).await()
            Pair("Şifre başarıyla güncellendi", true)
        } catch (e: Exception) {
            Pair(e.message ?: "Şifre güncelleme hatası", false)
        }
    }
}
