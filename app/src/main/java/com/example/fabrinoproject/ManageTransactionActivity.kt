package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ManageTransactionActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var ivHamburger: ImageView
    private lateinit var textViewUser: TextView
    private lateinit var userInfoLayout: LinearLayout

    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val adminEmail = "admin@gmail.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_transaction)

        // --- Drawer setup ---
        drawerLayout = findViewById(R.id.drawerLayoutAdmin)
        ivHamburger = findViewById(R.id.ivHamburger)

        ivHamburger.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Load Sidebar Fragment
        if (supportFragmentManager.findFragmentById(R.id.adminSidebarFragment) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.adminSidebarFragment, AdminSidebarFragment())
                .commit()
        }

        // --- Top Bar setup ---
        textViewUser = findViewById(R.id.textViewUser)
        userInfoLayout = findViewById(R.id.userInfoLayout)

        if (auth.currentUser != null) {
            if (auth.currentUser?.email == adminEmail) {
                textViewUser.text = "Hi, Admin"
            } else {
                fetchUserName()
            }
        }

        userInfoLayout.setOnClickListener {
            if (auth.currentUser != null) {
                showUserInfoPopup()
            }
        }
    }

    // --- Firebase user info ---
    private fun fetchUserName() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val firstName = doc.getString("firstName") ?: "Admin"
                textViewUser.text = "Hi, $firstName"
            }
            .addOnFailureListener {
                textViewUser.text = "Hi, Admin"
            }
    }

    // --- User info popup ---
    private fun showUserInfoPopup() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_user_info, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val tvFullName = popupView.findViewById<TextView>(R.id.tvFullName)
        val btnLogout = popupView.findViewById<Button>(R.id.btnLogout)

        val currentUser = auth.currentUser
        if (currentUser?.email == adminEmail) {
            tvFullName.text = "Admin"
        } else {
            val uid = currentUser?.uid
            if (uid != null) {
                db.collection("users").document(uid).get()
                    .addOnSuccessListener { doc ->
                        val firstName = doc.getString("firstName") ?: ""
                        val lastName = doc.getString("lastName") ?: ""
                        tvFullName.text = "$firstName $lastName"
                    }
            }
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        popupWindow.showAsDropDown(userInfoLayout, 0, 0, Gravity.START)
    }

    // --- Drawer helper ---
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
