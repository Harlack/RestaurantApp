package com.example.restaurantapp.retrofit

import com.example.restaurantapp.user.LoginData
import com.example.restaurantapp.user.RegisterData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {
    @POST("api/auth")
    fun login(@Body user : LoginData) : Call<LoginData>

    @POST("api/users")
    fun register(@Body user : RegisterData) : Call<RegisterData>

}