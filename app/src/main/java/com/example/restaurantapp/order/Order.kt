package com.example.restaurantapp.order

import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.meals.ShopMeal
import java.time.LocalDate
import java.util.Date

class Order {
    var meals: ArrayList<ShopMeal> = ArrayList()
    var tableNumber: Int = 0
    var totalPrice: Double = 0.0
    var status: String = "Zamówiono"
    var comments: String = ""
    var userToken : String = ""
    var userEmail : String = ""
    var paymentStatus : String = ""
    var orderRate : Int = 0
    var orderDate : Date = Date()
}