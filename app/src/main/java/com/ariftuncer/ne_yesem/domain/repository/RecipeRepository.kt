package com.ariftuncer.ne_yesem.domain.repository

import com.ariftuncer.ne_yesem.domain.model.RecipeItem

interface RecipeRepository {
    suspend fun searchByIngredients(ingredients: List<String>): List<RecipeItem>
}