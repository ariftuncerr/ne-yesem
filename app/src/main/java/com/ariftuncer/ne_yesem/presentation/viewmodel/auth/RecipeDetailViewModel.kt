package com.ariftuncer.ne_yesem.presentation.viewmodel.recipe

import androidx.lifecycle.*
import com.ariftuncer.ne_yesem.domain.model.RecipeDetail
import com.ariftuncer.ne_yesem.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repo: RecipeRepository
) : ViewModel() {

    private val _detail = MutableLiveData<RecipeDetail?>()
    val detail: LiveData<RecipeDetail?> = _detail

    fun load(id: Int) = viewModelScope.launch {
        runCatching { repo.getRecipeDetail(id) }
            .onSuccess { _detail.postValue(it) }
            .onFailure { _detail.postValue(null) }
    }
}
