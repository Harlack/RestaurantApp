package com.example.restaurantapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.meals.Meals
import com.example.restaurantapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class HomeViewModel():ViewModel(){
    private var randomMealLD = MutableLiveData<Meal>()
    private var listOfMeals = MutableLiveData<List<Meal>>()
    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<Meals> {
            override fun onResponse(call: Call<Meals>, response: Response<Meals>) {
                if (response.body()!=null){
                    val mealsResponse = response.body()
                    val mealList = mealsResponse?.data
                    var randomMealGenerated = mealList?.random()!!
                    randomMealGenerated.__v = mealList.indexOf(randomMealGenerated)
                    Log.d("Generated","Meals name: ${randomMealGenerated.productName}" +
                            ", id: ${randomMealGenerated._id}, index: ${randomMealGenerated.__v}")
                    randomMealLD.value = randomMealGenerated
                    listOfMeals.value = mealList!!

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

    fun getListOfMeals():LiveData<List<Meal>>{
        return listOfMeals
    }
    fun obRandomMeal():LiveData<Meal>{
        return randomMealLD
    }


}