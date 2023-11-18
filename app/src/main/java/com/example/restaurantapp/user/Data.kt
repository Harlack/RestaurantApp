package com.example.restaurantapp.user

import java.io.Serializable

data class Data(
    var __v: Int,
    var _id: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var password: String,
    var phoneNumber: String,
    var points: Int,
    var roles: String,
    var token : String
) : Serializable {
    constructor() : this(0,"","","","","","",0,"","")
}