package com.ariftuncer.ne_yesem.presentation.ui.home.fridge.catalog

import com.ariftuncer.ne_yesem.domain.model.UnitType

object UnitMapper {
    fun toDomainUnit(normalized: String): UnitType = when (normalized.lowercase()) {
        "kg" -> UnitType.KG
        "g","mg","oz","lb" -> UnitType.GRAM           // kütleyi GRAM’e indiriyoruz
        "l" -> UnitType.LITER
        "ml","pt","qt","gal" -> UnitType.MILLILITER   // hacmi ML’e indiriyoruz
        else -> UnitType.ADET                         // teaspoon, cup, piece, can, … → ADET
    }
}
