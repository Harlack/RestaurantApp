package com.example.restaurantapp.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.restaurantapp.MainActivity
import com.example.restaurantapp.R
import com.example.restaurantapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var login : EditText = findViewById(R.id.loginEdit)
        var password : EditText = findViewById(R.id.passwordEdit)
        var loginBtn : Button = findViewById(R.id.LoginBtn)
        var user = LoginData()

        loginBtn.setOnClickListener {
            user.email = login.text.toString()
            user.password = password.text.toString()
            loginUser(user)
        }

    }
    fun loginUser(dataRequest : LoginData){
        val api = RetrofitInstance
        val loginResponse = api.getService().login(dataRequest)
        loginResponse.enqueue(object : Callback<LoginData> {
            override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                if(response.isSuccessful){

                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("user", dataRequest)
                    Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginData>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}