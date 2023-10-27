package com.example.restaurantapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.restaurantapp.base.BaseFragment
import com.example.restaurantapp.databinding.FragmentLoginBinding
import com.example.restaurantapp.repository.AuthRepository
import com.example.restaurantapp.retrofit.AuthAPI
import com.example.restaurantapp.retrofit.Resource
import com.example.restaurantapp.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visible(false)
        binding.loginInButton.enable(false)
        vm.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visible(false)
            when(it){
                is Resource.Success -> {
                    Toast.makeText(requireContext(),"cc",Toast.LENGTH_LONG).show()
                    vm.saveAuthToken(it.value.data.token)
                    TODO("Dodać przekazanie tokenu")
                    requireActivity().startNewActivity(MainActivity::class.java)

                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(),"Login Failed",Toast.LENGTH_LONG).show()
                }
        } })

        binding.userLogin.addTextChangedListener {
            val email = binding.userLogin.text.toString().trim()
            binding.loginInButton.enable(email.isNotEmpty() && binding.userPassword.text.toString().trim().isNotEmpty())
        }

        binding.loginInButton.setOnClickListener {
            val email = binding.userLogin.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()
            binding.progressBar.visible(true)
            vm.login(email, password)
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater,container,false)

    override fun getFragmentRepository() = AuthRepository(remoteDataSource.dbAPI(AuthAPI::class.java), userPreferences)


}
