package com.example.restaurantapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.restaurantapp.databinding.ActivityMealBinding
import com.example.restaurantapp.fragment.HomeFragment
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.viewModel.CartViewModel
import com.example.restaurantapp.viewModel.MealViewModel
import kotlin.math.log
import kotlin.properties.Delegates

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealID: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealStatus: String
    private var mealIndex: Int = 0
    private lateinit var mealMvvm: MealViewModel
    private lateinit var cartMvvm: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            finish()
        }
        mealMvvm = ViewModelProviders.of(this)[MealViewModel::class.java]
        cartMvvm = ViewModelProviders.of(this)[CartViewModel::class.java]

        getMealInformation()
        setMealInformation()

        mealMvvm.getMealDetail(mealIndex)
        loadingBar()
        observerMealDetailsLiveData()

    }

    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this,object : Observer<Meal>{
            override fun onChanged(value: Meal) {
                onRespondBar()
                binding.categoryDetail.text = "Category : ${value!!.productCategory}"
                binding.cashDetail.text = "Price : ${value!!.productPrice} zł"
                binding.descriptionDetail.text = "Description: ${value!!.productCategory}"
                binding.addToCardButton.setOnClickListener {
                    cartMvvm.addToCart(value)
                    Toast.makeText(applicationContext,"Added to cart",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        })
    }

    private fun setMealInformation() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsing.title = mealName
        binding.collapsing.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsing.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformation() {
        var intent = intent
        mealID = intent.getStringExtra(HomeFragment.MEAL_ID).toString()
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME).toString()
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB).toString()
        mealStatus = intent.getStringExtra(HomeFragment.MEAL_STATUS).toString()
        mealIndex = intent.getIntExtra(HomeFragment.MEAL_INDEX,0)
    }

    private fun loadingBar() {
        binding.progressbar.visibility = View.VISIBLE
        binding.floatingLikeButton.visibility = View.INVISIBLE
        binding.categoryDetail.visibility = View.INVISIBLE
        binding.descriptionDetail.visibility = View.INVISIBLE
        binding.cashDetail.visibility = View.INVISIBLE
        binding.addToCardButton.visibility = View.INVISIBLE
    }
    private fun onRespondBar(){
        binding.progressbar.visibility = View.INVISIBLE
        binding.floatingLikeButton.visibility = View.VISIBLE
        binding.categoryDetail.visibility = View.VISIBLE
        binding.descriptionDetail.visibility = View.VISIBLE
        binding.cashDetail.visibility = View.VISIBLE
        binding.addToCardButton.visibility = View.VISIBLE
    }
}