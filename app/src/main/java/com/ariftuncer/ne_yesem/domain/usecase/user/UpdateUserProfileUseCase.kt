// domain/usecase/user/UpdateUserProfileUseCase.kt
package com.ariftuncer.ne_yesem.domain.usecase.user

import com.ariftuncer.ne_yesem.domain.repository.UserProfileRepository
import com.ne_yesem.domain.model.UserProfile

class UpdateUserProfileUseCase(
    private val repo: UserProfileRepository
) {
    /** İstediğin alanları (name/phone) kısmi güncelle */
    suspend operator fun invoke(uid: String, name: String?, phone: String?): UserProfile =
        repo.update(uid, name, phone)
}
