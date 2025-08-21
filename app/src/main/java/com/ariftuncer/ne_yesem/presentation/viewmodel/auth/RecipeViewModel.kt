// presentation/recipe/RecipeViewModel.kt
package com.ne_yesem.presentation.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ne_yesem.domain.repository.GenerateParams
import com.ne_yesem.domain.repository.PantryItemLite
import com.ne_yesem.domain.usecase.GenerateRecipesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeUiState(
    val loading: Boolean = false,
    val text: String? = null,
    val error: String? = null
)

class RecipeViewModel @Inject constructor(
    private val generate: GenerateRecipesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeUiState())
    val state: StateFlow<RecipeUiState> = _state

    fun generateFrom(
        pantry: List<PantryItemLite>,
        dietType: String?,
        allergens: List<String>,
        dislikes: List<String>
    ) {
        viewModelScope.launch {
            _state.value = RecipeUiState(loading = true)
            try {
                val res = generate(
                    GenerateParams(pantry, dietType, allergens, dislikes)
                )
                _state.value = RecipeUiState(text = res)
            } catch (e: Exception) {
                _state.value = RecipeUiState(error = e.message)
            }
        }
    }
}
