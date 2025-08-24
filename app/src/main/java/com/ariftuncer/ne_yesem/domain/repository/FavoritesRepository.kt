package com.ariftuncer.ne_yesem.domain.repository
import com.ariftuncer.ne_yesem.domain.model.FavoriteRecipeCard

interface FavoritesRepository {
    suspend fun add(uid: String, recipeId: Int)
    suspend fun remove(uid: String, recipeId: Int)
    suspend fun getIds(uid: String): List<Int>
    suspend fun getCards(uid: String): List<FavoriteRecipeCard>
}
