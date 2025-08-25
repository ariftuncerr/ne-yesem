package com.ariftuncer.ne_yesem.data.repository

import com.ariftuncer.ne_yesem.data.mapper.toDomain
import com.ariftuncer.ne_yesem.data.remote.api.SpoonApi
import com.ariftuncer.ne_yesem.data.remote.mappers.toDomain
import com.ariftuncer.ne_yesem.domain.model.DishTypeRecipe
import com.ariftuncer.ne_yesem.domain.model.RecipeDetail
import com.ariftuncer.ne_yesem.domain.model.RecipeItem
import com.ariftuncer.ne_yesem.domain.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: SpoonApi
) : RecipeRepository {
    override suspend fun searchByIngredients(ingredients: List<String>): List<RecipeItem> =
        api.findByIngredients(ingredients.joinToString(",")).map { it.toDomain() }

    override suspend fun getRecipeDetail(id: Int): RecipeDetail {
        return api.getRecipeDetail(id, includeNutrition = true).toDomain()

    }
    override suspend fun searchByDishType(dishType: String, number: Int): List<DishTypeRecipe> {
        val res = api.searchByDishType(dishType, number)
        return res.results.map {
            DishTypeRecipe(
                id = it.id,
                title = it.title,
                image = it.image ?: "",
                readyInMinutes = it.readyInMinutes,
                likes = it.aggregateLikes,
                dishTypes = it.dishTypes
            )
        }
    }
}

