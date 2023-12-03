package com.example.restaurantapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.meals.Meals
import com.example.restaurantapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel : ViewModel() {
    private var fullList = MutableLiveData<List<Meal>>()

    fun loadMeals(){
        RetrofitInstance.api.getListOfMeal().enqueue(object : Callback<Meals> {
            override fun onResponse(call: Call<Meals>, response: Response<Meals>) {
                if (response.body()!=null){
                    val mealsResponse = response.body()
                    val mealList = mealsResponse?.data
                    for (meal in mealList!!){
                        meal.__v = mealList.indexOf(meal)
                    }
                    fullList.value = mealList!!

                }else{
                    Log.d("Home",response.body().toString())
                    return
                }
            }

            override fun onFailure(call: Call<Meals>, t: Throwable) {
                Log.d("Home",t.message.toString())
            }
        })
    }
    fun getFullList():MutableLiveData<List<Meal>>{
        return fullList
    }
}