package com.example.restaurantapp.retrofit

import com.example.restaurantapp.order.Order
import com.example.restaurantapp.order.OrderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentAPI {
    @POST("api/payment")
    fun stripePayment(@Body order : Order) : Call<OrderResponse>
}