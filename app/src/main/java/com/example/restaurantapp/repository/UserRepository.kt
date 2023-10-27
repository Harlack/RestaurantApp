package com.example.restaurantapp.repository

import com.example.restaurantapp.UserPreferences
import com.example.restaurantapp.retrofit.AuthAPI
import com.example.restaurantapp.retrofit.Resource
import com.example.restaurantapp.retrofit.UserAPI
import com.example.restaurantapp.user.LoginResponse

class UserRepository(private val api: UserAPI, private val preferences: UserPreferences) : BaseRepository() {

    suspend fun login()  = safeApiCall {
        api.getUser()
    }
}