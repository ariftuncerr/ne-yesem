package com.ariftuncer.ne_yesem.domain.model
data class RecipeDetail(
    val id: Int,
    val title: String,
    val image: String?,
    val summaryPlain: String,
    val readyInMinutes: Int?,
    val servings: Int?,
    val caloriesText: String?,              // ör: "520 kcal"
    val ingredients: List<String>,
    val steps: List<String>
)
