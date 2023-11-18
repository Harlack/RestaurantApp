package com.example.restaurantapp.viewModel

import android.app.Application
import android.content.Context
import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map

import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.meals.ShopMeal
import com.example.restaurantapp.user.LoginData
import com.example.restaurantapp.user.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.log


class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("pref", Context.MODE_PRIVATE)
    private val userPreferences = application.getSharedPreferences("user", Context.MODE_PRIVATE)
    private var userEmail = userPreferences.getString("user", null)
    private val _cartMealList = MutableLiveData<ArrayList<ShopMeal>>()
    val cartMealListLD: LiveData<ArrayList<ShopMeal>>
        get() = _cartMealList


    fun loadCart() {
        val gson = Gson()
        val json = sharedPreferences.getString("$userEmail", null)

        var shopList = if (json != null) {
            val type = object : TypeToken<ArrayList<ShopMeal>>() {}.type
            gson.fromJson<ArrayList<ShopMeal>>(json, type)
        } else {
            ArrayList()
        }

        _cartMealList.value = shopList
    }

    fun addToCart(meal: Meal) {
        loadCart()
        var currentList = ArrayList<ShopMeal>()
        if(_cartMealList.value != null){
            for (i in _cartMealList.value!!){
                currentList.add(i)
            }
        }
        for (i in currentList){
            if(i.productName == meal.productName){
                i.quantity++
                saveCart(currentList)
                return
            }
        }
        val newShopMeal = ShopMeal(meal.productName, meal.productImage, meal.productPrice)
        currentList.add(newShopMeal)
        _cartMealList.value = currentList
        saveCart(currentList)
    }

    fun saveCart(cartMealList: java.util.ArrayList<ShopMeal>) {
        val gson = Gson()
        val json = gson.toJson(cartMealList)
        sharedPreferences.edit().putString("$userEmail", json).apply()
    }


}
