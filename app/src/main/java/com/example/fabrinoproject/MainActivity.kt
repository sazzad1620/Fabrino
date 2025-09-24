package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
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

    private val defaultSizes = listOf("S", "M", "L", "XL", "2XL")
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

        if (supportFragmentManager.findFragmentById(R.id.sidebarFragment) == null) {
            val sidebarFragment = SidebarFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.sidebarFragment, sidebarFragment)
                .commit()
        }

        // Check if firstName is passed via Intent
        val firstNameFromIntent = intent.getStringExtra("firstName")
        if (!firstNameFromIntent.isNullOrEmpty()) {
            textViewUser.text = "Hi, $firstNameFromIntent"
        } else {
            // Fetch user's first name from Firestore if not passed
            fetchUserName()
        }

        // Click on user info layout to show popup
        userInfoLayout.setOnClickListener {
            if (auth.currentUser != null) {
                showUserInfoPopup()
            } else {
                // If guest, go to sign in
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }

        // Fetch items from Firestore
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

    private fun showUserInfoPopup() {
        val inflater = LayoutInflater.from(this)
        val popupView: View = inflater.inflate(R.layout.popup_user_info, null)

        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        // Fetch full name from Firestore
        val uid = auth.currentUser?.uid
        val tvFullName = popupView.findViewById<TextView>(R.id.tvFullName)
        val btnLogout = popupView.findViewById<Button>(R.id.btnLogout)

        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        tvFullName.text = "$firstName $lastName"
                    }
                }
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish() // CloseActivity after logout
        }

        // Show popup below the user info layout
        popupWindow.showAsDropDown(userInfoLayout, 0, 0, Gravity.START)
    }

    private fun fetchItemsFromFirestore() {
        db.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firestore", "Documents fetched: ${documents.size()}") // Debug

                val items = documents.map { doc ->
                    val priceDouble = doc.getDouble("price") ?: 0.0
                    val sizesFromDb = (doc.get("sizes") as? List<*>)?.map { it.toString() } ?: defaultSizes
                    Log.d("FirestoreItem", "Item: ${doc.getString("name")} Price: $priceDouble") // DEBUG

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
