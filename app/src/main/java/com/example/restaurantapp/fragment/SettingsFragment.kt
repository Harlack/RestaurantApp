package com.example.restaurantapp.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
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
        binding.settingsList.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.settings)
        )
        if (user != null){
            setUserName(user?.email.toString())
        }else{
            setUserName("Guest")
        }
        user?.let { userViewModel.getUserData(it) }
        observerUserData()
        setListListener()
        setButton()


    }

    private fun setListListener() {
        binding.settingsList.setOnItemClickListener { _, _, position, _ ->
            when(position){
                0 -> {
                    val dialog = Dialog(requireContext())
                    dialog.window?.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    dialog.setContentView(R.layout.change_password_layout)
                    dialog.findViewById<TextView>(R.id.back).setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }
                1 -> {
                    val dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.change_phone_layout)
                    dialog.show()
                }
                2 -> {
                    deleteUser(userData)
                    activity?.getSharedPreferences("user", 0)?.edit()?.clear()?.apply()
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    Toast.makeText(activity,"User Deleted",Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    activity?.getSharedPreferences("user", 0)?.edit()?.clear()?.apply()
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }

    private fun observerUserData() {
        userViewModel.observerUserData().observe(viewLifecycleOwner) { t ->
            userData = t
        }
    }

    private fun setButton() {
        binding.loginBtn.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

    }

    private fun setUserName(email : String) {
        if (email == "Guest") {
            binding.loginLayout.visibility = View.VISIBLE
            binding.loggedInLayout.visibility = View.GONE
        }
        else
        {
            binding.userName.text = email
            binding.loginLayout.visibility = View.GONE
            binding.loggedInLayout.visibility = View.VISIBLE
        }
    }
    private fun deleteUser(user : Data){

    }



}