package com.example.restaurantapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restaurantapp.base.BaseFragment
import com.example.restaurantapp.databinding.FragmentLoginBinding
import com.example.restaurantapp.repository.AuthRepository
import com.example.restaurantapp.retrofit.AuthAPI
import com.example.restaurantapp.viewModel.AuthViewModel

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater,container,false)

    override fun getFragmentRepository() = AuthRepository(remoteDataSource.dbAPI(AuthAPI::class.java))


}
