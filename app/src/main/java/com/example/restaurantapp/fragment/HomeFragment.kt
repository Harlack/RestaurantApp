package com.example.restaurantapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.restaurantapp.MealActivity
import com.example.restaurantapp.databinding.FragmentHomeBinding
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.meals.ListMeals
import com.example.restaurantapp.retrofit.RetrofitInstance
import com.example.restaurantapp.viewModel.HomeViewModel
import retrofit2.Call
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var myMeal: Meal
    companion object{
        const val MEAL_ID = "MEAL_ID"
        const val MEAL_NAME = "MEAL_NAME"
        const val MEAL_THUMB = "MEAL_THUMB"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getRandomMeal()
        observer()
        onRandomMealClick()

    }

    private fun onRandomMealClick() {
        binding.mealShowCard.setOnClickListener{
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,myMeal.idMeal)
            intent.putExtra(MEAL_NAME,myMeal.strMeal)
            intent.putExtra(MEAL_THUMB,myMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observer() {
        homeViewModel.obRandomMeal().observe(viewLifecycleOwner
        ) { t ->
            Glide.with(this@HomeFragment)
                .load(t!!.strMealThumb)
                .into(binding.mealShowCard)

            this.myMeal = t
        }
    }

}

