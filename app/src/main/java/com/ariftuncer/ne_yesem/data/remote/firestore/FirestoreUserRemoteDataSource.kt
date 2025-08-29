package com.ariftuncer.ne_yesem.data.remote.firestore

import com.ariftuncer.ne_yesem.core.result.AppError
import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.core.result.Either
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ne_yesem.domain.model.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun ensureUserDocument(profile: UserProfile): AppResult<Unit> =
        runCatching {
            val ref = db.collection("users").document(profile.uid)
            val snap = ref.get().await()
            if (!snap.exists()) {
                ref.set(
                    mapOf(
                        "uid" to profile.uid,
                        "name" to profile.name,
                        "phoneNumber" to profile.phoneNumber,
                        "email" to profile.email, // email eklendi
                    )
                ).await()
            }
            Either.Right(Unit)
        }.getOrElse { Either.Left(AppError.Network(it.message)) }

    override suspend fun fetchUserProfile(uid: String): AppResult<UserProfile?> =
        runCatching {
            val snap = db.collection("users").document(uid).get().await()
            if (!snap.exists()) return Either.Right(null)
            val data = snap.data!!
            Either.Right(
                UserProfile(
                    uid = uid,
                    name = data["name"] as? String,
                    phoneNumber = data["phoneNumber"] as? String,
                    email = (data["email"] as? String).toString()
                )
            )
        }.getOrElse { Either.Left(AppError.Network(it.message)) }

    override suspend fun addFavorite(uid: String, recipeId: Int) {
        db.collection("users").document(uid)
            .collection("favorites")
            .document(recipeId.toString())
            .set(mapOf("id" to recipeId, "createdAt" to FieldValue.serverTimestamp()))
            .await()
    }

    override suspend fun removeFavorite(uid: String, recipeId: Int) {
        db.collection("users").document(uid)
            .collection("favorites")
            .document(recipeId.toString())
            .delete()
            .await()
    }

    override suspend fun getFavoriteIds(uid: String): List<Int> {
        val snaps = db.collection("users").document(uid)
            .collection("favorites").get().await()
        return snaps.documents.mapNotNull { it.id.toIntOrNull() }
    }

    override suspend fun addLastView(uid: String, recipeId: Int) {
        db.collection("users").document(uid)
            .collection("lastViews")
            .document(recipeId.toString())
            .set(mapOf("id" to recipeId, "createdAt" to FieldValue.serverTimestamp()))
            .await()
    }

    override suspend fun getLastViewedIds(uid: String): List<Int> {
        val snaps = db.collection("users").document(uid)
            .collection("lastViews").orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(10)
            .get().await()
        return snaps.documents.mapNotNull { it.getLong("id")?.toInt() }
    }
}
