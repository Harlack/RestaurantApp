package com.example.restaurantapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.restaurantapp.repository.BaseRepository
import com.example.restaurantapp.retrofit.RetrofitInstance
import com.example.restaurantapp.viewModel.ViewModelFactory
import javax.sql.CommonDataSource

abstract class BaseFragment<VM: ViewModel, B: ViewBinding, R: BaseRepository> : Fragment() {

    protected lateinit var binding : B

    protected lateinit var  vm: VM
    protected var remoteDataSource = RetrofitInstance
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater,container)
        val factory = ViewModelFactory(getFragmentRepository())
        vm = ViewModelProvider(this,factory).get(getViewModel())
        return binding.root
    }
    abstract fun getViewModel(): Class<VM>
    abstract fun getFragmentBinding(inflater: LayoutInflater,container: ViewGroup?): B
    abstract fun getFragmentRepository(): R





}