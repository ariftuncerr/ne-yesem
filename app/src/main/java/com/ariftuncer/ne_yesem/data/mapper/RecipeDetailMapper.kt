package com.ariftuncer.ne_yesem.data.remote.mappers

import RecipeDetailDto
import androidx.core.text.HtmlCompat
import com.ariftuncer.ne_yesem.domain.model.RecipeDetail

fun RecipeDetailDto.toDomain(): RecipeDetail {
    val calories = nutrition?.nutrients?.firstOrNull { it.name.equals("Calories", true) }
    val caloriesTxt = calories?.let { amt ->
        val num = amt.amount?.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() } ?: return@let null
        "$num ${amt.unit ?: "kcal"}"
    }

    val ingredients = extendedIngredients?.mapNotNull { it.original?.trim() } ?: emptyList()
    val steps = analyzedInstructions?.firstOrNull()?.steps?.mapNotNull { it.step?.trim() } ?: emptyList()
    val plainSummary = HtmlCompat.fromHtml(summary ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    return RecipeDetail(
        id = id,
        title = title,
        image = image,
        summaryPlain = plainSummary,
        readyInMinutes = readyInMinutes,
        servings = servings,
        caloriesText = caloriesTxt,
        ingredients = ingredients,
        steps = steps
    )
}
