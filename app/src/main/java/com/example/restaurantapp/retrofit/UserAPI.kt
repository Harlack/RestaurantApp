package com.example.restaurantapp.retrofit

import com.example.restaurantapp.user.LoginData
import com.example.restaurantapp.user.RegisterData
import com.example.restaurantapp.user.ResponseBody
import com.example.restaurantapp.user.UpdatePassword
import com.example.restaurantapp.user.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface UserAPI {
    @POST("api/auth")
    fun login(@Body user : LoginData) : Call<ResponseBody>

    @POST("api/users")
    fun register(@Body user : RegisterData) : Call<RegisterData>

    @PUT("api/users/password")
    fun updateData(@Body passwords : UpdatePassword, @Header("x-access-token") token : String): Call<String>

    @GET("api/users/{email}")
    fun getUserData(@Path("email") email: String): Call<User>

    @DELETE("api/users/{id}")
    fun delete(@Path("id") id : String) : Call<String>
}