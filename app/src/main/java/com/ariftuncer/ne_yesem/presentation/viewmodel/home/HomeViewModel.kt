package com.ariftuncer.ne_yesem.presentation.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariftuncer.ne_yesem.data.remote.firestore.UserRemoteDataSource
import com.ariftuncer.ne_yesem.domain.model.RecipeDetail
import com.ariftuncer.ne_yesem.domain.repository.RecipeRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val _lastViewedRecipes = MutableLiveData<List<RecipeDetail>>()
    val lastViewedRecipes: LiveData<List<RecipeDetail>> = _lastViewedRecipes

    fun loadLastViewedRecipes() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            val ids = userRemoteDataSource.getLastViewedIds(uid)
            val details = ids.mapNotNull { id ->
                runCatching { recipeRepository.getRecipeDetail(id) }.getOrNull()
            }
            _lastViewedRecipes.postValue(details)
        }
    }
}

