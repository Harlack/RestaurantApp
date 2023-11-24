package com.example.restaurantapp.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentSettingsBinding
import com.example.restaurantapp.user.LoginActivity
import com.example.restaurantapp.user.UpdatePassword
import com.example.restaurantapp.user.User
import com.example.restaurantapp.viewModel.UserViewModel


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userData: User
    private lateinit var user: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = UserViewModel(requireActivity().application)
        userData = User()
        user = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("user", null).toString()
        password = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("password", null).toString()
        if (user != "Guest"){
            userViewModel.getUserData(user!!)
            Log.d("userData",userData.toString())
        }else{
            userData.data.email = "Guest"
        }

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
        binding.settingsList.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.settings)
        )
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
                    dialog.findViewById<TextView>(R.id.changePasswordBtn).setOnClickListener {
                        val oldPassword = dialog.findViewById<EditText>(R.id.oldPassword).text.toString()
                        val newPassword = dialog.findViewById<EditText>(R.id.newPassword).text.toString()
                        val confirmPassword = dialog.findViewById<EditText>(R.id.confirmPassword).text.toString()
                        val passwords = UpdatePassword(oldPassword,newPassword)
                        if (oldPassword == password){
                            if (newPassword == confirmPassword && newPassword.isNotEmpty()){
                                userViewModel.updatePassword(passwords)
                                dialog.dismiss()
                            }else{
                                Toast.makeText(activity,"Hasła się różnią!",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(activity,"Złe hasło",Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.show()
                }
                1 -> {
                    val dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.change_phone_layout)
                    dialog.show()
                }
                2 -> {
                    //deleteUser(userData)
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
            userData.data = t
            setUserName(userData.data.email)
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


}