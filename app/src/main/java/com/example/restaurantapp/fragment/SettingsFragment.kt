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
import com.example.restaurantapp.user.LoginActivity
import com.example.restaurantapp.user.LoginData


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val user = activity?.intent?.getSerializableExtra("user") as? LoginData
        if (user != null) {
            setUserData(user)
        }
        setButtons()


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

    private fun setUserData(loginData: LoginData) {
        if (loginData.email=="guest") {
            binding.loginLayout.visibility = View.VISIBLE
        }
        else
        {
            binding.userName.text = loginData.email
            binding.loginLayout.visibility = View.GONE
        }
    }


}