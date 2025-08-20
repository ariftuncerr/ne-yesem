package com.ariftuncer.ne_yesem.domain.usecase.user
import com.ariftuncer.ne_yesem.domain.repository.UserProfileRepository
import com.ne_yesem.domain.model.UserProfile

class GetUserProfileUseCase(
    private val repo: UserProfileRepository
) {
    suspend operator fun invoke(uid: String): UserProfile? = repo.get(uid)
}
