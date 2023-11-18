package com.example.restaurantapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentCartBinding
import com.example.restaurantapp.databinding.FragmentSettingsBinding
import com.example.restaurantapp.retrofit.RetrofitInstance
import com.example.restaurantapp.user.Data
import com.example.restaurantapp.user.LoginActivity
import com.example.restaurantapp.user.LoginData
import com.example.restaurantapp.viewModel.UserViewModel


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userData: Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = UserViewModel()
        userData = Data()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = activity?.intent?.getSerializableExtra("user") as LoginData?
        if (user != null) {
            userViewModel.getUserData(user)
        }
        setButtons()
        observerUserData()

    }

    private fun observerUserData() {
        userViewModel.observerUserData().observe(viewLifecycleOwner) { t ->
            userData = t
            setUserName(t)
        }
    }

    private fun setButtons() {
        binding.loginBtn.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        binding.logoutBtn.setOnClickListener {
            activity?.getSharedPreferences("user", 0)?.edit()?.clear()?.apply()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun setUserName(loginData: Data) {
        if (loginData.email=="guest") {
            binding.loginLayout.visibility = View.VISIBLE
        }
        else
        {
            binding.userName.text = loginData.email
            binding.loginLayout.visibility = View.GONE
        }
    }
    private fun deleteUser(user : Data){

    }



}