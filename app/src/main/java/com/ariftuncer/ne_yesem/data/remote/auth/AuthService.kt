package com.ariftuncer.ne_yesem.data.remote.auth

import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.domain.model.user.AuthOutcome
import com.ariftuncer.ne_yesem.domain.model.user.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthService {
    suspend fun signUpWithEmail(email: String, password: String): AppResult<AuthOutcome>
    suspend fun signInWithEmail(email: String, password: String): AppResult<AuthOutcome>
    suspend fun signInWithGoogle(idToken: String): AppResult<AuthOutcome>
    suspend fun signInWithFacebook(accessToken: String): AppResult<AuthOutcome>

    suspend fun linkWithGoogle(idToken: String): AppResult<AuthUser>
    suspend fun linkWithFacebook(accessToken: String): AppResult<AuthUser>

    suspend fun sendPasswordReset(email: String): AppResult<Unit>

    fun observeAuthState(): Flow<AuthUser?>
    suspend fun signOut(): AppResult<Unit>
}
