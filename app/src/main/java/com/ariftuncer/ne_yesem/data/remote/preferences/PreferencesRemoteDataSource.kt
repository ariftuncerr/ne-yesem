// data/remote/preferences/PreferencesRemoteDataSource.kt
package com.ariftuncer.ne_yesem.data.remote.preferences

import com.ariftuncer.ne_yesem.domain.model.Preferences
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PreferencesRemoteDataSource(
    private val db: FirebaseFirestore
) {
    // users/{uid}/preferences/preferences  → tek doküman
    private fun doc(uid: String) =
        db.collection("users").document(uid)
            .collection("preferences").document("preferences")

    suspend fun load(uid: String): Preferences? =
        doc(uid).get().await().toObject(Preferences::class.java)

    suspend fun save(uid: String, prefs: Preferences) {
        doc(uid).set(prefs).await()
    }
}
