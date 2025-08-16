package com.ne_yesem.domain.usecase.auth

import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.domain.model.AuthOutcome
import com.ariftuncer.ne_yesem.domain.repository.AuthRepository

class SignUpWithEmail(private val authRepo: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): AppResult<AuthOutcome> =
        authRepo.signUpWithEmail(email, password)
}
