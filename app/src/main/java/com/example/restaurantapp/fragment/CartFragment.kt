package com.example.restaurantapp.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.adapter.CartAdapter
import com.example.restaurantapp.databinding.FragmentCartBinding
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.visible

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var cartMealList = ArrayList<Meal>()
        val sharedPreferences = activity?.getSharedPreferences("Shopping_cart", MODE_PRIVATE)
        if (sharedPreferences != null) {
            cartMealList = sharedPreferences.all as ArrayList<Meal>
            Log.d("Shared",cartMealList.toString())
        }else{

            changeVisibility()
        }

        var cartRecyclerView = view.findViewById<RecyclerView>(R.id.cartRecyclerView)
        cartRecyclerView.layoutManager = LinearLayoutManager(context)
        cartRecyclerView.adapter = CartAdapter(cartMealList)

    }

    private fun changeVisibility() {
        binding.cartRecyclerView.visible(false)
        binding.totalPriceTextView.visible(false)
        binding.checkoutButton.visible(false)
        binding.emptyCartMessage.visible(true)
    }
}