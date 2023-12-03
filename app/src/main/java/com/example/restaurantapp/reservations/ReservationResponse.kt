package com.example.restaurantapp.reservations

data class ReservationResponse(
    val `data`: List<Reservation>,
    val message: String
)
