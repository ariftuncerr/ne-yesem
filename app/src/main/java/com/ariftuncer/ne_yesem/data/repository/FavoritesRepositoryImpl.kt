package com.ariftuncer.ne_yesem.data.repository

import com.ariftuncer.ne_yesem.data.mapper.toCard
import com.ariftuncer.ne_yesem.data.remote.api.SpoonApi
import com.ariftuncer.ne_yesem.data.remote.dto.RecipeInfoBriefDto
import com.ariftuncer.ne_yesem.domain.model.FavoriteRecipeCard
import com.ariftuncer.ne_yesem.domain.repository.FavoritesRepository
import com.ariftuncer.ne_yesem.data.remote.firestore.UserRemoteDataSource
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val api: SpoonApi,
    private val userDs: UserRemoteDataSource
) : FavoritesRepository {

    override suspend fun add(uid: String, recipeId: Int) =
        userDs.addFavorite(uid, recipeId)

    override suspend fun remove(uid: String, recipeId: Int) =
        userDs.removeFavorite(uid, recipeId)

    override suspend fun getIds(uid: String): List<Int> =
        userDs.getFavoriteIds(uid)

    override suspend fun getCards(uid: String): List<FavoriteRecipeCard> {
        val ids = getIds(uid)
        if (ids.isEmpty()) return emptyList()

        // Spoonacular limitini güvene almak için parça parça isteyelim (<=100)
        val chunks = ids.chunked(100)
        val cards = mutableListOf<FavoriteRecipeCard>()
        for (chunk in chunks) {
            val brief = api.getRecipeInformationBulk(chunk.joinToString(","))
            cards += brief.map { it.toCard() }
        }
        return cards
    }
}

