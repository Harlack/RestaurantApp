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
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealAPI::class.java)
    }

    fun <Api> dbAPI(
        api: Class<Api>,
        token: String? = null
    ) : Api {
        val request : Request = Request.Builder()
        .url("http://164.90.183.62/login")
            .build()
        val client = OkHttpClient()

        try {
            val response: Response = client.newCall(request).execute()
            val responseBody : String? = response.body?.string()
            val token : String? = response.headers["Authorization"]
            Log.d("token",token.toString())
        }catch (e: Exception){
            e.printStackTrace()
        }
        return Retrofit.Builder()
            .baseUrl("http://164.90.183.62/")
            .client(OkHttpClient()
                .newBuilder()
                .addInterceptor{ chain ->
                    chain.proceed(chain.request().newBuilder().also {
                        it.addHeader("Authorization", "Bearer $token")
                    }.build())}
                .also { client ->
                if(BuildConfig.DEBUG){
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}