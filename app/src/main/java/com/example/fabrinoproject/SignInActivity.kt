package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var tvGoToSignUp: TextView
    private lateinit var progressBar: ProgressBar

    private val db = Firebase.firestore

    // Fixed admin credentials
    private val adminEmail = "admin@gmail.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        tvGoToSignUp = findViewById(R.id.tvGoToSignUp)
        progressBar = findViewById(R.id.progressBar)

        // Check if already logged in
        checkLoggedInUser()

        btnSignIn.setOnClickListener { signInUser() }
        tvGoToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        val backButton = findViewById<ImageView>(R.id.ivBack)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Close SignInActivity
        }
    }

    private fun checkLoggedInUser() {
        auth.currentUser?.let { currentUser ->
            val email = currentUser.email
            if (email == adminEmail) {
                goToAdmin()
            } else {
                fetchUserAndGoToMain(currentUser.uid)
            }
        }
    }

    private fun signInUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Email required"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Invalid Email"
            return
        }
        if (password.isEmpty() || password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            return
        }

        progressBar.visibility = ProgressBar.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = ProgressBar.GONE
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        if (currentUser.email == adminEmail) {
                            goToAdmin()
                        } else {
                            fetchUserAndGoToMain(currentUser.uid)
                        }
                    } else {
                        Toast.makeText(this, "Login Failed: User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchUserAndGoToMain(uid: String) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val firstName = document?.getString("firstName") ?: ""
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("firstName", firstName)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    private fun goToAdmin() {
        val intent = Intent(this, AdminActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
