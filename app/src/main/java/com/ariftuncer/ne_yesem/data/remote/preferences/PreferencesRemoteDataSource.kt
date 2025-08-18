package com.ariftuncer.ne_yesem.data.remote.preferences

import com.ariftuncer.ne_yesem.domain.model.Preferences
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRemoteDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {
    private fun doc(uid: String) =
        db.collection("users").document(uid)
            .collection("preferences")
            .document("preferences") // tek doküman

    suspend fun load(uid: String): Preferences? =
        doc(uid).get().await().toObject(Preferences::class.java)

    suspend fun save(uid: String, prefs: Preferences) {
        doc(uid).set(prefs).await()
    }
}
