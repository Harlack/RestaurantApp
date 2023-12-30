package com.example.restaurantapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.adapter.TableAdapter
import com.example.restaurantapp.meals.ShopMeal
import com.example.restaurantapp.viewModel.CartViewModel
import com.example.restaurantapp.viewModel.MealViewModel

class CheckoutActivity : AppCompatActivity() {

    private lateinit var userMealList : ArrayList<ShopMeal>
    private lateinit var cartViewModel : CartViewModel
    private lateinit var adapter: TableAdapter
    private lateinit var totalPrice : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        totalPrice = findViewById(R.id.checkoutTotalPrice)
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        userMealList = ArrayList()
        cartViewModel.loadCart()
        observerList()

        val tableRecyclerView : RecyclerView = findViewById(R.id.table_recycler_view)
        adapter = TableAdapter(userMealList)
        tableRecyclerView.layoutManager = LinearLayoutManager(this)
        tableRecyclerView.adapter = adapter


    }
    private fun observerList(){
        cartViewModel.cartMealListLD.observe(this) { t -> ArrayList<ShopMeal>()
            userMealList = t
            adapter.updateData(userMealList)
            setTotalPrice()
            Log.d("MealList", userMealList.size.toString())
        }
    }
    private fun setTotalPrice() {
        if (userMealList.isEmpty()) {
            totalPrice.text = "Łączna cena: 0.00 zł"
            return
        }
        var suma = userMealList.sumOf { parsePrice(it.productPrice) * it.quantity.toDouble() }
        totalPrice.text = "Łączna cena: ${String.format("%.2f", suma)} zł"
    }
    private fun parsePrice(price: String): Double {
        val cleanedPrice = price.replace(",", ".")
        return cleanedPrice.toDoubleOrNull() ?: 0.0
    }
}