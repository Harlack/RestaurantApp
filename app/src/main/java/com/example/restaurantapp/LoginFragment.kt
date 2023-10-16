package com.example.restaurantapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.restaurantapp.base.BaseFragment
import com.example.restaurantapp.databinding.FragmentLoginBinding
import com.example.restaurantapp.repository.AuthRepository
import com.example.restaurantapp.retrofit.AuthAPI
import com.example.restaurantapp.retrofit.Resource
import com.example.restaurantapp.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.loginResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success<*> -> {

                    Toast.makeText(requireContext(),"Login Succes",Toast.LENGTH_LONG).show()
                    lifecycleScope.launch { userPreferences.saveAuthToken(it.value.toString())}
                    TODO("Dodać przekazanie tokenu")

                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(),"Login Failed",Toast.LENGTH_LONG).show()
                }
        } })
        binding.loginInButton.setOnClickListener {
            val email = binding.loginText.text.toString().trim()
            val password = binding.passwordText.text.toString().trim()
            vm.login(email, password)
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater,container,false)

    override fun getFragmentRepository() = AuthRepository(remoteDataSource.dbAPI(AuthAPI::class.java))


}
