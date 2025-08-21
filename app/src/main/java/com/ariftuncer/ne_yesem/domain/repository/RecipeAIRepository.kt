// domain/repository/RecipeAIRepository.kt
package com.ne_yesem.domain.repository

import com.ne_yesem.domain.model.Recipe
import com.ne_yesem.domain.model.UnitType

data class PantryItemLite(
    val name: String,
    val quantity: Double,
    val unit: UnitType,
    val category: String? = null
)

data class GenerateParams(
    val pantry: List<PantryItemLite>,
    val dietType: String?,
    val allergens: List<String>,
    val dislikes: List<String>,
    val servings: Int? = null,
    val maxMinutes: Int? = null,
    val maxCalories: Int? = null
)

interface RecipeAIRepository {
    suspend fun generate(params: GenerateParams): String // şimdilik ham text dönecek
}
