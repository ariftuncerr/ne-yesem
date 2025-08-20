package com.neyesem.domain.usecase.user

import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.domain.repository.UserRepository
import com.ne_yesem.domain.model.UserProfile

class EnsureUserProfileOnLogin(
    private val userRepo: UserRepository
) {
    suspend operator fun invoke(
        uid: String,
        name: String?,
        phone: String?
    ): AppResult<UserProfile> {
        val local = userRepo.ensureLocalProfile(uid, name, phone)
        // Firestore'da (remote) dokümanı hazırla/merge et
        // (yedeklemeler için)
        userRepo.ensureRemoteProfile(uid, name, phone)
        return local
    }
}
