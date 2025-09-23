package com.example.fabrinoproject

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var hamburger: ImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView

    private val defaultSizes = listOf("S", "M", "L", "XL", "2XL")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        drawerLayout = findViewById(R.id.drawerLayout)
        hamburger = findViewById(R.id.ivHamburger)

        // Drawer toggle
        hamburger.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        if (supportFragmentManager.findFragmentById(R.id.sidebarFragment) == null) {
            val sidebarFragment = SidebarFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.sidebarFragment, sidebarFragment)
                .commit()
        }

        // Fetch items from Firestore
        fetchItemsFromFirestore()
    }

    private fun fetchItemsFromFirestore() {
        val db = Firebase.firestore

        db.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firestore", "Documents fetched: ${documents.size()}") // Debug

                val items = documents.map { doc ->
                    val priceDouble = doc.getDouble("price") ?: 0.0
                    val sizesFromDb = (doc.get("sizes") as? List<*>)?.map { it.toString() } ?: defaultSizes
                    Log.d("FirestoreItem", "Item: ${doc.getString("name")} Price: $priceDouble") // DEBUG: show each item

                    Item(
                        name = doc.getString("name") ?: "Unnamed",
                        price = priceDouble,
                        imageUrl = doc.getString("imageUrl") ?: "",
                        sizes = sizesFromDb,
                        description = doc.getString("description") ?: "No description"
                    )
                }

                recyclerView.adapter = ItemAdapter(items)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Failed to fetch items", e) // DEBUG: show any error
            }
    }



    fun closeSidebar() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
