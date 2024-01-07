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

class PaymentViewModel : ViewModel(){
    private var paymentData = MutableLiveData<String>()
    fun doPayment(order: Order) {
        val api = RetrofitInstance
        var paymentResponse = api.getPaymentService().stripePayment(order)
        paymentResponse.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    paymentData.value = response.body()?.url
                    Log.d("Payment", response.body().toString())
                } else {
                    return
                    Log.d("Payment", response.body().toString())
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                Log.d("Payment", t.message.toString())
            }
        })
    }
    fun getResponse() : MutableLiveData<String> {
        return paymentData
    }

}