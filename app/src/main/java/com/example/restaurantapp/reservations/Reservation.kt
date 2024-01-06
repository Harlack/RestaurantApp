package com.example.restaurantapp.reservations

data class Reservation(
    val reservationComment: String,
    val reservationDate: String,
    val reservationPerson: String,
    var reservationTable: Int,
    val reservationTime: Int
)