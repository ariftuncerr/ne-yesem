package com.ariftuncer.ne_yesem.domain.repository

import com.ariftuncer.ne_yesem.domain.model.RecipeDetail
import com.ariftuncer.ne_yesem.domain.model.RecipeItem

interface RecipeRepository {
    suspend fun getRecipeDetail(id: Int): RecipeDetail
    suspend fun searchByIngredients(ingredients: List<String>): List<RecipeItem>
}
