package com.example.fabrinoproject

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Category"

        val topBarIcon = findViewById<ImageView>(R.id.ivHamburger)
        topBarIcon.setImageResource(R.drawable.ic_arrow_back)
        topBarIcon.setOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategory)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        lifecycleScope.launch {
            val items = ItemRepository.getItemsByCategory(categoryName)
            recyclerView.adapter = ItemAdapter(items)
        }
    }
}
