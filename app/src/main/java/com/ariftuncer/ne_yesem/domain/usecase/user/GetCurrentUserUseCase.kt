package com.ariftuncer.ne_yesem.domain.usecase.user

import com.ariftuncer.ne_yesem.domain.repository.UserProfileRepository
import com.ne_yesem.domain.model.UserProfile

class GetCurrentUserUseCase(
    private val repo: UserProfileRepository
) {
    operator fun invoke(): UserProfile? = repo.getCurrentUser()
}
