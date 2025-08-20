package com.ariftuncer.ne_yesem.presentation.viewmodel.auth

import androidx.lifecycle.*
import com.ariftuncer.ne_yesem.core.result.Either
import com.ariftuncer.ne_yesem.core.result.readableMessage
import com.ariftuncer.ne_yesem.domain.model.user.AuthOutcome
import com.ariftuncer.ne_yesem.domain.usecase.auth.*
import com.ne_yesem.domain.usecase.auth.SignUpWithEmail
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signInEmail: SignInWithEmail,
    private val signUpEmail: SignUpWithEmail,
    private val signInGoogle: SignInWithGoogle,
    private val signInFacebook: SignInWithFacebook,
    private val sendPasswordReset: SendPasswordReset
) : ViewModel() {

    private val _login = MutableLiveData<Pair<Boolean, String>>()
    val login: LiveData<Pair<Boolean, String>> = _login

    private val _register = MutableLiveData<Pair<Boolean, String>>()
    val register: LiveData<Pair<Boolean, String>> = _register

    private val _google = MutableLiveData<Pair<Boolean, String>>()
    val google: LiveData<Pair<Boolean, String>> = _google

    private val _facebook = MutableLiveData<Pair<Boolean, String>>()
    val facebook: LiveData<Pair<Boolean, String>> = _facebook

    private val _reset = MutableLiveData<Pair<Boolean, String>>()
    val reset: LiveData<Pair<Boolean, String>> = _reset

    private var lastAuthOutcome: AuthOutcome? = null
    val isNewUserRegister: Boolean?
        get() = lastAuthOutcome?.isNewUser
    val isNewUserGoogle: Boolean?
        get() = lastAuthOutcome?.isNewUser
    val isNewUserLogin: Boolean?
        get() = lastAuthOutcome?.isNewUser

    fun login(email: String, password: String) = viewModelScope.launch {
        when (val r = signInEmail(email, password)) {
            is Either.Left  -> _login.postValue(false to r.value.readableMessage())
            is Either.Right -> {
                lastAuthOutcome = r.value
                _login.postValue(true to "Giriş başarılı")
            }
        }
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        when (val r = signUpEmail(email, password)) {
            is Either.Left  -> _register.postValue(false to r.value.readableMessage())
            is Either.Right -> {
                lastAuthOutcome = r.value
                _register.postValue(true to "Kayıt başarılı")
            }
        }
    }

    fun loginWithGoogle(idToken: String) = viewModelScope.launch {
        when (val r = signInGoogle(idToken)) {
            is Either.Left  -> _google.postValue(false to r.value.readableMessage())
            is Either.Right -> {
                lastAuthOutcome = r.value
                _google.postValue(true to "Google ile giriş başarılı")
            }
        }
    }

    fun loginWithFacebook(token: String) = viewModelScope.launch {
        when (val r = signInFacebook(token)) {
            is Either.Left  -> _facebook.postValue(false to r.value.readableMessage())
            is Either.Right -> _facebook.postValue(true to "Facebook ile giriş başarılı")
        }
    }

    fun sendResetEmail(email: String) = viewModelScope.launch {
        when (val r = sendPasswordReset(email)) {
            is Either.Left  -> _reset.postValue(false to r.value.readableMessage())
            is Either.Right -> _reset.postValue(true to "Şifre sıfırlama e-postası gönderildi")
        }
    }
}

class AuthViewModelFactory(
    private val provider: Provider
) : ViewModelProvider.Factory {

    interface Provider {
        val signInEmail: SignInWithEmail
        val signUpEmail: SignUpWithEmail
        val signInGoogle: SignInWithGoogle
        val signInFacebook: SignInWithFacebook
        val sendPasswordReset: SendPasswordReset
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(AuthViewModel::class.java))
        return AuthViewModel(
            provider.signInEmail,
            provider.signUpEmail,
            provider.signInGoogle,
            provider.signInFacebook,
            provider.sendPasswordReset
        ) as T
    }
}
