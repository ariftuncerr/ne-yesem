package com.ariftuncer.ne_yesem.domain.model

data class Preferences(
    val diet: List<String> = emptyList(),
    val allergens: List<String> = emptyList(),
    val unlikedFoods: List<String> = emptyList(),
)
