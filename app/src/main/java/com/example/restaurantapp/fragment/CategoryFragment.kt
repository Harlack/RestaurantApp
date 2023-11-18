package com.example.restaurantapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.restaurantapp.MealActivity
import com.example.restaurantapp.R
import com.example.restaurantapp.adapter.CategoryAdapter
import com.example.restaurantapp.adapter.MealsAdapter
import com.example.restaurantapp.databinding.FragmentCartBinding
import com.example.restaurantapp.databinding.FragmentCategoryBinding
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.viewModel.CategoryViewModel
import com.example.restaurantapp.viewModel.HomeViewModel

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var mealsList: List<Meal>
    private lateinit var adapter: CategoryAdapter
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var filteredValues: List<Meal>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        mealsList = emptyList()
        adapter = CategoryAdapter(emptyList())
        filteredValues = emptyList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        binding.categoryRecyclerView.setHasFixedSize(true)
        categoryViewModel.loadMeals()
        binding.categoryRecyclerView.adapter = adapter
        observerList()
        radioButtons()
        adapter.setOnClickListener(object : CategoryAdapter.Listeners {
            override fun onItemCLick(position: Int) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID, mealsList[position]._id)
                intent.putExtra(HomeFragment.MEAL_NAME, mealsList[position].productName)
                intent.putExtra(HomeFragment.MEAL_THUMB, mealsList[position].productImage)
                intent.putExtra(HomeFragment.MEAL_INDEX, mealsList[position].__v)
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.wszystkie.isChecked = true
    }
    private fun observerList() {
      categoryViewModel.getFullList().observe(viewLifecycleOwner) { t ->
            mealsList = t
            adapter.updateData(mealsList)
        }

    }
    private fun radioButtons(){
        binding.categoriesGroup.setOnCheckedChangeListener(){ _, checkedId ->
            when(checkedId){
                R.id.wszystkie -> {
                    filteredValues = mealsList
                    adapter.updateData(filteredValues)
                }
                R.id.daniaGlowne -> {
                    filteredValues = mealsList.filter { it.productCategory == "Dania główne" }
                    adapter.updateData(filteredValues)
                }
                R.id.przystawki -> {
                    filteredValues = mealsList.filter { it.productCategory == "Przystawki" }
                    adapter.updateData(filteredValues)
                }
                R.id.desery -> {
                    filteredValues = mealsList.filter { it.productCategory == "Desery" }
                    adapter.updateData(filteredValues)
                }
                R.id.napoje -> {
                    filteredValues = mealsList.filter { it.productCategory == "Napoje" }
                    adapter.updateData(filteredValues)
                }
            }
        }
    }

}