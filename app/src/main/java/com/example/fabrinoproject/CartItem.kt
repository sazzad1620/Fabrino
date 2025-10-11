package com.example.fabrinoproject

data class CartItem(
    var documentId: String = "",
    var userName: String = "",
    var productName: String = "",
    var productPrice: Double = 0.0,
    var productImageUrl: String? = null,
    var size: String? = null,
    var quantity: Int = 1
)
