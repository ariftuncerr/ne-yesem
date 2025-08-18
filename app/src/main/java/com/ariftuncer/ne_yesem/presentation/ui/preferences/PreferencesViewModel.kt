package com.ariftuncer.ne_yesem.presentation.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariftuncer.ne_yesem.domain.model.Preferences
import com.ariftuncer.ne_yesem.domain.usecase.preferences.GetPreferences
import com.ariftuncer.ne_yesem.domain.usecase.preferences.SavePreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.remove

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getPreferences: GetPreferences,
    private val savePreferences: SavePreferences
) : ViewModel() {

    private val _state = MutableStateFlow(Preferences())
    val state = _state.asStateFlow()

    fun load() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            getPreferences(uid)?.let { _state.value = it }
        }
    }

    fun selectDiet(diet: String) {
        _state.value = _state.value.copy(diet = diet)
    }

    fun toggleAllergen(label: String) {
        val s = _state.value.allergens.toMutableSet()
        if (!s.add(label)) s.remove(label)
        _state.value = _state.value.copy(allergens = s.toList())
    }

    fun toggleUnliked(label: String) {
        val s = _state.value.unlikedFoods.toMutableSet()
        if (!s.add(label)) s.remove(label)
        _state.value = _state.value.copy(unlikedFoods = s.toList())
    }

    fun save(onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            runCatching { savePreferences(uid, _state.value) }
                .onSuccess { onResult(true) }
                .onFailure { onResult(false) }
        }
    }
    // ...existing code...
    fun removeAllergen(label: String) {
        val s = _state.value.allergens.toMutableSet()
        s.remove(label)
        _state.value = _state.value.copy(allergens = s.toList())
    }

    fun removeUnliked(label: String) {
        val s = _state.value.unlikedFoods.toMutableSet()
        s.remove(label)
        _state.value = _state.value.copy(unlikedFoods = s.toList())
    }
// ...existing code...

}
