    package com.example.restaurantapp.adapter

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageButton
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.core.content.ContextCompat
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import com.example.restaurantapp.R
    import com.example.restaurantapp.meals.ShopMeal

    class CartAdapter(private var cartMealList: ArrayList<ShopMeal>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

        private lateinit var mListener: Listeners

        interface Listeners {
            fun onDelete(position: Int)
            fun onAdd(position: Int)
            fun onMinus(position: Int)
        }

        fun setOnDeleteListener(listener: Listeners) {
            mListener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_row, parent, false)
            return ViewHolder(itemView, mListener)
        }

        override fun getItemCount(): Int {
            return cartMealList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = cartMealList[position]
            holder.name.text = item.productName
            holder.price.text = "${item.productPrice} zł"
            holder.quantity.text = item.quantity.toString()
            holder.status.text = item.productStatus
            holder.category.text = item.productCategory
            Glide.with(holder.itemView.context).load(item.productImage).into(holder.image)

            if (item.productStatus == "Dostępny") {
                holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
            } else {
                holder.status.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            }

            holder.minusButton.isEnabled = item.quantity != 1
            holder.addButton.setOnClickListener {
                mListener.onAdd(position)
            }
            holder.minusButton.setOnClickListener {
                mListener.onMinus(position)
            }
            holder.deleteButton.setOnClickListener{
                mListener.onDelete(position)
            }


        }

        fun updateData(newData: ArrayList<ShopMeal>) {
            cartMealList = newData
            notifyDataSetChanged()
        }


        class ViewHolder(itemView: View, listener: Listeners) : RecyclerView.ViewHolder(itemView) {
            val image: ImageView = itemView.findViewById(R.id.mealimg)
            val name: TextView = itemView.findViewById(R.id.mealname)
            val price: TextView = itemView.findViewById(R.id.mealprice)
            val quantity: TextView = itemView.findViewById(R.id.mealquantity)
            val status: TextView = itemView.findViewById(R.id.mealstatus)
            val category: TextView = itemView.findViewById(R.id.mealcategory)
            val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
            val addButton: ImageButton = itemView.findViewById(R.id.add_button)
            val minusButton: ImageButton = itemView.findViewById(R.id.minus_button)

        }
    }
