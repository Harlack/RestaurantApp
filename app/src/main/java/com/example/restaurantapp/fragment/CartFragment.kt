package com.example.restaurantapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R

class CartFragment : Fragment() {

    private val cartItems = mutableListOf<CartItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        val cartRecyclerView = view.findViewById<RecyclerView>(R.id.cartRecyclerView)
        val totalPriceTextView = view.findViewById<TextView>(R.id.totalPriceTextView)

        // Initialize cart items (you can add products to the cart here)

        // Create an adapter for the RecyclerView
        val cartAdapter = CartAdapter(cartItems)

        // Set the adapter for the RecyclerView
        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(context)

        // Calculate and update the total price
        val totalPrice = cartItems.sumByDouble { it.product.productPrice.toDouble() * it.quantity }
        totalPriceTextView.text = "Total Price: $$totalPrice"

        return view
    }

}