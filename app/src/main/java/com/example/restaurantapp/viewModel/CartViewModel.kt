package com.example.restaurantapp.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.restaurantapp.meals.Meal

class CartViewModel : ViewModel() {
    var cartMealList = mutableListOf<Meal>()


}