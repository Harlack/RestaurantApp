package com.example.restaurantapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantapp.order.Order
import com.example.restaurantapp.order.OrderResponse
import com.example.restaurantapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel : ViewModel(){
    private var paymentData = MutableLiveData<OrderResponse>()

    fun paymentSheet(order: Order) {
        val api = RetrofitInstance
        val paymentResponse = api.getOrderService()
        paymentResponse.stripePayment(order).enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    paymentData.value = response.body()
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }
        })

    }

    fun makeOrder(order: Order) {
        val api = RetrofitInstance
        val orderResponse = api.getOrderService()
        orderResponse.makeOrder(order).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.e("Success", response.body().toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }
        })
    }

    fun getResponse() : MutableLiveData<OrderResponse> {
        return paymentData
    }

}