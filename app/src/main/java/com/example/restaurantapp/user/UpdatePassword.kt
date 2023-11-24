package com.example.restaurantapp.user

data class UpdatePassword (
    val currentPassword: String,
    val newPassword: String
) {constructor():this("","")}
