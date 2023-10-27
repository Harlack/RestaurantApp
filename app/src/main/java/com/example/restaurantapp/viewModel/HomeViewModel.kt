package com.example.restaurantapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantapp.meals.ListMeals
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.repository.UserRepository
import com.example.restaurantapp.retrofit.AuthAPI
import com.example.restaurantapp.retrofit.Resource
import com.example.restaurantapp.retrofit.RetrofitInstance
import com.example.restaurantapp.retrofit.UserAPI
import com.example.restaurantapp.user.LoginResponse
import com.example.restaurantapp.user.User
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class HomeViewModel(private val userRepository: UserRepository):ViewModel(){
    private var randomMealLD = MutableLiveData<Meal>()
    private val _user : MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val user : LiveData<Resource<LoginResponse>>
        get() = _user


    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : retrofit2.Callback<ListMeals>{
            override fun onResponse(call: Call<ListMeals>, response: Response<ListMeals>) {
                if (response.body()!=null){
                    val randomMealGenerated : Meal = response.body()!!.meals[0]
                    Log.d("Generated","Meal name: ${randomMealGenerated.strMeal}" +
                            ", id: ${randomMealGenerated.idMeal}")
                    randomMealLD.value = randomMealGenerated

                }else{
                    return
                }
            }

            override fun onFailure(call: Call<ListMeals>, t: Throwable) {
                Log.d("Home",t.message.toString())
            }
        })
    }
    fun obRandomMeal():LiveData<Meal>{
        return randomMealLD
    }

    fun getUser() = viewModelScope.launch {
        _user.value = Resource.Loading
        _user.value = userRepository.login() }
}