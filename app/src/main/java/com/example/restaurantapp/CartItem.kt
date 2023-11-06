package com.example.restaurantapp

import com.example.restaurantapp.meals.Meal

data class CartItem(
    val meal: Meal,
    var quantity: Int
)
