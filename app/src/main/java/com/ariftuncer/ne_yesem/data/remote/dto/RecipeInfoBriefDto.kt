package com.ariftuncer.ne_yesem.data.remote.dto

data class RecipeInfoBriefDto(
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int? = null,
    val aggregateLikes: Int? = null,
    val dishTypes: List<String>? = null
)
