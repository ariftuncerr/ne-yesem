package com.ariftuncer.ne_yesem.data.remote.api
import RecipeDetailDto
import com.ariftuncer.ne_yesem.BuildConfig
import com.ariftuncer.ne_yesem.data.remote.dto.FindByIngredientsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonApi {
    @GET("recipes/findByIngredients")
    suspend fun findByIngredients(
        @Query("ingredients") ingredientsCommaSeparated: String,
        @Query("number") number: Int = 10,
        @Query("ranking") ranking: Int = 2,
        @Query("ignorePantry") ignorePantry: Boolean = true
    ): List<FindByIngredientsDto>

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetail(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = true,
    ): RecipeDetailDto
}
