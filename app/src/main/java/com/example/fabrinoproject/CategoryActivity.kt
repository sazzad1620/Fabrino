package com.example.fabrinoproject

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Get category name
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Category"

        // Top Bar
        val topBarIcon = findViewById<ImageView>(R.id.topBarIcon)
        topBarIcon.setImageResource(R.drawable.ic_arrow_back) // replace hamburger with arrow
        topBarIcon.setOnClickListener {
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategory)  //item card by recycler
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        val items = ItemRepository.getItemsByCategory(categoryName)
        recyclerView.adapter = ItemAdapter(items)

    }
}
