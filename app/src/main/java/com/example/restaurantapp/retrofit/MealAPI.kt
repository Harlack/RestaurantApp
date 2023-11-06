package com.example.restaurantapp.retrofit

import com.example.restaurantapp.meals.Meals
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPI {

    @GET("products")
    fun getRandomMeal():Call<Meals>

}