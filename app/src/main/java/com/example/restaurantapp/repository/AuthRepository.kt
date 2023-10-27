package com.example.restaurantapp.repository

import com.example.restaurantapp.UserPreferences
import com.example.restaurantapp.retrofit.AuthAPI
import com.example.restaurantapp.retrofit.Resource
import com.example.restaurantapp.user.LoginResponse

class AuthRepository(private val api: AuthAPI, private val preferences: UserPreferences) : BaseRepository() {

    suspend fun login(
        email: String,
        password : String
    )  = safeApiCall {
        api.login(email, password)
    }

    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }
}