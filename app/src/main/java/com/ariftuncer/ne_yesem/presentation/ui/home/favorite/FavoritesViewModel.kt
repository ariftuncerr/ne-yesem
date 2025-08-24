package com.ariftuncer.ne_yesem.presentation.viewmodel

import androidx.lifecycle.*
import com.ariftuncer.ne_yesem.domain.model.FavoriteRecipeCard
import com.ariftuncer.ne_yesem.domain.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repo: FavoritesRepository
) : ViewModel() {

    private val _ids = MutableLiveData<Set<Int>>(emptySet())
    val ids: LiveData<Set<Int>> = _ids

    private val _cards = MutableLiveData<List<FavoriteRecipeCard>>(emptyList())
    val cards: LiveData<List<FavoriteRecipeCard>> = _cards

    fun refresh(uid: String) = viewModelScope.launch {
        runCatching { repo.getIds(uid).toSet() }
            .onSuccess { _ids.postValue(it) }
        runCatching { repo.getCards(uid) }
            .onSuccess { _cards.postValue(it) }
            .onFailure { _cards.postValue(emptyList()) }
    }

    fun toggle(uid: String, recipeId: Int, nowFavorite: Boolean) = viewModelScope.launch {
        if (nowFavorite) repo.add(uid, recipeId) else repo.remove(uid, recipeId)
        // local state’i hızlı güncelle
        val cur = _ids.value?.toMutableSet() ?: mutableSetOf()
        if (nowFavorite) cur += recipeId else cur -= recipeId
        _ids.postValue(cur)
    }
}
