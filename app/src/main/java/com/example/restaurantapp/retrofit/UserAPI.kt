package com.example.restaurantapp.retrofit

import com.example.restaurantapp.user.Data
import com.example.restaurantapp.user.LoginData
import com.example.restaurantapp.user.RegisterData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserAPI {
    @POST("api/auth")
    fun login(@Body user : LoginData) : Call<LoginData>

    @POST("api/users")
    fun register(@Body user : RegisterData) : Call<RegisterData>

    @GET("api/users/{email}")
    fun getUserData(@Path("email") email: String): Call<Data>


    @DELETE("api/users/{id}")
    fun delete(@Path("id") @Body id : String) : Call<String>
}