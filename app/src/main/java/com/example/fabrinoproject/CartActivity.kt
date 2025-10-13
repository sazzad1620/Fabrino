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
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject

class CartActivity : AppCompatActivity() {

    private lateinit var rvCart: RecyclerView
    private lateinit var adapter: CartAdapter
    private val cartList = mutableListOf<CartItem>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var tvTotalPrice: TextView
    private lateinit var btnPlaceOrder: TextView

    private var cartListener: ListenerRegistration? = null

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

        btnPlaceOrder.setOnClickListener {
            placeOrder()
        }

        // RecyclerView setup
        rvCart = findViewById(R.id.rvCart)
        rvCart.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(this, cartList) { updateTotalPrice() }
        rvCart.adapter = adapter

        listenToCartUpdates() // Use real-time listener
    }

    private fun listenToCartUpdates() {
        val currentUser = auth.currentUser ?: return
        val uid = currentUser.uid

        cartListener = db.collection("users").document(uid).collection("cart")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("CartActivity", "Listen failed.", e)
                    return@addSnapshotListener
                }

                cartList.clear()
                snapshots?.forEach { doc ->
                    val item = doc.toObject<CartItem>()
                    item.documentId = doc.id
                    cartList.add(item)
                }
                adapter.notifyDataSetChanged()
                updateTotalPrice()
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

        db.collection("users").document(uid).get()
            .addOnSuccessListener { userDoc ->
                val firstName = userDoc.getString("firstName") ?: ""
                val lastName = userDoc.getString("lastName") ?: ""
                val userName = "$firstName $lastName".trim()
                val userEmail = currentUser.email ?: "unknown@example.com"

                val transactionId = "TXN_" + System.currentTimeMillis()
                val deliveryCost = 80.0
                val totalPrice = cartList.sumOf { it.productPrice * it.quantity } + deliveryCost

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

                db.collection("transactions")
                    .add(transactionData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cartListener?.remove() // Remove listener to avoid memory leaks
    }
}
