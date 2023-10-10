package com.example.restaurantapp.repository

import com.example.restaurantapp.retrofit.AuthAPI

class AuthRepository(private val api: AuthAPI) : BaseRepository() {

    suspend fun login(
        email: String,
        password : String
    ) = safeApiCall {
        api.login(email, password)
    }
}