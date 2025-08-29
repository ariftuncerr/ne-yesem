package com.ariftuncer.ne_yesem.data.mapper

import com.ariftuncer.ne_yesem.data.remote.dto.RecipeInfoBriefDto
import com.ariftuncer.ne_yesem.domain.model.FavoriteRecipeCard

fun RecipeInfoBriefDto.toCard() = FavoriteRecipeCard(
    id = id,
    title = title,
    image = image ?: "",
    readyInMinutes = readyInMinutes,
    likes = aggregateLikes,
    dishTypes = dishTypes
)