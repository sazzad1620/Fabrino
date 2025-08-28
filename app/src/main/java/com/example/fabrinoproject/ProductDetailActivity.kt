package com.example.fabrinoproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class ProductDetailActivity : AppCompatActivity() {

    // track selected size
    private var selectedSize: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)


        val topBarIcon = findViewById<ImageView>(R.id.topBarIcon)
        topBarIcon.setImageResource(R.drawable.ic_arrow_back)
        topBarIcon.setOnClickListener { finish() }

        val productName = intent.getStringExtra("ITEM_NAME")
        val productPrice = intent.getStringExtra("ITEM_PRICE")
        val productImage = intent.getIntExtra("ITEM_IMAGE", 0)
        val productSizes = intent.getStringArrayListExtra("ITEM_SIZES") ?: arrayListOf()
        val productDescription = intent.getStringExtra("ITEM_DESC") ?: ""

        val tvName: TextView = findViewById(R.id.tvProductName)
        val tvPrice: TextView = findViewById(R.id.tvProductPrice)
        val ivImage: ImageView = findViewById(R.id.ivProductImage)
        val sizeContainer: LinearLayout = findViewById(R.id.sizeContainer)
        val tvDescription: TextView = findViewById(R.id.tvDescription)

        tvName.text = productName
        tvPrice.text = productPrice
        if (productImage != 0) ivImage.setImageResource(productImage)
        tvDescription.text = productDescription

        // product size card
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

            // OnClick Listener for selection
            cardView.setOnClickListener {
                // Reset all boxes
                for (i in 0 until sizeContainer.childCount) {
                    val child = sizeContainer.getChildAt(i) as CardView
                    child.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ash))
                    (child.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(this, android.R.color.black))
                }
                // Highlight selected
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.black))
                sizeTextView.setTextColor(ContextCompat.getColor(this, R.color.ash))

                // Save selected size
                selectedSize = size
            }

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(12, 0, 12, 0)
            sizeContainer.addView(cardView, layoutParams)
        }

        //Checkout Button
        val checkoutButton: TextView = findViewById(R.id.navCheckout)
        checkoutButton.setOnClickListener {
            if (selectedSize == null) {
                Toast.makeText(this, "Please select a size first", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please create an account first", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
