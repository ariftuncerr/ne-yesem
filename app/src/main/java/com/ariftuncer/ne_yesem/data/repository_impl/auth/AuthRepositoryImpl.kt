package com.ariftuncer.ne_yesem.data.repository
import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.core.result.Either
import com.ariftuncer.ne_yesem.domain.model.AuthOutcome
import com.ariftuncer.ne_yesem.domain.model.AuthUser
import com.ariftuncer.ne_yesem.domain.repository.AuthRepository
import com.ariftuncer.ne_yesem.data.remote.auth.AuthService
import com.ariftuncer.ne_yesem.data.remote.firestore.UserRemoteDataSource
import com.ne_yesem.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val auth: AuthService,
    private val userRemote: UserRemoteDataSource
) : AuthRepository {

    override suspend fun signUpWithEmail(email: String, password: String): AppResult<AuthOutcome> =
        when (val r = auth.signUpWithEmail(email, password)) {
            is Either.Left  -> r
            is Either.Right -> ensureUserIfNeeded(r.value)
        }

    override suspend fun signInWithEmail(email: String, password: String): AppResult<AuthOutcome> =
        when (val r = auth.signInWithEmail(email, password)) {
            is Either.Left  -> r
            is Either.Right -> ensureUserIfNeeded(r.value)
        }

    override suspend fun signInWithGoogle(idToken: String): AppResult<AuthOutcome> =
        when (val r = auth.signInWithGoogle(idToken)) {
            is Either.Left  -> r
            is Either.Right -> ensureUserIfNeeded(r.value)
        }

    override suspend fun signInWithFacebook(accessToken: String): AppResult<AuthOutcome> =
        when (val r = auth.signInWithFacebook(accessToken)) {
            is Either.Left  -> r
            is Either.Right -> ensureUserIfNeeded(r.value)
        }

    override suspend fun sendPasswordReset(email: String) = auth.sendPasswordReset(email)
    override fun observeAuthUser(): Flow<AuthUser?> = auth.observeAuthState()
    override suspend fun signOut() = auth.signOut()

    private suspend fun ensureUserIfNeeded(out: AuthOutcome): AppResult<AuthOutcome> {
        if (!out.isNewUser) return Either.Right(out)
        val profile = UserProfile(uid = out.user.uid, name = null, phoneNumber = null, email = out.user.email.toString())
        return when (val e = userRemote.ensureUserDocument(profile)) {
            is Either.Left  -> e
            is Either.Right -> Either.Right(out)
        }
    }
}
