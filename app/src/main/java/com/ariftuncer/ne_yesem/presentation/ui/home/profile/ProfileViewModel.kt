// presentation/profile/ProfileViewModel.kt
package com.ariftuncer.ne_yesem.presentation.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariftuncer.ne_yesem.domain.usecase.user.ChangePasswordUseCase
import com.ariftuncer.ne_yesem.domain.usecase.user.GetCurrentUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.ariftuncer.ne_yesem.domain.usecase.user.GetUserProfileUseCase
import com.ariftuncer.ne_yesem.domain.usecase.user.UpdateUserProfileUseCase
import com.ne_yesem.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getUserProfile: GetUserProfileUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _profile = MutableLiveData<UserProfile?>()
    val profile: LiveData<UserProfile?> = _profile

    // İstersen basit durum bilgileri:
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadProfile() {
        val u = auth.currentUser ?: run {
            _error.value = "Oturum bulunamadı"
            return
        }
        _loading.value = true
        viewModelScope.launch {
            try {
                val p = getUserProfile(u.uid)
                // email Firestore’da boş olabilir; auth’tan tamamlayalım
                _profile.value = p?.copy(email = u.email.orEmpty())
                    ?: UserProfile(
                        email = u.email.orEmpty(),
                        uid = u.uid,
                        name = null,
                        phoneNumber = null
                    )
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun saveProfile(name: String?, phone: String?) {
        val u = auth.currentUser ?: return
        _loading.value = true
        viewModelScope.launch {
            try {
                val updated = updateUserProfile(u.uid, name, phone)
                _profile.value = updated.copy(email = u.email.orEmpty())
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    private val _passwordResult = MutableLiveData<Pair<String, Boolean>>()
    val passwordResult: LiveData<Pair<String, Boolean>> = _passwordResult

    fun changePassword(current: String, new: String) {
        viewModelScope.launch {
            val result = changePasswordUseCase(current, new)
            _passwordResult.value = result as Pair<String, Boolean>?
        }
    }

    private val _singOutResult = MutableLiveData<Boolean>()
    val signOutResult: LiveData<Boolean> = _singOutResult
    fun signOut() {
        viewModelScope.launch {
            try {
                auth.signOut()
                _singOutResult.value = true
            } catch (e: Exception) {
                _singOutResult.value = false
            }
        }
    }


}

