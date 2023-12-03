package com.example.restaurantapp.retrofit

import com.example.restaurantapp.reservations.Reservation
import com.example.restaurantapp.reservations.ReservationResponse
import com.example.restaurantapp.reservations.Tables
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReservationAPI {
    @GET("api/reservations")
    fun getReservations(): Call<ReservationResponse>

    @POST("api/reservations")
    fun makeReservation(@Body reservation: Reservation): Call<Reservation>

    @GET("api/tables")
    fun getTables(): Call<Tables>
}
