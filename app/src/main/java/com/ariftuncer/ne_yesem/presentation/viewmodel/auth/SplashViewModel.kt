package com.ariftuncer.ne_yesem.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariftuncer.ne_yesem.domain.repository.AuthRepository
import com.neyesem.domain.usecase.user.EnsureUserProfileOnLogin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface SplashEffect {
    data class Navigate(val route: String) : SplashEffect
}

class SplashViewModel(
    private val authRepo: AuthRepository,
    private val ensureUserProfileOnLogin: EnsureUserProfileOnLogin
) : ViewModel() {

    private val _effect = Channel<SplashEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onStart() {
        viewModelScope.launch {
            // 1) authRepo.observeAuthState().first()
            // 2) user == null -> Navigate("login")
            // 3) user != null -> ensureUserProfileOnLogin(user.uid, null, null)  (idempotent)
            // 4) Navigate("home")
        }
    }
}
