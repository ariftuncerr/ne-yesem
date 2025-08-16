package com.ariftuncer.ne_yesem.domain.usecase.auth


import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.domain.model.AuthOutcome
import com.ariftuncer.ne_yesem.domain.repository.AuthRepository

class SignInWithGoogle(private val authRepo: AuthRepository) {
    suspend operator fun invoke(idToken: String): AppResult<AuthOutcome> =
        authRepo.signInWithGoogle(idToken)
}
