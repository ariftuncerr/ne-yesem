package com.ariftuncer.ne_yesem.presentation.viewmodel

/*import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariftuncer.ne_yesem.domain.repository.AuthRepository
import com.ariftuncer.ne_yesem.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel1 : ViewModel() {

    private val authRepository = AuthRepository()

    private val _registerResult = MutableLiveData<Pair<Boolean, String>>()
    val registerResult: LiveData<Pair<Boolean, String>> get() = _registerResult

    fun register(email: String, password: String) {
        viewModelScope.launch {
            authRepository.register(email, password) { success, message ->
                _registerResult.value = Pair(success, message) as Pair<Boolean, String>?
            }

        }
    }

    private val _loginResult = MutableLiveData<Pair<Boolean, String>>()
    val loginResult: LiveData<Pair<Boolean, String>> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login (email,password) { success, message ->
                _loginResult.value = Pair(success,message) as Pair<Boolean, String>?
            }

        }
    }

    private val _googleRegisterResult = MutableLiveData<Pair<Boolean, String?>>()
    val googleRegisterResult: LiveData<Pair<Boolean, String?>> get() = _googleRegisterResult

    fun registerWithGoogle(idToken: String) {
        authRepository.registerWithGoogle(idToken) { success, message ->
            _googleRegisterResult.value = Pair(success, message)
        }
    }

    private val _loginWithGoogleResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginWithGoogleResult : LiveData<Pair<Boolean, String?>> = _loginWithGoogleResult


    fun loginWithGoogle(idToken: String) {
        authRepository.loginWithGoogle(idToken) { success, message ->
            _loginWithGoogleResult.value = Pair(success, message)
        }
    }


    private val _resetResult = MutableLiveData<Pair<String, Boolean>>()
    val resetResult: LiveData<Pair<String, Boolean>> = _resetResult

    fun sendResetEmail(email: String) {
        authRepository.sendPasswordResetEmail(email) { result ->
            _resetResult.postValue(result)
        }
    }


    //facebook login
    private val _facebookLoginResult = MutableLiveData<Pair<Boolean, String?>>()
    val facebookLoginResult: LiveData<Pair<Boolean, String?>> = _facebookLoginResult

    fun loginWithFacebook(token: String) {
        authRepository.loginWithFacebook(token) { success, message ->
            _facebookLoginResult.value = Pair(success, message)
        }
    }

}*/