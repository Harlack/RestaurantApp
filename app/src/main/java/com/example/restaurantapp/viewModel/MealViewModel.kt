package com.example.restaurantapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantapp.meals.ListMeals
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel():ViewModel() {
    private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<ListMeals> {
            override fun onResponse(call: Call<ListMeals>, response: Response<ListMeals>) {
                if(response.body()!=null){
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<ListMeals>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }

        })
    }

    fun observerMealDetailsLiveData():LiveData<Meal>{
        return mealDetailsLiveData
    }
}