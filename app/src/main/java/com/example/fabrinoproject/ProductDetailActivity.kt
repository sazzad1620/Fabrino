package com.example.fabrinoproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductDetailActivity : AppCompatActivity() {

    private var selectedSize: String? = null
    private lateinit var textViewUser: TextView
    private lateinit var userInfoLayout: LinearLayout
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Back arrow setup
        val topBarIcon = findViewById<ImageView>(R.id.ivHamburger)
        topBarIcon.setImageResource(R.drawable.ic_arrow_back)
        topBarIcon.setOnClickListener { finish() }

        // User info
        textViewUser = findViewById(R.id.textViewUser)
        userInfoLayout = findViewById(R.id.userInfoLayout)

        val firstNameFromIntent = intent.getStringExtra("firstName")
        if (!firstNameFromIntent.isNullOrEmpty()) {
            textViewUser.text = "Hi, $firstNameFromIntent"
        } else {
            fetchUserName()
        }

        userInfoLayout.setOnClickListener {
            if (auth.currentUser != null) {
                UserPopupHelper.showUserPopup(this, userInfoLayout)
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }

        // Product details
        val productName = intent.getStringExtra("ITEM_NAME")
        val productPrice = intent.getDoubleExtra("ITEM_PRICE", 0.0)
        val productImageUrl = intent.getStringExtra("ITEM_IMAGE_URL")
        val productSizes = intent.getStringArrayListExtra("ITEM_SIZES") ?: arrayListOf()
        val productDescription = intent.getStringExtra("ITEM_DESC") ?: ""

        val tvName: TextView = findViewById(R.id.tvProductName)
        val tvPrice: TextView = findViewById(R.id.tvProductPrice)
        val ivImage: ImageView = findViewById(R.id.ivProductImage)
        val sizeContainer: LinearLayout = findViewById(R.id.sizeContainer)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val addToCartButton: TextView = findViewById(R.id.navAddtoCart)

        tvName.text = productName
        tvPrice.text = "৳$productPrice"
        tvDescription.text = productDescription

        if (!productImageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(productImageUrl.trim())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(ivImage)
        }

        // Create size options dynamically
        productSizes.forEach { size ->
            val cardView = CardView(this).apply {
                radius = 16f
                cardElevation = 4f
                setCardBackgroundColor(ContextCompat.getColor(this@ProductDetailActivity, R.color.ash))
                useCompatPadding = true
            }

            val sizeTextView = TextView(this).apply {
                text = size
                setPadding(32, 16, 32, 16)
                setTextColor(ContextCompat.getColor(this@ProductDetailActivity, android.R.color.black))
                textSize = 16f
            }

            cardView.addView(sizeTextView)

            cardView.setOnClickListener {
                for (i in 0 until sizeContainer.childCount) {
                    val child = sizeContainer.getChildAt(i) as CardView
                    child.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ash))
                    (child.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(this, android.R.color.black))
                }

                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.black))
                sizeTextView.setTextColor(ContextCompat.getColor(this, R.color.ash))
                selectedSize = size
            }

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(12, 0, 12, 0)
            sizeContainer.addView(cardView, layoutParams)
        }

        addToCartButton.setOnClickListener {
            if (selectedSize == null) {
                Toast.makeText(this, "Please select a size first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Toast.makeText(this, "Please create an account first", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SignInActivity::class.java))
                return@setOnClickListener
            }

            val uid = currentUser.uid
            val userDocRef = db.collection("users").document(uid)
            val cartRef = userDocRef.collection("cart")

            userDocRef.get().addOnSuccessListener { userDoc ->
                val userName = userDoc.getString("firstName") ?: "Unknown"

                // Check if same product + size already exists
                val query = cartRef.whereEqualTo("productName", productName)
                    .whereEqualTo("size", selectedSize)
                    .limit(1)

                query.get().addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val doc = documents.documents[0]
                        val currentQty = doc.getLong("quantity") ?: 1
                        doc.reference.update("quantity", currentQty + 1)
                    } else {
                        val cartItem = hashMapOf(
                            "userName" to userName,
                            "productName" to productName,
                            "productPrice" to productPrice,
                            "productImageUrl" to productImageUrl,
                            "size" to selectedSize,
                            "quantity" to 1
                        )
                        cartRef.add(cartItem)
                    }

                    Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, CartActivity::class.java))
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                    Log.e("FirestoreError", "Error adding cart item", e)
                }

            }.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch user info", Toast.LENGTH_SHORT).show()
                Log.e("FirestoreError", "Error fetching user info", e)
            }
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
