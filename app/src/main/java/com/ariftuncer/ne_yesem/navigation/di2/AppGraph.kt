package com.ariftuncer.ne_yesem.di

import com.ariftuncer.ne_yesem.data.remote.auth.AuthService
import com.ariftuncer.ne_yesem.data.remote.auth.FirebaseAuthService
import com.ariftuncer.ne_yesem.data.remote.firestore.FirestoreUserRemoteDataSource
import com.ariftuncer.ne_yesem.data.remote.firestore.UserRemoteDataSource
import com.ariftuncer.ne_yesem.data.repository.AuthRepositoryImpl
import com.ariftuncer.ne_yesem.domain.repository.AuthRepository
import com.ariftuncer.ne_yesem.domain.usecase.auth.*
import com.ariftuncer.ne_yesem.presentation.viewmodel.auth.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.ne_yesem.domain.usecase.auth.SignUpWithEmail

object AppGraph : AuthViewModelFactory.Provider {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { com.google.firebase.Firebase.firestore }

    private val authService: AuthService by lazy { FirebaseAuthService(firebaseAuth) }
    private val userRemote: UserRemoteDataSource by lazy { FirestoreUserRemoteDataSource(firestore) }

    private val authRepository: AuthRepository by lazy { AuthRepositoryImpl(authService, userRemote) }

    override val signInEmail by lazy { SignInWithEmail(authRepository) }
    override val signUpEmail by lazy { SignUpWithEmail(authRepository) }
    override val signInGoogle by lazy { SignInWithGoogle(authRepository) }
    override val signInFacebook by lazy { SignInWithFacebook(authRepository) }
    override val sendPasswordReset by lazy { SendPasswordReset(authRepository) }

    val authVmFactory by lazy { AuthViewModelFactory(this) }
}
