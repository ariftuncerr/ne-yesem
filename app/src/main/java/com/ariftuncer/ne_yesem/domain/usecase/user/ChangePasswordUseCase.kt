package com.ariftuncer.ne_yesem.domain.usecase.user

import com.ariftuncer.ne_yesem.domain.repository.UserProfileRepository

class ChangePasswordUseCase(
    private val repo: UserProfileRepository
) {
    suspend operator fun invoke(currentPassword: String, newPassword: String): Pair<String, Boolean> {
        return repo.changePassword(currentPassword, newPassword)
    }
}
