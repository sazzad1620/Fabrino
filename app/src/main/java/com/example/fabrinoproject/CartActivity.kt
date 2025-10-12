package com.example.fabrinoproject

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
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

        btnPlaceOrder.setOnClickListener {
            // TODO: Implement placing order logic
            // Example: Move items to "orders" collection and clear "cart"
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

}
