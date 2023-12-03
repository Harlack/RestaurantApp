package com.example.restaurantapp.user


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
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
        val cardView : LinearLayout = findViewById(R.id.cardView)

        val user = RegisterData()

        setValidation(email,password,firstName,lastName,phone)

        registerButton.setOnClickListener {
            if (email.text.toString().trim().isEmpty() || password.text.toString().trim().isEmpty() || firstName.text.toString().trim().isEmpty()
                    || lastName.text.toString().trim().isEmpty() || phone.text.toString().trim().isEmpty()){
                Toast.makeText(this@RegisterActivity, "Wszystkie pola są wymagane", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            user.email = email.text.toString()
            user.password = password.text.toString()
            user.firstName = firstName.text.toString()
            user.lastName = lastName.text.toString()
            user.phoneNumber = phone.text.toString()
            registerUser(user)
        }
        cardView.setOnClickListener {
            finish()
        }


    }

    private fun setValidation(email: EditText, password: EditText, firstName: EditText, lastName: EditText, phone: EditText) {
        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No need to implement
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No need to implement
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {



                if (email.text.toString().trim().isEmpty()){
                    email.error = "Email jest wymagany"
                }else{
                    if(email.text.toString().trim().contains("@")){
                        email.error = null
                    }else{
                        email.error = "Niepoprawny email"
                    }
                }
            }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No need to implement
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No need to implement
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (password.text.toString().trim().isEmpty()) {
                    password.error = "Hasło jest wymagane"
                } else {
                    if(password.text.toString().trim().length >= 8 && password.text.toString().trim().contains(Regex("[A-Z]"))
                            && password.text.toString().trim().contains(Regex("[!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,.\\-]"))) {
                        password.error = null
                    }else{
                        password.error = "Hasło musi mieć minimum 8 znaków, w tym jedną wielką literę oraz znak specjalny"
                    }
                }
            }

        })

        firstName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No need to implement
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No need to implement
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (firstName.text.toString().trim().isEmpty()) {
                    firstName.error = "Imię jest wymagane"
                } else {
                    if(firstName.text.toString().trim().length >= 2 && firstName.text.toString().trim().contains(Regex("[A-Z]"))){
                        firstName.error = null
                    }else{
                        firstName.error = "Imię musi mieć minimum 2 znaki oraz jedną wielką literę"
                    }
                }
            }

        })

        lastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No need to implement
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No need to implement
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (lastName.text.toString().trim().isEmpty()) {
                    lastName.error = "Nazwisko jest wymagane"
                } else {
                    if(lastName.text.toString().trim().length >= 2 && lastName.text.toString().trim().contains(Regex("[A-Z]"))){
                        lastName.error = null
                    }else{
                        lastName.error = "Nazwisko musi mieć minimum 2 znaki oraz jedną wielką literę"
                    }
                }
            }

        })

        phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
               // No need to implement
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No need to implement
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (phone.text.toString().trim().isEmpty()) {
                    phone.error = "Numer telefonu jest wymagany"
                } else {
                    if(phone.text.toString().trim().length == 9 && phone.text.toString().trim().contains(Regex("[0-9]"))){
                        phone.error = null
                    }else{
                        phone.error = "Numer telefonu musi mieć 9 cyfr"
                    }
                }
            }

        })
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