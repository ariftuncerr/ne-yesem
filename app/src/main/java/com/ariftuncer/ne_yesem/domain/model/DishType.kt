package com.ariftuncer.ne_yesem.domain.model
enum class DishType(val apiName: String, val displayNameTr: String) {
    MAIN_COURSE("main course", "Ana Yemek"),
    SIDE_DISH("side dish", "Ara Sıcak"),
    DESSERT("dessert", "Tatlı"),
    APPETIZER("appetizer", "Başlangıç"),
    SALAD("salad", "Salata"),
    BREAD("bread", "Ekmek"),
    BREAKFAST("breakfast", "Kahvaltı"),
    SOUP("soup", "Çorba"),
    BEVERAGE("beverage", "İçecek"),
    SAUCE("sauce", "Sos"),
    MARINADE("marinade", "Marine"),
    FINGERFOOD("fingerfood", "Atıştırmalık"),
    SNACK("snack", "Atıştırmalık 2"),
    DRINK("drink", "İçecek 2");
}
