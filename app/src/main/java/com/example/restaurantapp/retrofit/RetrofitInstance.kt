package com.example.restaurantapp.retrofit

import com.intuit.sdp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    val api:MealAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealAPI::class.java)
    }

    fun <Api> dbAPI(
        api: Class<Api>
    ) : Api {
        return Retrofit.Builder()
            .baseUrl("http://164.90.183.62/")
            .client(OkHttpClient()
                .newBuilder()
                .also { client ->
                if(BuildConfig.DEBUG){
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BASIC
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}