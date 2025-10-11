package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var hamburger: ImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewUser: TextView
    private lateinit var userInfoLayout: LinearLayout

    private val defaultSizes = listOf("S", "M", "L", "XL", "2XL", "Free Size")
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        drawerLayout = findViewById(R.id.drawerLayout)
        hamburger = findViewById(R.id.ivHamburger)
        textViewUser = findViewById(R.id.textViewUser)
        userInfoLayout = findViewById(R.id.userInfoLayout)

        // Drawer toggle
        hamburger.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Sidebar fragment
        if (supportFragmentManager.findFragmentById(R.id.sidebarFragment) == null) {
            val sidebarFragment = SidebarFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.sidebarFragment, sidebarFragment)
                .commit()
        }

        // Show username
        val firstNameFromIntent = intent.getStringExtra("firstName")
        if (!firstNameFromIntent.isNullOrEmpty()) {
            textViewUser.text = "Hi, $firstNameFromIntent"
        } else {
            fetchUserName()
        }

        // Click user info layout → show reusable popup
        userInfoLayout.setOnClickListener {
            if (auth.currentUser != null) {
                UserPopupHelper.showUserPopup(this, userInfoLayout)
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }

        fetchItemsFromFirestore()
    }

    private fun fetchUserName() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        textViewUser.text = "Hi, $firstName"
                    } else {
                        textViewUser.text = "Hi, Guest"
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreError", "Failed to fetch user name", e)
                    textViewUser.text = "Hi, Guest"
                }
        } else {
            textViewUser.text = "Hi, Guest"
        }
    }

    private fun fetchItemsFromFirestore() {
        db.collection("items")
            .whereEqualTo("isPopular", true)
            .get()
            .addOnSuccessListener { documents ->
                val items = documents.map { doc ->
                    val priceDouble = doc.getDouble("price") ?: 0.0
                    val sizesFromDb = (doc.get("sizes") as? List<*>)?.map { it.toString() } ?: defaultSizes

                    Item(
                        name = doc.getString("name") ?: "Unnamed",
                        price = priceDouble,
                        imageUrl = doc.getString("imageUrl") ?: "",
                        sizes = sizesFromDb,
                        description = doc.getString("description") ?: "No description",
                        category = doc.getString("category") ?: "",
                        quantity = (doc.getLong("quantity") ?: 0L).toInt(),
                        isPopular = doc.getBoolean("isPopular") ?: false
                    )
                }

                recyclerView.adapter = ItemAdapter(items)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Failed to fetch items", e)
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
