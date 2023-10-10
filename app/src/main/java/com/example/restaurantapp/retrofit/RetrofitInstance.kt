package com.example.restaurantapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    val api:MealAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealAPI::class.java)
    }

    fun <Api> dbAPI(
        api: Class<Api>
    ) : Api {
        return Retrofit.Builder()
            .baseUrl("http://164.90.183.62/api")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}