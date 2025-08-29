package com.ariftuncer.ne_yesem.domain.usecase.pantry

import com.ariftuncer.ne_yesem.domain.repository.PantryRepository
import com.google.firebase.Timestamp
import com.ariftuncer.ne_yesem.domain.model.IngredientCategory
import com.ariftuncer.ne_yesem.domain.model.PantryItem


class AddPantryItemUseCase(private val repo: PantryRepository) {
    suspend operator fun invoke(item: PantryItem) = repo.addItem(item)
}

class UpdatePantryItemUseCase(private val repo: PantryRepository) {
    suspend operator fun invoke(item: PantryItem) = repo.updateItem(item)
}

class DeletePantryItemUseCase(private val repo: PantryRepository) {
    suspend operator fun invoke(cat: IngredientCategory, id: String) = repo.deleteItem(cat, id)
}

class GetItemsByCategoryUseCase(private val repo: PantryRepository) {
    suspend operator fun invoke(cat: IngredientCategory) = repo.getItemsByCategory(cat)
}

class GetAllPantryItemsUseCase(private val repo: PantryRepository) {
    suspend operator fun invoke() = repo.getAllItems()
}

class UpdateExpiryUseCase(private val repo: PantryRepository) {
    suspend operator fun invoke(cat: IngredientCategory, id: String, expiry: Timestamp?) =
        repo.updateExpiry(cat, id, expiry)
}
