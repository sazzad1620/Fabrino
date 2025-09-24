package com.example.fabrinoproject

data class Item(
    val name: String = "No Name",
    val price: Double = 0.0,           // Price as Double
    val imageUrl: String = "",          // URL instead of drawable
    val sizes: List<String> = listOf("S", "M", "L", "XL", "2XL"),
    val description: String = "No Description",
    val category: String = ""
)
