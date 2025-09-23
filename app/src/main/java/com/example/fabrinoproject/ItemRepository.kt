package com.example.fabrinoproject

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object ItemRepository {

    private val db = FirebaseFirestore.getInstance()

    // Suspend function to get items by category
    suspend fun getItemsByCategory(categoryName: String): List<Item> {
        return try {
            val snapshot = db.collection("items")
                .whereEqualTo("category", categoryName)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Item::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Suspend function to get item by name
    suspend fun getItemByName(name: String): Item? {
        return try {
            val snapshot = db.collection("items")
                .whereEqualTo("name", name)
                .get()
                .await()

            snapshot.documents.firstOrNull()?.toObject(Item::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
