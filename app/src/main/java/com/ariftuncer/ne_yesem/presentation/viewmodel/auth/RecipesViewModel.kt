package com.ariftuncer.ne_yesem.presentation.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariftuncer.ne_yesem.domain.model.RecipeItem
import com.ariftuncer.ne_yesem.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val repo: RecipeRepository
) : ViewModel() {

    // UI state kullanmıyoruz; çağıran yer direkt observe edebilir.
    private val _recommended = MutableLiveData<List<RecipeItem>>()
    val recommended: LiveData<List<RecipeItem>> = _recommended

    fun loadRecommendations(ingredients: List<String>, number: Int = 10) {
        viewModelScope.launch {
            runCatching { repo.searchByIngredients(ingredients) }
                .onSuccess { _recommended.postValue(it.take(number)) }
                .onFailure { _recommended.postValue(emptyList()) }
        }
    }
}
