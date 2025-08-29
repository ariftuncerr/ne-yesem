data class RecipeDetailDto(
    val id: Int,
    val title: String,
    val image: String?,
    val summary: String?,                 // HTML
    val readyInMinutes: Int?,
    val servings: Int?,
    val extendedIngredients: List<Ing>?,
    val analyzedInstructions: List<Instruction>?,
    val nutrition: Nutrition?
) {
    data class Ing(val original: String?)
    data class Instruction(val steps: List<Step>?)
    data class Step(val number: Int?, val step: String?)
    data class Nutrition(val nutrients: List<Nutrient>?)
    data class Nutrient(val name: String?, val amount: Double?, val unit: String?)
}
