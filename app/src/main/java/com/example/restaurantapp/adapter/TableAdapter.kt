package com.example.restaurantapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.meals.ShopMeal

class TableAdapter(private var mealList: List<ShopMeal>) : RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.table_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
    fun updateData(newData: ArrayList<ShopMeal>) {
        mealList = newData
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mealList[position]
        holder.name.text = item.productName
        holder.quantity.text = item.quantity.toString()
        holder.price.text = "${item.productPrice} zł"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.checkoutTableDishName)
        val quantity: TextView = itemView.findViewById(R.id.checkoutTableDishQuantity)
        val price: TextView = itemView.findViewById(R.id.checkoutTableDishPrice)
    }

}