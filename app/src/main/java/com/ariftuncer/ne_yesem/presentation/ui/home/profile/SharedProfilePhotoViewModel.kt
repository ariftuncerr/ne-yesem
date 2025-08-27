package com.ariftuncer.ne_yesem.presentation.ui.home.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedProfilePhotoViewModel : ViewModel() {
    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri

    fun setPhoto(uri: Uri?) { _photoUri.value = uri }
}
