package com.ariftuncer.ne_yesem.domain.repository

import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.domain.model.AuthOutcome
import com.ariftuncer.ne_yesem.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUpWithEmail(email: String, password: String): AppResult<AuthOutcome>
    suspend fun signInWithEmail(email: String, password: String): AppResult<AuthOutcome>
    suspend fun signInWithGoogle(idToken: String): AppResult<AuthOutcome>
    suspend fun signInWithFacebook(accessToken: String): AppResult<AuthOutcome>
    suspend fun sendPasswordReset(email: String): AppResult<Unit>
    fun observeAuthUser(): Flow<AuthUser?>
    suspend fun signOut(): AppResult<Unit>
}