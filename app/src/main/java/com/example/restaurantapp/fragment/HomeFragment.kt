package com.example.restaurantapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.bumptech.glide.Glide
import com.example.restaurantapp.MealActivity
import com.example.restaurantapp.MealsAdapter
import com.example.restaurantapp.databinding.FragmentHomeBinding
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.user.LoginData
import com.example.restaurantapp.viewModel.HomeViewModel


class HomeFragment : Fragment(), MealsAdapter.MealInterface {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var myMeal: Meal
    private lateinit var mealsAdapter: MealsAdapter

    companion object{
        const val MEAL_ID = "MEAL_ID"
        const val MEAL_NAME = "MEAL_NAME"
        const val MEAL_THUMB = "MEAL_THUMB"
        const val MEAL_INDEX = "MEAL_INDEX"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]
        myMeal = Meal()
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
        mealsAdapter = MealsAdapter(myMeal.itemCallback)
        binding.recyclerview.adapter = mealsAdapter
        homeViewModel.getRandomMeal()

        onRandomMealClick()

    }


    private fun onRandomMealClick() {
        binding.mealShowCard.setOnClickListener{
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,myMeal._id)
            intent.putExtra(MEAL_NAME,myMeal.productName)
            intent.putExtra(MEAL_THUMB,myMeal.productImage)
            intent.putExtra(MEAL_INDEX,myMeal.__v)
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
        ) { t : Meal ->
            Glide.with(this@HomeFragment)
                .load(t.productImage)
                .into(binding.mealShowCard)

            this.myMeal = t
        }
    }

    override fun addMeal(meal: Meal) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(meal: Meal) {
        TODO("Not yet implemented")
    }

}

