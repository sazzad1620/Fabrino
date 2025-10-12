package com.example.fabrinoproject

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    private lateinit var tvTotalPrice: TextView
    private lateinit var btnPlaceOrder: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Back arrow
        val backButton = findViewById<ImageView>(R.id.ivBack)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Total price and Place Order UI
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)

        // Place Order functionality
        btnPlaceOrder.setOnClickListener {
            placeOrder()
        }

        // RecyclerView setup with callback for total price update
        rvCart = findViewById(R.id.rvCart)
        rvCart.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(this, cartList) {
            updateTotalPrice() // Callback to update total price instantly
        }
        rvCart.adapter = adapter

        loadCartItems()
    }

    private fun loadCartItems() {
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid

        db.collection("users").document(uid).collection("cart")
            .get()
            .addOnSuccessListener { documents ->
                cartList.clear()
                for (doc in documents) {
                    val item = doc.toObject(CartItem::class.java)
                    item.documentId = doc.id
                    cartList.add(item)
                }
                adapter.notifyDataSetChanged()
                updateTotalPrice()
            }
            .addOnFailureListener { e ->
                Log.e("CartActivity", "Failed to load cart items", e)
            }
    }

    private fun updateTotalPrice() {
        val cartTotal = cartList.sumOf { it.productPrice * it.quantity }
        val deliveryCost = 80.0
        val totalWithDelivery = cartTotal + deliveryCost
        tvTotalPrice.text = " ৳${"%.2f".format(totalWithDelivery)}"
    }

    private fun placeOrder() {
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch user info
        db.collection("users").document(uid).get()
            .addOnSuccessListener { userDoc ->
                val userName = userDoc.getString("firstName") ?: "Unknown"
                val userEmail = currentUser.email ?: "unknown@example.com"

                // Generate transaction ID
                val transactionId = "TXN_" + System.currentTimeMillis()

                // Calculate total with delivery
                val deliveryCost = 80.0
                val totalPrice = cartList.sumOf { it.productPrice * it.quantity } + deliveryCost

                // Prepare transaction data
                val transactionData = hashMapOf(
                    "transactionId" to transactionId,
                    "userId" to uid,
                    "userName" to userName,
                    "userEmail" to userEmail,
                    "items" to cartList.map { item ->
                        hashMapOf(
                            "productName" to item.productName,
                            "productPrice" to item.productPrice,
                            "quantity" to item.quantity,
                            "size" to item.size
                        )
                    },
                    "totalAmount" to totalPrice,
                    "deliveryCost" to deliveryCost,
                    "status" to "Order Received",
                    "timestamp" to System.currentTimeMillis()
                )

                // Save to transactions collection
                db.collection("transactions")
                    .add(transactionData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()

                        // Clear cart after placing order
                        clearUserCart(uid)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
                        Log.e("CartActivity", "Error placing order", e)
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch user info", Toast.LENGTH_SHORT).show()
                Log.e("CartActivity", "Error fetching user info", e)
            }
    }

    private fun clearUserCart(uid: String) {
        val cartRef = db.collection("users").document(uid).collection("cart")
        cartRef.get().addOnSuccessListener { documents ->
            for (doc in documents) {
                doc.reference.delete()
            }
            cartList.clear()
            adapter.notifyDataSetChanged()
            updateTotalPrice()
        }
    }
}
