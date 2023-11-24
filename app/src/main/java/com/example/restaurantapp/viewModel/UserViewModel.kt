package com.example.restaurantapp.viewModel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantapp.retrofit.RetrofitInstance
import com.example.restaurantapp.user.Data
import com.example.restaurantapp.user.UpdatePassword
import com.example.restaurantapp.user.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var userData = MutableLiveData<Data>()
    private var token = application.getSharedPreferences("user", MODE_PRIVATE).getString("token",null)
    fun getUserData(userEmail: String){
        val api = RetrofitInstance
        var loginResponse = api.getService().getUserData(userEmail)
        loginResponse.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    userData.value = response.body()?.data
                    Log.d("UserData",userData.value.toString())
                }else{
                    return
                    Log.d("UserData",response.body().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("UserData",t.message.toString())
            }

        })
    }

    fun updatePassword(passwords : UpdatePassword){
        val api = RetrofitInstance
        var loginResponse = api.getService().updateData(passwords,token = "$token")
        loginResponse.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    Toast.makeText(null,response.body().toString(), Toast.LENGTH_SHORT).show()

                }else{
                    return
                    Toast.makeText(null,response.body().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("UserData",t.message.toString())
            }

        })
    }
    fun observerUserData():MutableLiveData<Data>{
        return userData
    }


}