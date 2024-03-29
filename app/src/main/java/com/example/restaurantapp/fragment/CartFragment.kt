package com.example.restaurantapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantapp.activity.CheckoutActivity
import com.example.restaurantapp.adapter.CartAdapter
import com.example.restaurantapp.databinding.FragmentCartBinding
import com.example.restaurantapp.meals.ShopMeal
import com.example.restaurantapp.user.LoginData
import com.example.restaurantapp.viewModel.CartViewModel



class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartMealList: ArrayList<ShopMeal>
    private lateinit var cartViewModel: CartViewModel
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartMealList = ArrayList()
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        adapter = CartAdapter(kotlin.collections.ArrayList())

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.cartRecyclerView.setHasFixedSize(true)
        cartViewModel.loadCart()

        cartViewModel.cartMealListLD.observe(viewLifecycleOwner) { t -> ArrayList<ShopMeal>()
            cartMealList = t
            adapter.updateData(cartMealList)
            changeVisibility()
            setTotalPrice()

        }

        binding.cartRecyclerView.adapter = adapter

        binding.checkoutButton.setOnClickListener {
            startActivity(Intent(activity, CheckoutActivity::class.java))
        }

        adapter.setOnDeleteListener(object : CartAdapter.Listeners{
            override fun onDelete(position: Int) {
                cartMealList.removeAt(position)
                adapter.updateData(cartMealList)
                cartViewModel.saveCart(cartMealList)
                if (cartMealList.isEmpty()) {
                    changeVisibility()
                }
                setTotalPrice()
            }

            override fun onAdd(position: Int) {
                cartMealList[position].quantity++
                adapter.updateData(cartMealList)
                cartViewModel.saveCart(cartMealList)
                setTotalPrice()
            }

            override fun onMinus(position: Int) {
                if (cartMealList[position].quantity > 1){
                    cartMealList[position].quantity--
                    adapter.updateData(cartMealList)
                    cartViewModel.saveCart(cartMealList)
                    setTotalPrice()
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        cartViewModel.loadCart()
    }
    private fun setTotalPrice() {
        if (cartMealList.isEmpty()) {
            binding.totalPriceTextView.text = "Łączna cena: 0.00 zł"
            return
        }
        var suma = cartMealList.sumOf { parsePrice(it.productPrice) * it.quantity.toDouble() }
        binding.totalPriceTextView.text = "Łączna cena: $suma zł"
    }

    private fun parsePrice(price: String): Double {
            val cleanedPrice = price.replace(",", ".")
            return cleanedPrice.toDoubleOrNull() ?: 0.0
        }


        private fun changeVisibility() {
        if (cartMealList.isEmpty()) {
            binding.cartRecyclerView.visibility = View.GONE
            binding.totalPriceTextView.visibility = View.GONE
            binding.checkoutButton.visibility = View.GONE
            binding.emptyCartMessage.visibility = View.VISIBLE
        }else{
            binding.cartRecyclerView.visibility = View.VISIBLE
            binding.totalPriceTextView.visibility = View.VISIBLE
            binding.checkoutButton.visibility = View.VISIBLE
            binding.emptyCartMessage.visibility = View.GONE

        }

    }



}