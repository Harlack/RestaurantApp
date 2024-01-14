package com.example.restaurantapp.meals

import java.io.Serializable

class Order : Serializable {
    var meals: List<OrderMeals> = ArrayList()
    var tableNumber: Int = 0
    var totalPrice: Double = 0.0
    var comments: String = ""
    var userToken : String = ""
    var userEmail : String = ""
    var paymentStatus : String = ""
}