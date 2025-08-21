// domain/model/Recipe.kt
package com.ne_yesem.domain.model

data class Recipe(
    val id: String,
    val title: String,
    val servings: Int,
    val totalMinutes: Int,
    val caloriesPerServing: Int?,
    val ingredients: List<RecipeIngredient>,
    val missing: List<RecipeIngredient>,
    val steps: List<String>,
    val tags: List<String>,
    val notes: String?
)

data class RecipeIngredient(
    val name: String,
    val quantity: Double,
    val unit: UnitType,
    val inPantry: Boolean? = null
)

enum class UnitType { ADET, GR, ML, LT }
