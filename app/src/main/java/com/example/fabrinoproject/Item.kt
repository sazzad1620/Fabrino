package com.example.fabrinoproject

data class Item(
    val name: String = "No Name",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val sizes: List<String> = listOf("S", "M", "L", "XL", "2XL"),
    val description: String = "No Description",
    val category: String = "",
    val isPopular: Boolean = false
)
