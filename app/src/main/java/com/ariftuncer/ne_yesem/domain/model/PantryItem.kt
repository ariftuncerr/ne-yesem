// domain/model/PantryItem.kt
package com.ne_yesem.domain.model

import com.google.firebase.Timestamp

enum class IngredientCategory(val docName: String) {
    VEGETABLES("vegetables"),
    GRAINS("grains"),
    MEAT_PROTEIN("meat_protein"),
    DAIRY("dairy")
}

enum class UnitType { ADET, GR, ML, LT }

data class PantryItem(
    val id: String = "",                      // Firestore doc id
    val name: String,
    val category: IngredientCategory,
    val qty: Int,
    val unit: UnitType,
    val expiryAt: Timestamp? = null,          // Son kullanma tarihi
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
