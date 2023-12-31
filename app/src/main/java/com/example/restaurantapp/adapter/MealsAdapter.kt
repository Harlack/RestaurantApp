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

class MealsAdapter(private var mealList: List<Meal>) : RecyclerView.Adapter<MealsAdapter.ViewHolder>(){

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
        holder.price.text = "${item.productPrice} zł"
        holder.status.text = item.productStatus
        holder.category.text = item.productCategory
        if (item.productStatus == "Dostępny") {
            holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        } else {
            holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }
        Glide.with(holder.itemView.context).load(item.productImage).into(holder.image)
    }

    fun updateData(newData: List<Meal>) {
        mealList = newData
        notifyDataSetChanged()
    }


    class ViewHolder(itemView : View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.mealimg)
        val name: TextView = itemView.findViewById(R.id.mealname)
        val price: TextView = itemView.findViewById(R.id.mealprice)
        val status: TextView = itemView.findViewById(R.id.mealstatus)
        val category: TextView = itemView.findViewById(R.id.mealcategory)
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)

            }
        }
    }
}
