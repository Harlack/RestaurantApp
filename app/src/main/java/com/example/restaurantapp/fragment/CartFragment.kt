package com.example.restaurantapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

    private fun setTotalPrice() {
        var suma = cartMealList.sumOf{it.productPrice.toDouble() * it.quantity.toDouble()}
            binding.totalPriceTextView.text = "Total price: " +
                "${String.format("%.2f",suma).toDouble()} zł"
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