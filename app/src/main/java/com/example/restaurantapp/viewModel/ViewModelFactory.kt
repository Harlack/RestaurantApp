package com.example.restaurantapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantapp.repository.AuthRepository
import com.example.restaurantapp.repository.BaseRepository

class ViewModelFactory (private val baseRepository: BaseRepository ) : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return super.create(modelClass)
        return when{
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(baseRepository as AuthRepository) as T
            else -> throw IllegalArgumentException("ViewModel not found")
        }
    }
}