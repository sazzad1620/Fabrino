package com.example.fabrinoproject

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartActivity : AppCompatActivity() {

    private lateinit var rvCart: RecyclerView
    private lateinit var adapter: CartAdapter
    private val cartList = mutableListOf<CartItem>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Back arrow
        val backButton = findViewById<ImageView>(R.id.ivBack)
        backButton.setOnClickListener {
            onBackPressed() // Go back to previous page
        }

        // RecyclerView setup
        rvCart = findViewById(R.id.rvCart)
        rvCart.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(this, cartList)
        rvCart.adapter = adapter

        loadCartItems()
    }

    fun loadCartItems() {
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid

        db.collection("users").document(uid).collection("cart")
            .get()
            .addOnSuccessListener { documents ->
                cartList.clear()
                for (doc in documents) {
                    val item = doc.toObject(CartItem::class.java)
                    // Save Firestore document ID for proper updates/deletes
                    item.documentId = doc.id
                    cartList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("CartActivity", "Failed to load cart items", e)
            }
    }
}
