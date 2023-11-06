package com.example.restaurantapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Carousel.Adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.databinding.CartRowBinding
import com.example.restaurantapp.meals.Meal
import com.example.restaurantapp.meals.Meals
import okhttp3.internal.notify

class MealsAdapter(private val mealList: List<Meal>) : RecyclerView.Adapter<MealsAdapter.ViewHolder>(){

    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.meal_row,parent,false)
        return ViewHolder(itemView, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mealList[position]
        holder.name.text = item.productName
        holder.price.text = item.productPrice
        Glide.with(holder.itemView.context).load(item.productImage).into(holder.image)
    }



    class ViewHolder(itemView : View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.mealimg)
        val name: TextView = itemView.findViewById(R.id.mealname)
        val price: TextView = itemView.findViewById(R.id.mealprice)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)

            }
        }
    }
}
