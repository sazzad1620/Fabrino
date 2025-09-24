package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmailSignUp: EditText
    private lateinit var etPasswordSignUp: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var tvGoToSignIn: TextView
    private lateinit var progressBar: ProgressBar

    private val db = Firebase.firestore

    // Fixed admin email to block from signup
    private val adminEmail = "admin@gmail.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmailSignUp = findViewById(R.id.etEmailSignUp)
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvGoToSignIn = findViewById(R.id.tvGoToSignIn)
        progressBar = findViewById(R.id.progressBar)

        btnSignUp.setOnClickListener { registerUser() }
        tvGoToSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = etEmailSignUp.text.toString().trim()
        val password = etPasswordSignUp.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (firstName.isEmpty()) {
            etFirstName.error = "First name required"
            return
        }
        if (lastName.isEmpty()) {
            etLastName.error = "Last name required"
            return
        }
        if (email.isEmpty()) {
            etEmailSignUp.error = "Email required"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailSignUp.error = "Invalid Email"
            return
        }
        // Block admin email from signup
        if (email == adminEmail) {
            etEmailSignUp.error = "Cannot use admin email"
            return
        }
        if (password.isEmpty() || password.length < 6) {
            etPasswordSignUp.error = "Password must be at least 6 characters"
            return
        }
        if (password != confirmPassword) {
            etConfirmPassword.error = "Passwords do not match"
            return
        }

        progressBar.visibility = ProgressBar.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Save first name and last name in Firestore
                    val uid = auth.currentUser?.uid ?: ""
                    val userMap = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email
                    )

                    db.collection("users").document(uid).set(userMap)
                        .addOnSuccessListener {
                            progressBar.visibility = ProgressBar.GONE
                            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_LONG).show()

                            // Directly go to Home/Main activity
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }

                } else {
                    progressBar.visibility = ProgressBar.GONE
                    Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
