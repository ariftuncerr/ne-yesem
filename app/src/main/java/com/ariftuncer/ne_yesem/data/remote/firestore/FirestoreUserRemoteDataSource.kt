// data/remote/firestore/FirestoreUserRemoteDataSource.kt
package com.ariftuncer.ne_yesem.data.remote.firestore

import com.ariftuncer.ne_yesem.core.result.AppError
import com.ariftuncer.ne_yesem.core.result.AppResult
import com.ariftuncer.ne_yesem.core.result.Either
import com.google.firebase.firestore.FirebaseFirestore
import com.ne_yesem.domain.model.UserProfile
import kotlinx.coroutines.tasks.await

class FirestoreUserRemoteDataSource(
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
                        "phoneNumber" to profile.phoneNumber
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
                    phoneNumber = data["phoneNumber"] as? String
                )
            )
        }.getOrElse { Either.Left(AppError.Network(it.message)) }
}
