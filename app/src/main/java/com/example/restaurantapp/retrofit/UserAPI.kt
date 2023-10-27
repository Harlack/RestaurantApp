package com.example.restaurantapp.retrofit

import com.example.restaurantapp.user.LoginResponse
import retrofit2.http.GET

interface UserAPI {
    @GET("users")
    suspend fun getUser(): LoginResponse
}