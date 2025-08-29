package com.ariftuncer.ne_yesem.domain.model

data class FavoriteRecipeCard(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int? = null,
    val likes: Int? = null,
    val dishTypes: List<String>? = null
)
