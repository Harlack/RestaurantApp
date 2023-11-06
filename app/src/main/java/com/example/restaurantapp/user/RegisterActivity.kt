package com.example.restaurantapp.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.restaurantapp.R
import com.example.restaurantapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerButton : Button = findViewById(R.id.RegisterBtn)
        val email : EditText = findViewById(R.id.loginEdit)
        val password : EditText = findViewById(R.id.passwordEdit)
        val firstName : EditText = findViewById(R.id.imieEdit)
        val lastName : EditText = findViewById(R.id.nazwiskoEdit)
        val phone : EditText = findViewById(R.id.telefonEdit)

        var user = RegisterData()

        registerButton.setOnClickListener {
            user.email = email.text.toString()
            user.password = password.text.toString()
            user.firstName = firstName.text.toString()
            user.lastName = lastName.text.toString()
            user.phoneNumber = phone.text.toString()
            user.role = "user"
            registerUser(user)
        }

    }
    fun registerUser(dataRequest : RegisterData){
        val api = RetrofitInstance
        val registerResponse : Call<RegisterData> = api.getService().register(dataRequest)
        registerResponse.enqueue(object : Callback<RegisterData> {
            override fun onResponse(call: Call<RegisterData>, response: Response<RegisterData>) {
                if(response.isSuccessful){
                    Toast.makeText(this@RegisterActivity, "Register Success, please Log in", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@RegisterActivity, "Register Failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<RegisterData>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Register Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}