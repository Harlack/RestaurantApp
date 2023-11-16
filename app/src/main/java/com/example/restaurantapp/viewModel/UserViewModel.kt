package com.example.restaurantapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantapp.retrofit.RetrofitInstance
import com.example.restaurantapp.user.Data
import com.example.restaurantapp.user.LoginData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private var userData = MutableLiveData<Data>()

    fun getUserData(user: LoginData){
        val api = RetrofitInstance
        var loginResponse = api.getService().getUserData(user.email)
        loginResponse.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if(response.isSuccessful){
                    userData.value = response.body()
                    Log.d("UserData",userData.value.toString())

                }else{
                    return
                    Log.d("UserData",response.body().toString())
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Log.d("UserData",t.message.toString())
            }

        })
    }
    fun observerUserData():MutableLiveData<Data>{
        return userData
    }
}