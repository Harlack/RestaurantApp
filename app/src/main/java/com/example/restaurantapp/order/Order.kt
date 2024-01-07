package com.example.restaurantapp.order

import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.meals.ShopMeal
import java.time.LocalDate
import java.util.Date

class Order {
    var meals: List<OrderMeals> = ArrayList()
    var tableNumber: Int = 0
    var totalPrice: Double = 0.0
    var comments: String = ""
    var userToken : String = ""
    var userEmail : String = ""
}