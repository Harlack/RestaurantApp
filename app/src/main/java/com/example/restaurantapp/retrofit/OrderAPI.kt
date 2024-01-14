package com.example.restaurantapp.retrofit

import com.example.restaurantapp.order.Order
import com.example.restaurantapp.order.OrderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderAPI {

    @POST("api/payment/payment-sheet")
    fun stripePayment(@Body order : Order) : Call<OrderResponse>

    @POST("api/orders")
    fun makeOrder(@Body order : Order) : Call<String>
}