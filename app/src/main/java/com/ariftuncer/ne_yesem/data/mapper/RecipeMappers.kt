package com.ariftuncer.ne_yesem.data.mapper

import com.ariftuncer.ne_yesem.data.remote.dto.FindByIngredientsDto
import com.ariftuncer.ne_yesem.domain.model.RecipeItem

fun FindByIngredientsDto.toDomain() = RecipeItem(
    id = id,
    title = title,
    image = image,
    likes = likes ?: 0,
    missedCount = missedIngredientCount ?: 0
)
