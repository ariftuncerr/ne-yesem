package com.ariftuncer.ne_yesem.presentation.ui.home.favorite

enum class SortOption(val label: String) {
    NEWEST("En Son Eklenenler"),
    OLDEST("En Eski Eklenenler"),
    ALPHA_AZ("Alfabetik (A→Z)"),
    ALPHA_ZA("Alfabetik (Z→A)"),
    TIME_ASC("Hazırlama Süresi (Kısa→Uzun)"),
    TIME_DESC("Hazırlama Süresi (Uzun→Kısa)"),
    CAL_LOW_HIGH("Kalori (Düşük→Yüksek)"),
    CAL_HIGH_LOW("Kalori (Yüksek→Düşük)")
}