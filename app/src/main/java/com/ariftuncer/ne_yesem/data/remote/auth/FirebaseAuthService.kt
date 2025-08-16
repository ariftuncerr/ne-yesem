// data/remote/auth/FirebaseAuthService.kt
package com.ariftuncer.ne_yesem.data.remote.auth

import com.ariftuncer.ne_yesem.core.result.AppError
import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.core.result.Either
import com.ariftuncer.ne_yesem.domain.model.AuthOutcome
import com.ariftuncer.ne_yesem.domain.model.AuthUser
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(
    private val auth: FirebaseAuth
) : AuthService {

    override suspend fun signUpWithEmail(email: String, password: String): AppResult<AuthOutcome> =
        runCatching {
            val res = auth.createUserWithEmailAndPassword(email, password).await()
            val u = res.user ?: error("User null")
            Either.Right(AuthOutcome(u.toAuthUser(), isNewUser = true))
        }.getOrElse { Either.Left(AppError.Auth(null, it.message)) }

    override suspend fun signInWithEmail(email: String, password: String): AppResult<AuthOutcome> =
        runCatching {
            val res = auth.signInWithEmailAndPassword(email, password).await()
            val u = res.user ?: error("User null")
            val isNew = res.additionalUserInfo?.isNewUser == true
            Either.Right(AuthOutcome(u.toAuthUser(), isNew))
        }.getOrElse { Either.Left(AppError.Auth(null, it.message)) }

    override suspend fun signInWithGoogle(idToken: String): AppResult<AuthOutcome> =
        signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))

    override suspend fun signInWithFacebook(accessToken: String): AppResult<AuthOutcome> =
        signInWithCredential(FacebookAuthProvider.getCredential(accessToken))

    private suspend fun signInWithCredential(cred: com.google.firebase.auth.AuthCredential): AppResult<AuthOutcome> =
        runCatching {
            val res = auth.signInWithCredential(cred).await()
            val u = res.user ?: error("User null")
            val isNew = res.additionalUserInfo?.isNewUser == true
            Either.Right(AuthOutcome(u.toAuthUser(), isNew))
        }.getOrElse { Either.Left(AppError.Auth(null, it.message)) }

    override suspend fun linkWithGoogle(idToken: String): AppResult<AuthUser> =
        runCatching {
            val u = auth.currentUser ?: error("No current user")
            val res = u.linkWithCredential(GoogleAuthProvider.getCredential(idToken, null)).await()
            Either.Right(res.user!!.toAuthUser())
        }.getOrElse { Either.Left(AppError.Auth(null, it.message)) }

    override suspend fun linkWithFacebook(accessToken: String): AppResult<AuthUser> =
        runCatching {
            val u = auth.currentUser ?: error("No current user")
            val res = u.linkWithCredential(FacebookAuthProvider.getCredential(accessToken)).await()
            Either.Right(res.user!!.toAuthUser())
        }.getOrElse { Either.Left(AppError.Auth(null, it.message)) }

    override suspend fun sendPasswordReset(email: String): AppResult<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Either.Right(Unit)
        } catch (e: Exception) {
            Either.Left(AppError.Auth(code = null, message = e.message))
        }
    }




    override fun observeAuthState(): Flow<AuthUser?> = callbackFlow {
        val l = FirebaseAuth.AuthStateListener { a -> trySend(a.currentUser?.toAuthUser()) }
        auth.addAuthStateListener(l)
        awaitClose { auth.removeAuthStateListener(l) }
    }

    override suspend fun signOut(): AppResult<Unit> =
        runCatching { auth.signOut(); Either.Right(Unit) }
            .getOrElse { Either.Left(AppError.Unknown(it)) }
}

private fun com.google.firebase.auth.FirebaseUser.toAuthUser() = AuthUser(
    uid = uid,
    email = email,
    providerIds = providerData.mapNotNull { it.providerId }
)
