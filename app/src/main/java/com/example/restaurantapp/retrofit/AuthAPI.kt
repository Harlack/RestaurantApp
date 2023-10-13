package com.example.restaurantapp.retrofit

import com.example.restaurantapp.user.Data
import com.example.restaurantapp.user.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthAPI {

    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,

    ): LoginResponse
}