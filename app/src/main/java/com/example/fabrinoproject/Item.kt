package com.example.fabrinoproject

data class Item(
    val id: String = "",
    val name: String = "No Name",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val sizes: List<String> = listOf("S", "M", "L", "XL", "2XL", "Free Size"),
    val description: String = "No Description",
    val category: String = "",
    val quantity: Int = 0,
    val isPopular: Boolean = false
)
