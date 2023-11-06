package com.example.restaurantapp.meals

import androidx.recyclerview.widget.DiffUtil

class Meal {
    var __v: Int = 0
    val _id: String = ""
    val productCategory: String = ""
    val productDescription: String = ""
    val productImage: String = ""
    val productName: String = ""
    val productPrice: String = ""
    val productStatus: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Meal

        if (__v != other.__v) return false
        if (_id != other._id) return false
        if (productCategory != other.productCategory) return false
        if (productDescription != other.productDescription) return false
        if (productImage != other.productImage) return false
        if (productName != other.productName) return false
        if (productPrice != other.productPrice) return false
        if (productStatus != other.productStatus) return false
        if (itemCallback != other.itemCallback) return false

        return true
    }

    override fun hashCode(): Int {
        var result = __v
        result = 31 * result + _id.hashCode()
        result = 31 * result + productCategory.hashCode()
        result = 31 * result + productDescription.hashCode()
        result = 31 * result + productImage.hashCode()
        result = 31 * result + productName.hashCode()
        result = 31 * result + productPrice.hashCode()
        result = 31 * result + productStatus.hashCode()
        result = 31 * result + itemCallback.hashCode()
        return result
    }

    var itemCallback : DiffUtil.ItemCallback<Meal>  = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem._id.equals(newItem._id)
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.equals(newItem)
        }
    }


}