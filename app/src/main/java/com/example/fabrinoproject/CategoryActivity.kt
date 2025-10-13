package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    private lateinit var textViewUser: TextView
    private lateinit var userInfoLayout: LinearLayout
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Category"

        // Top bar back arrow
        val topBarIcon = findViewById<ImageView>(R.id.ivHamburger)
        topBarIcon.setImageResource(R.drawable.ic_arrow_back)
        topBarIcon.setOnClickListener { finish() }

        textViewUser = findViewById(R.id.textViewUser)
        userInfoLayout = findViewById(R.id.userInfoLayout)

        // Fetch and show user name
        val firstNameFromIntent = intent.getStringExtra("firstName")
        if (!firstNameFromIntent.isNullOrEmpty()) {
            textViewUser.text = "Hi, $firstNameFromIntent"
        } else {
            fetchUserName()
        }

        // User info popup using reusable helper
        userInfoLayout.setOnClickListener {
            if (auth.currentUser != null) {
                UserPopupHelper.showUserPopup(this, userInfoLayout)
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }

        // Recycler setup
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategory)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        lifecycleScope.launch {
            val items = ItemRepository.getItemsByCategory(categoryName)
            recyclerView.adapter = ItemAdapter(items)
        }

        // ✅ Setup bottom bar correctly
        val bottomBar = findViewById<LinearLayout>(R.id.bottomBar)
        if (bottomBar != null) {
            val isLoggedIn = auth.currentUser != null
            BottomBarHelper.setupBottomBar(this, bottomBar, isLoggedIn)
        } else {
            Log.e("CategoryActivity", "Bottom bar not found!")
        }
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
}