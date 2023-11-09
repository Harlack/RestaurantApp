package com.example.restaurantapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.R
import com.example.restaurantapp.meals.Meal
import java.util.ArrayList

class CartAdapter(private var cartMealList: ArrayList<Meal>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cartMealList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cartMealList[position]
        holder.name.text = item.productName
        holder.price.text = item.productPrice
        holder.quantity.text = "1"
        Glide.with(holder.itemView.context).load(item.productImage).into(holder.image)
        holder.addButton.setOnClickListener {
            var quantity = holder.quantity.text.toString().toInt()
            quantity++
            holder.quantity.text = quantity.toString()
        }
        holder.minusButton.setOnClickListener {
            var quantity = holder.quantity.text.toString().toInt()
            if (quantity > 1) {
                quantity--
                holder.quantity.text = quantity.toString()
            }
        }
        holder.deleteButton.setOnClickListener {
            cartMealList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartMealList.size)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.mealimg)
        val name: TextView = itemView.findViewById(R.id.mealname)
        val price: TextView = itemView.findViewById(R.id.mealprice)
        val quantity: TextView = itemView.findViewById(R.id.mealquantity)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
        val addButton: Button = itemView.findViewById(R.id.add_button)
        val minusButton: Button = itemView.findViewById(R.id.minus_button)

    }
}
