package com.ariftuncer.ne_yesem.data.repository

import com.ariftuncer.ne_yesem.domain.repository.PantryRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ne_yesem.domain.model.IngredientCategory
import com.ne_yesem.domain.model.PantryItem
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PantryRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : PantryRepository {

    override fun currentUid(): String? = auth.currentUser?.uid

    private fun catDoc(uid: String, cat: IngredientCategory) =
        db.collection("users").document(uid)
            .collection("fridge").document(cat.docName)

    private fun itemDoc(uid: String, cat: IngredientCategory, itemId: String) =
        catDoc(uid, cat).collection("items").document(itemId)

    override suspend fun addItem(item: PantryItem): PantryItem {
        val uid = currentUid() ?: throw IllegalStateException("No user")
        // Kategori dökümanı yoksa yarat (metadata istersen buraya eklenebilir)
        catDoc(uid, item.category).set(mapOf("exists" to true), SetOptions.merge()).await()

        val now = Timestamp.now()
        val data = hashMapOf(
            "name" to item.name,
            "category" to item.category.docName,
            "qty" to item.qty,
            "unit" to item.unit.name,
            "expiryAt" to item.expiryAt,
            "createdAt" to now,
            "updatedAt" to now
        )

        val ref = catDoc(uid, item.category).collection("items").document()
        ref.set(data).await()

        return item.copy(id = ref.id, createdAt = now, updatedAt = now)
    }

    override suspend fun updateItem(item: PantryItem): PantryItem {
        val uid = currentUid() ?: throw IllegalStateException("No user")
        require(item.id.isNotBlank()) { "item.id boş olamaz" }

        val patch = hashMapOf(
            "name" to item.name,
            "qty" to item.qty,
            "unit" to item.unit.name,
            "expiryAt" to item.expiryAt,
            "updatedAt" to Timestamp.now()
        )

        itemDoc(uid, item.category, item.id).set(patch, SetOptions.merge()).await()
        val snap = itemDoc(uid, item.category, item.id).get().await()
        val updatedAt = snap.getTimestamp("updatedAt")
        return item.copy(updatedAt = updatedAt)
    }

    override suspend fun deleteItem(category: IngredientCategory, itemId: String) {
        val uid = currentUid() ?: throw IllegalStateException("No user")
        itemDoc(uid, category, itemId).delete().await()
    }

    override suspend fun getItemsByCategory(category: IngredientCategory): List<PantryItem> {
        val uid = currentUid() ?: throw IllegalStateException("No user")
        val qs = catDoc(uid, category).collection("items").get().await()
        return qs.documents.map { d ->
            PantryItem(
                id = d.id,
                name = d.getString("name") ?: "",
                category = category,
                qty = d.getLong("qty")?.toInt() ?: 0,
                unit = runCatching { com.ne_yesem.domain.model.UnitType.valueOf(d.getString("unit") ?: "ADET") }
                    .getOrDefault(com.ne_yesem.domain.model.UnitType.ADET),
                expiryAt = d.getTimestamp("expiryAt"),
                createdAt = d.getTimestamp("createdAt"),
                updatedAt = d.getTimestamp("updatedAt")
            )
        }
    }

    override suspend fun getAllItems(): List<PantryItem> {
        // Tüm kategorileri sırayla çek ve birleştir
        val cats = IngredientCategory.values()
        return cats.flatMap { getItemsByCategory(it) }
    }

    override suspend fun updateExpiry(
        category: IngredientCategory,
        itemId: String,
        expiryAt: Timestamp?
    ): Boolean {
        val uid = currentUid() ?: throw IllegalStateException("No user")
        val patch = mapOf(
            "expiryAt" to expiryAt,
            "updatedAt" to Timestamp.now()
        )
        itemDoc(uid, category, itemId).set(patch, SetOptions.merge()).await()
        return true
    }
}
