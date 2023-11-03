package com.example.restaurantapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.bumptech.glide.Glide
import com.example.restaurantapp.MealActivity
import com.example.restaurantapp.databinding.FragmentHomeBinding
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.user.LoginData
import com.example.restaurantapp.viewModel.HomeViewModel


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
        setUsername()
        observer()
        homeViewModel.getRandomMeal()
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

    private fun setUsername(){
        val user = activity?.intent?.getSerializableExtra("user") as? LoginData
        Log.d("user",user.toString())
        if (user != null) {
            binding.userName.text = user.email
        }else{
            binding.userName.text = "Guest"
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

