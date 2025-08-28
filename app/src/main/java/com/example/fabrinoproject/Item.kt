package com.example.fabrinoproject

data class Item(
    val name: String,
    val price: String,
    val imageResId: Int,
    val sizes: List<String>,
    val description: String
)