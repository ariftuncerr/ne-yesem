package com.ariftuncer.ne_yesem.domain.model

import com.google.firebase.Timestamp

enum class IngredientCategory(val docName: String) {
    VEGETABLES("vegetables"),
    GRAINS("grains"),
    MEAT_PROTEIN("meat_protein"),
    DAIRY("dairy")
}

data class PantryItem(
    val id: String = "",
    val name: String,
    val category: IngredientCategory,
    val qty: Int,
    val unit: UnitType,
    val expiryAt: Timestamp? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
enum class UnitType {
    ADET, KG, GRAM, LITER, MILLILITER
}