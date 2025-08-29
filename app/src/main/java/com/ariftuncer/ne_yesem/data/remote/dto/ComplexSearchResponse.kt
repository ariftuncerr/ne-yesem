package com.ariftuncer.ne_yesem.data.remote.dto

data class ComplexSearchResponseDto(
    val results: List<ComplexRecipeDto> = emptyList(),
    val offset: Int? = null,
    val number: Int? = null,
    val totalResults: Int? = null
)

data class ComplexRecipeDto(
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int? = null,
    val aggregateLikes: Int? = null,
    val dishTypes: List<String>? = null
)
