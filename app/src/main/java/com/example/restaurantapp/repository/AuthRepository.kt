package com.example.restaurantapp.repository

import com.example.restaurantapp.retrofit.AuthAPI
import com.example.restaurantapp.retrofit.Resource
import com.example.restaurantapp.user.LoginResponse

class AuthRepository(private val api: AuthAPI) : BaseRepository() {

    suspend fun login(
        email: String,
        password : String
    )  = safeApiCall {
        api.login(email, password)
    }
}