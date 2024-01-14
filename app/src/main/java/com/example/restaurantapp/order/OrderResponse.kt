package com.example.restaurantapp.order

data class OrderResponse(
    val customer: String,
    val ephemeralKey: String,
    val paymentIntent: String,
    val publishableKey: String
)