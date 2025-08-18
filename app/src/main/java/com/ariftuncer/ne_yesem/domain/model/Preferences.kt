package com.ariftuncer.ne_yesem.domain.model

data class Preferences(
    val diet : String? = null,
    val allergens: List<String> = emptyList(),
    val unlikedFoods: List<String> = emptyList(),
)
