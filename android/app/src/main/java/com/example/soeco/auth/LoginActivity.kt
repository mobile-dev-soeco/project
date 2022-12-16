package com.example.soeco.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soeco.R
import com.example.soeco.TAG
import com.example.soeco.databinding.LoginActivityBinding
import com.example.soeco.realmAppServices
import io.realm.mongodb.Credentials

class LoginActivity: AppCompatActivity(R.layout.login_activity) {

    private lateinit var binding: LoginActivityBinding
    private lateinit var loginButton: Button
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var forgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loginButton = binding.btnLogin
        emailInput = binding.etUsername
        passwordInput = binding.etPassword
        forgotPassword = binding.tvForgotPassword

        loginButton.setOnClickListener {
            onLoginClick()
        }

        forgotPassword.setOnClickListener {
            onForgotPasswordClick()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun onLoginClick() {

        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        loginButton.isEnabled = false

        val userData = Credentials.emailPassword(email, password)

        realmAppServices.loginAsync(userData) {
            if (it.isSuccess) {

                Log.v(TAG(), "User logged in successfully")

                loginButton.isEnabled = true

                startActivity(Intent(application, AuthActivity::class.java))
                finish()
            } else {
                Log.e(TAG(), it.error.toString())
                onLoginFailed("${it.error.message}")
                loginButton.isEnabled = true
            }
        }
    }

    fun onForgotPasswordClick() {
        Log.v(TAG(), "Clicked")
        startActivity(Intent(application, ForgotPasswordActivity::class.java))
    }

    fun onLoginFailed(message: String) {
        Toast.makeText(application, message, Toast.LENGTH_LONG).show()
    }

}