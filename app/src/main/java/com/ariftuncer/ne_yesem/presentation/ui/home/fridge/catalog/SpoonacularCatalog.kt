package com.ariftuncer.ne_yesem.presentation.ui.home.fridge.catalog

import com.ariftuncer.ne_yesem.domain.model.IngredientCategory
import java.util.Locale

object SpoonacularCatalog {
    val units: List<String> = listOf(
        "mg","milligram","milligrams","g","gram","grams","kg","kilogram","kilograms",
        "ml","milliliter","milliliters","l","liter","liters",
        "tsp","teaspoon","teaspoons","tbsp","tablespoon","tablespoons",
        "cup","cups","pt","pint","pints","qt","quart","quarts","gal","gallon","gallons",
        "oz","ounce","ounces","lb","pound","pounds",
        "piece","pieces","slice","slices","clove","cloves","stick","sticks",
        "bunch","bunches","head","heads","sprig","sprigs","leaf","leaves",
        "can","cans","jar","jars","bottle","bottles","bag","bags",
        "package","packages","packet","packets","box","boxes",
        "drop","drops","pinch","pinches","dash","dashes","serving","servings"
    )

    private val aisleToCategory = mapOf(
        "Produce" to IngredientCategory.VEGETABLES,
        "Spices and Seasonings" to IngredientCategory.VEGETABLES,
        "Dried Fruits" to IngredientCategory.VEGETABLES,
        "Herbs" to IngredientCategory.VEGETABLES,

        "Baking" to IngredientCategory.GRAINS,
        "Pasta and Rice" to IngredientCategory.GRAINS,
        "Cereal" to IngredientCategory.GRAINS,
        "Bakery/Bread" to IngredientCategory.GRAINS,
        "Grains" to IngredientCategory.GRAINS,

        "Meat" to IngredientCategory.MEAT_PROTEIN,
        "Seafood" to IngredientCategory.MEAT_PROTEIN,
        "Canned and Jarred" to IngredientCategory.MEAT_PROTEIN,
        "Nuts" to IngredientCategory.MEAT_PROTEIN,
        "Deli" to IngredientCategory.MEAT_PROTEIN,
        "Refrigerated" to IngredientCategory.MEAT_PROTEIN,

        "Dairy" to IngredientCategory.DAIRY,
        "Cheese" to IngredientCategory.DAIRY,
        "Frozen" to IngredientCategory.DAIRY
    )

    private val ingredientsByAisle: Map<String, List<String>> = mapOf(
        "Produce" to listOf("tomato","cucumber","bell pepper","onion","garlic","potato","carrot","zucchini","eggplant",
            "mushrooms","spinach","lettuce","arugula","parsley","dill","cilantro","green onion","leek",
            "broccoli","cauliflower","cabbage","celery","beet","radish","asparagus","avocado","ginger",
            "chili pepper","lime","lemon","orange","apple","banana","strawberries","blueberries","grapes",
            "pear","peach","plum","pomegranate","mint","rosemary","thyme","basil","bay leaf"),
        "Spices and Seasonings" to listOf("salt","black pepper","red pepper flakes","paprika","cumin","turmeric","coriander",
            "cinnamon","nutmeg","clove","cardamom","oregano","chili powder","garam masala","curry powder","sumac",
            "vanilla extract","bay leaf","thyme","rosemary"),
        "Baking" to listOf("all purpose flour","whole wheat flour","baking powder","baking soda","granulated sugar",
            "brown sugar","powdered sugar","yeast","cocoa powder","cornstarch","oats","vanilla sugar"),
        "Pasta and Rice" to listOf("spaghetti","penne","fusilli","linguine","macaroni","rice","basmati rice","jasmine rice",
            "bulgur","quinoa","couscous","noodles","lasagna sheets"),
        "Cereal" to listOf("rolled oats","corn flakes","granola","muesli"),
        "Bakery/Bread" to listOf("bread","baguette","tortilla","pita","buns","lavash"),
        "Meat" to listOf("chicken breast","chicken thighs","ground beef","beef steak","turkey breast","lamb","pork loin","salami","bacon","sausage"),
        "Seafood" to listOf("salmon","tuna","shrimp","prawn","cod","sea bass","mackerel","anchovy","squid","octopus"),
        "Canned and Jarred" to listOf("canned tomatoes","tomato paste","tomato sauce","canned corn","canned peas","canned beans",
            "kidney beans","chickpeas","black beans","canned tuna","olives","pickles","capers","artichoke hearts","coconut milk","chicken broth"),
        "Nuts" to listOf("almonds","walnuts","hazelnuts","peanuts","pistachios","cashews","sunflower seeds","pumpkin seeds"),
        "Deli" to listOf("ham","prosciutto","mortadella","turkey slices","smoked chicken"),
        "Refrigerated" to listOf("eggs","fresh pasta","fresh tortellini"),
        "Dairy" to listOf("milk","yogurt","butter","cream","heavy cream","sour cream","mozzarella","cheddar","feta","parmesan","labneh","kefir","ayran"),
        "Frozen" to listOf("frozen peas","frozen spinach","frozen mixed vegetables","ice cream"),
        "Oil, Vinegar, Salad Dressing" to listOf("olive oil","vegetable oil","sunflower oil","sesame oil","balsamic vinegar",
            "apple cider vinegar","white vinegar","soy sauce","mayonnaise","mustard","ketchup")
    )

