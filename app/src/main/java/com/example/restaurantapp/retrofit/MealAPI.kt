package com.example.restaurantapp.retrofit

import com.example.restaurantapp.meals.ListMeals
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPI {

    @GET("random.php")
    fun getRandomMeal():Call<ListMeals>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id:String) : Call<ListMeals>
}