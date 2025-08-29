package com.ariftuncer.ne_yesem.domain.usecase.auth

import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.domain.repository.AuthRepository

class SendPasswordReset(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String): AppResult<Unit> {
        return repo.sendPasswordReset(email)
    }
}