    fun search(category: IngredientCategory?, query: String?): List<String> {
        val pool = if (category == null) ingredientsByAisle.values.flatten() else {
            val aisles = aisleToCategory.filterValues { it == category }.keys
            ingredientsByAisle.filterKeys { it in aisles }.values.flatten()
        }
        val q = query?.trim()?.lowercase(Locale.ROOT).orEmpty()
        return if (q.isEmpty()) pool else pool.filter { it.lowercase(Locale.ROOT).contains(q) }
    }

    fun resolveCategoryFor(name: String, fallback: IngredientCategory = IngredientCategory.VEGETABLES): IngredientCategory {
        val n = name.lowercase(Locale.ROOT)
        ingredientsByAisle.forEach { (aisle, list) ->
            if (list.any { it.equals(n, true) }) return aisleToCategory[aisle] ?: fallback
        }
        return fallback
    }

    fun normalizeUnit(input: String): String = when (input.trim().lowercase(Locale.ROOT)) {
        "milligram","milligrams" -> "mg"
        "gram","grams" -> "g"
        "kilogram","kilograms" -> "kg"
        "milliliter","milliliters" -> "ml"
        "liter","liters" -> "l"
        "teaspoon","teaspoons" -> "tsp"
        "tablespoon","tablespoons" -> "tbsp"
        "ounce","ounces" -> "oz"
        "pound","pounds" -> "lb"
        "pint","pints" -> "pt"
        "quart","quarts" -> "qt"
        "gallon","gallons" -> "gal"
        "pieces" -> "piece"
        "slices" -> "slice"
        "cloves" -> "clove"
        "sticks" -> "stick"
        "bunches" -> "bunch"
        "heads" -> "head"
        "sprigs" -> "sprig"
        "leaves" -> "leaf"
        "cans" -> "can"
        "jars" -> "jar"
        "bottles" -> "bottle"
        "bags" -> "bag"
        "packages" -> "package"
        "packets" -> "packet"
        "boxes" -> "box"
        "drops" -> "drop"
        "pinches" -> "pinch"
        "dashes" -> "dash"
        "servings" -> "serving"
        "cups" -> "cup"
        else -> input.trim().lowercase(Locale.ROOT)
    }
    // ─── SpoonacularCatalog.kt (dosyanın sonuna ek) ───────────────────────────────

    fun searchAll(): List<String> = ingredientsByAisle.values.flatten().distinct()

    fun tabTitleFor(cat: IngredientCategory): String = when (cat) {
        IngredientCategory.VEGETABLES   -> "🥬 Sebzeler"
        IngredientCategory.GRAINS       -> "🌾 Tahıllar"
        IngredientCategory.MEAT_PROTEIN -> "🥩 Et/Protein"
        IngredientCategory.DAIRY        -> "🧀 Süt/Şarküteri"
    }

    fun categoryFromTabTitle(title: String): IngredientCategory? {
        val t = title.trim()
        return when {
            t.endsWith("Sebzeler")      -> IngredientCategory.VEGETABLES
            t.endsWith("Tahıllar")      -> IngredientCategory.GRAINS
            t.endsWith("Et/Protein")    -> IngredientCategory.MEAT_PROTEIN
            t.endsWith("Süt/Şarküteri") -> IngredientCategory.DAIRY
            else -> null
        }
    }

    // SpoonacularCatalog.kt içine ekle
    fun suggestUnitFor(name: String): String? {
        val lower = name.lowercase()

        // İçecekler
        if (listOf("milk", "water", "juice", "cola", "wine", "oil").any { lower.contains(it) }) {
            return "ml"
        }

        // Kuru bakliyat / unlu mamul
        if (listOf("flour", "sugar", "rice", "salt", "beans", "lentil").any { lower.contains(it) }) {
            return "g"
        }

        // Et ve balık
        if (listOf("meat", "chicken", "beef", "fish", "lamb", "pork").any { lower.contains(it) }) {
            return "kg"
        }

        // Sebze ve meyveler
        if (listOf("apple", "banana", "tomato", "onion", "carrot", "pepper", "potato", "eggplant").any { lower.contains(it) }) {
            return "piece"
        }

        // Süt ürünleri
        if (listOf("cheese", "yogurt", "butter", "cream").any { lower.contains(it) }) {
            return "g"
        }

        // Yumurtalar
        if (lower.contains("egg")) {
            return "piece"
        }

        // Varsayılan — eğer eşleşme bulunmazsa
        return null
    }



}
