package com.example.restaurantapp.viewModel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.restaurantapp.retrofit.RetrofitInstance
import com.example.restaurantapp.user.Data
import com.example.restaurantapp.user.ResponseMessage
import com.example.restaurantapp.user.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var userData = MutableLiveData<Data>()
    private var message = MutableLiveData<String>()
    private var token = application.getSharedPreferences("user", MODE_PRIVATE).getString("token",null)
    fun getUserData(userEmail: String){
        val api = RetrofitInstance
        var userResponse = api.getService().getUserData(userEmail)
        userResponse.enqueue(object : Callback<User> {
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

    fun updatePassword(passwords: Map<String, String>){
        val api = RetrofitInstance
        var passwordResponse = api.getService().updateData(passwords, token = "$token")
        passwordResponse.enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                if(response.isSuccessful){
                    val responseMessage = response.body()
                    message.value = responseMessage?.message
                }else{
                    return
                    Toast.makeText(null,response.body().toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                Log.d("UserData",t.message.toString())
            }

        })
    }

    fun updatePhone(updateNumbers: Map<String,String>) {
        val api = RetrofitInstance
        var phoneResponse = api.getService().updatePhone(updateNumbers, token = "$token")
        phoneResponse.enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                if(response.isSuccessful){
                    val responseMessage = response.body()
                    message.value = responseMessage?.message
                }else{
                    return
                    Toast.makeText(null,response.body().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                Log.d("UserData",t.message.toString())
            }

        })
    }


    fun deleteUser(id: String) {
        val api = RetrofitInstance
        var deleteResponse = api.getService().deleteUser(id, token = "$token")
        deleteResponse.enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                if(response.isSuccessful){
                    val responseMessage = response.body()?.message
                    message.value = responseMessage!!
                }else{
                    return
                    Toast.makeText(null,response.body().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                Log.d("UserData",t.message.toString())
            }

        })
    }
    fun observerUserData():MutableLiveData<Data>{
        return userData
    }
    fun observerMessage():MutableLiveData<String>{
        return message
    }

}