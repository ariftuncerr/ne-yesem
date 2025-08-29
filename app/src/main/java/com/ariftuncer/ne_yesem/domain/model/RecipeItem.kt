package com.ariftuncer.ne_yesem.domain.model

data class RecipeItem(
    val id: Int,
    val title: String,
    val image: String?,
    val likes: Int,
    val missedCount: Int
)

