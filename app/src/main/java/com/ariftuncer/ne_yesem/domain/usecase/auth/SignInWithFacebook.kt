package com.ariftuncer.ne_yesem.domain.usecase.auth

import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.domain.model.AuthOutcome
import com.ariftuncer.ne_yesem.domain.repository.AuthRepository

class SignInWithFacebook(private val authRepo: AuthRepository) {
    suspend operator fun invoke(accessToken: String): AppResult<AuthOutcome> =
        authRepo.signInWithFacebook(accessToken)
}