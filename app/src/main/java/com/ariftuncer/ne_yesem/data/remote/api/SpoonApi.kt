package com.ariftuncer.ne_yesem.data.remote.api
import RecipeDetailDto
import com.ariftuncer.ne_yesem.BuildConfig
import com.ariftuncer.ne_yesem.data.remote.dto.ComplexSearchResponseDto
import com.ariftuncer.ne_yesem.data.remote.dto.FindByIngredientsDto
import com.ariftuncer.ne_yesem.data.remote.dto.RecipeInfoBriefDto
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

    @GET("recipes/complexSearch")
    suspend fun searchByDishType(
        @Query("type") dishType: String,
        @Query("number") number: Int = 20,
        @Query("addRecipeInformation") addInfo: Boolean = true
    ): ComplexSearchResponseDto

    @GET("recipes/informationBulk")
    suspend fun getRecipeInformationBulk(
        @Query("ids") ids: String,
        @Query("includeNutrition") includeNutrition: Boolean = false
    ): List<RecipeInfoBriefDto>

}
