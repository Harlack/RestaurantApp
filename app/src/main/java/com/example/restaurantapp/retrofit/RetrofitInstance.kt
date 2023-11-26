package com.example.restaurantapp.retrofit

import android.util.JsonToken
import android.util.Log
import com.google.gson.JsonObject
import com.intuit.sdp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    val api:MealAPI by lazy {
        Retrofit.Builder()
            .baseUrl("http://164.90.183.62/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealAPI::class.java)
    }

    private fun retrofit() : Retrofit {

        val logging = HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        val httpClient = OkHttpClient.Builder().addInterceptor(logging).build();

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://164.90.183.62/")
            .client(httpClient)
            .build();
        return retrofit;
    }
    fun getService() : UserAPI{
        val userService = retrofit().create(UserAPI::class.java);
        return userService;
    }
    fun getReservationService() : ReservationAPI{
        val userService = retrofit().create(ReservationAPI::class.java);
        return userService;
    }

}