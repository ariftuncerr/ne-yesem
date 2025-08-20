package com.ariftuncer.ne_yesem.domain.repository

import com.ne_yesem.domain.model.IngredientCategory
import com.ne_yesem.domain.model.PantryItem
import com.google.firebase.Timestamp

interface PantryRepository {
    fun currentUid(): String?                               // giriş yapan kullanıcı

    suspend fun addItem(item: PantryItem): PantryItem      // ekle
    suspend fun updateItem(item: PantryItem): PantryItem   // tüm alanlar (qty, unit, name, expiryAt) güncellenebilir
    suspend fun deleteItem(category: IngredientCategory, itemId: String)

    suspend fun getItemsByCategory(category: IngredientCategory): List<PantryItem>
    suspend fun getAllItems(): List<PantryItem>

    suspend fun updateExpiry(
        category: IngredientCategory,
        itemId: String,
        expiryAt: Timestamp?
    ): Boolean
}
