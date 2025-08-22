package com.ariftuncer.ne_yesem.data.repository

import com.ariftuncer.ne_yesem.data.mapper.toDomain
import com.ariftuncer.ne_yesem.data.remote.api.SpoonApi
import com.ariftuncer.ne_yesem.domain.model.RecipeItem
import com.ariftuncer.ne_yesem.domain.repository.RecipeRepository
import javax.inject.Inject

// data/repository/RecipeRepositoryImpl.kt
class RecipeRepositoryImpl @Inject constructor(
    private val api: SpoonApi
) : RecipeRepository {
    override suspend fun searchByIngredients(ingredients: List<String>): List<RecipeItem> =
        api.findByIngredients(ingredients.joinToString(",")).map { it.toDomain() }
}

