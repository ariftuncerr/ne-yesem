package com.ariftuncer.ne_yesem.data.remote.dto

data class FindByIngredientsDto(
    val id: Int,
    val title: String,
    val image: String?,
    val likes: Int? = null,
    val missedIngredientCount: Int? = 0
)

