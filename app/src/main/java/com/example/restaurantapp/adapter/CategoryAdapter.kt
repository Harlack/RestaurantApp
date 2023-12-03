package com.example.restaurantapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.R
import com.example.restaurantapp.meals.Meal

class CategoryAdapter(private var categoryList: List<Meal>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private lateinit var mListener: Listeners

    interface Listeners {
        fun onItemCLick(position: Int)
    }

    fun setOnClickListener(listener: Listeners) {
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.meal_row, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categoryList[position]
        holder.name.text = item.productName
        holder.price.text = "${item.productPrice} zł"
        holder.status.text = item.productStatus
        holder.category.text = item.productCategory
        holder.description.text = item.productDescription
        if (item.productStatus == "Dostępny") {
            holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        } else {
            holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }
        Glide.with(holder.itemView.context).load(item.productImage).into(holder.image)

    }

    fun updateData(mealsList: List<Meal>) {
        categoryList = mealsList
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View, listener: Listeners) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.mealimg)
        val name: TextView = itemView.findViewById(R.id.mealname)
        val price: TextView = itemView.findViewById(R.id.mealprice)
        val status: TextView = itemView.findViewById(R.id.mealstatus)
        val category: TextView = itemView.findViewById(R.id.mealcategory)
        val description : TextView = itemView.findViewById(R.id.mealdescription)
        init {
            itemView.setOnClickListener{
                listener.onItemCLick(adapterPosition)
            }
        }
    }
}