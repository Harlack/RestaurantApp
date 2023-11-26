package com.example.restaurantapp.viewModel

import android.app.Application

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.restaurantapp.reservations.Reservation
import com.example.restaurantapp.reservations.ReservationResponse
import com.example.restaurantapp.reservations.Table
import com.example.restaurantapp.reservations.Tables
import com.example.restaurantapp.retrofit.RetrofitInstance
import com.example.restaurantapp.user.Data
import com.example.restaurantapp.user.User

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationViewModel(application: Application) : AndroidViewModel(application) {

    private var userData = MutableLiveData<Data>()
    private var reservationList = MutableLiveData<List<Reservation>>()
    private var tableList = MutableLiveData<List<Table>>()

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

    fun getListOfReservations(){
        val api = RetrofitInstance
        var reservationResponse = api.getReservationService().getReservations()
        reservationResponse.enqueue(object : Callback<ReservationResponse> {
            override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                if(response.isSuccessful){
                    val reservations = response.body()
                    reservationList.value = reservations?.data
                    Log.d("ReservationList",reservationList.toString())
                }else{
                    return
                    Log.d("ReservationList",response.body().toString())
                }
            }

            override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                Log.d("ReservationList",t.message.toString())
            }

        })
    }

    fun getListOfTables(){
        val api = RetrofitInstance
        var tableResponse = api.getReservationService().getTables()
        tableResponse.enqueue(object : Callback<Tables> {
            override fun onResponse(call: Call<Tables>, response: Response<Tables>) {
                if(response.isSuccessful){
                    val tables = response.body()
                    tableList.value = tables?.data
                    Log.d("TableList",tableList.toString())
                }else{
                    return
                    Log.d("TableList",response.body().toString())
                }
            }

            override fun onFailure(call: Call<Tables>, t: Throwable) {
                Log.d("TableList",t.message.toString())
            }

        })
    }

    fun addReservation(reservation: Reservation) {
        val api = RetrofitInstance
        var reservationResponse = api.getReservationService().makeReservation(reservation)
        reservationResponse.enqueue(object : Callback<Reservation> {
            override fun onResponse(call: Call<Reservation>, response: Response<Reservation>) {
                if(response.isSuccessful){
                    val reservation = response.body()
                    Log.d("Reservation",reservation.toString())
                }else{
                    return
                    Log.d("Reservation",response.body().toString())
                }
            }

            override fun onFailure(call: Call<Reservation>, t: Throwable) {
                Log.d("Reservation",t.message.toString())
            }

        })

    }


    fun getListOfReservationLD():MutableLiveData<List<Reservation>>{
        return reservationList
    }

    fun getUserDataLD():MutableLiveData<Data>{
        return userData
    }

    fun getListOfTablesLD():MutableLiveData<List<Table>>{
        return tableList
    }


}