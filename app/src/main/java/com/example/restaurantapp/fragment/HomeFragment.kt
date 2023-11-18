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
import com.example.restaurantapp.adapter.MealsAdapter
import com.example.restaurantapp.databinding.FragmentHomeBinding
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.user.LoginData
import com.example.restaurantapp.viewModel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var myMeal: Meal
    private lateinit var mealsList: List<Meal>
    private lateinit var adapter: MealsAdapter

    companion object{
        const val MEAL_ID = "MEAL_ID"
        const val MEAL_NAME = "MEAL_NAME"
        const val MEAL_THUMB = "MEAL_THUMB"
        const val MEAL_INDEX = "MEAL_INDEX"
        const val MEAL_STATUS = "MEAL_STATUS"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        myMeal = Meal()
        mealsList = mutableListOf()
        adapter = MealsAdapter(emptyList())

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
        binding.recyclerview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        binding.recyclerview.setHasFixedSize(true)
        homeViewModel.getRandomMeal()

        observerRandom()
        observerList()
        onRandomMealClick()
        binding.recyclerview.adapter = adapter

        adapter.setOnItemClickListener(object : MealsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(MEAL_ID, mealsList[position]._id)
                intent.putExtra(MEAL_NAME, mealsList[position].productName)
                intent.putExtra(MEAL_THUMB, mealsList[position].productImage)
                intent.putExtra(MEAL_INDEX, mealsList[position].__v)
                startActivity(intent)
            }
        })

    }

    private fun observerList() {
        homeViewModel.getListOfMeals().observe(viewLifecycleOwner
        ) { t: List<Meal> ->
            mealsList = t
            adapter.updateData(mealsList)
        }

    }


    private fun onRandomMealClick() {
        binding.mealShowCard.setOnClickListener{
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,myMeal._id)
            intent.putExtra(MEAL_NAME,myMeal.productName)
            intent.putExtra(MEAL_THUMB,myMeal.productImage)
            intent.putExtra(MEAL_STATUS,myMeal.productStatus)
            intent.putExtra(MEAL_INDEX,myMeal.__v)
            startActivity(intent)
        }
    }

    private fun setUsername(){
        val user = activity?.intent?.getSerializableExtra("user") as? LoginData
        if (user != null) {
            binding.userName.text = user.email
        }else{
            binding.userName.text = "Guest"
        }
    }
    private fun observerRandom() {
        homeViewModel.obRandomMeal().observe(viewLifecycleOwner
        ) { t : Meal ->
            Glide.with(this@HomeFragment)
                .load(t.productImage)
                .into(binding.mealShowCard)

            this.myMeal = t
        }
    }




}

