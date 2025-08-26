package com.ariftuncer.ne_yesem.presentation.ui.home.fridge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ariftuncer.ne_yesem.domain.usecase.pantry.AddPantryItemUseCase
import com.ariftuncer.ne_yesem.domain.usecase.pantry.DeletePantryItemUseCase
import com.ariftuncer.ne_yesem.domain.usecase.pantry.GetAllPantryItemsUseCase
import com.ariftuncer.ne_yesem.domain.usecase.pantry.GetItemsByCategoryUseCase
import com.ariftuncer.ne_yesem.domain.usecase.pantry.UpdateExpiryUseCase
import com.ariftuncer.ne_yesem.domain.usecase.pantry.UpdatePantryItemUseCase
import com.google.firebase.Timestamp
import com.ariftuncer.ne_yesem.domain.model.IngredientCategory
import com.ariftuncer.ne_yesem.domain.model.PantryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PantryViewModel @Inject constructor(
    private val addPantryItemUseCase: AddPantryItemUseCase,
    private val updatePantryItemUseCase: UpdatePantryItemUseCase,
    private val deletePantryItemUseCase: DeletePantryItemUseCase,
    private val getItemsByCategoryUseCase: GetItemsByCategoryUseCase,
    private val getAllPantryItemsUseCase: GetAllPantryItemsUseCase,
    private val updateExpiryUseCase: UpdateExpiryUseCase
) : ViewModel() {
    private val _items = MutableLiveData<List<PantryItem>>()
    val items: LiveData<List<PantryItem>> get() = _items

    private val _categoryItems = MutableLiveData<List<PantryItem>>()
    val categoryItems: LiveData<List<PantryItem>> get() = _categoryItems

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun addItem(item: PantryItem) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = addPantryItemUseCase(item)
                fetchAllItems()
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun updateItem(item: PantryItem) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = updatePantryItemUseCase(item)
                fetchAllItems()
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun deleteItem(category: IngredientCategory, itemId: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                deletePantryItemUseCase(category, itemId)
                fetchAllItems()
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun fetchItemsByCategory(category: IngredientCategory) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = getItemsByCategoryUseCase(category)
                _categoryItems.value = result
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun fetchAllItems() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = getAllPantryItemsUseCase()
                _items.value = result
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun updateExpiry(category: IngredientCategory, itemId: String, expiryAt: Timestamp?) {
        _loading.value = true
        viewModelScope.launch {
            try {
                updateExpiryUseCase(category, itemId, expiryAt)
                fetchAllItems()
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }
}
