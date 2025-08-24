package com.ariftuncer.ne_yesem.domain.model

data class DishTypeRecipe(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int? = null,
    val likes: Int? = null,
    val dishTypes: List<String>? = null
)
