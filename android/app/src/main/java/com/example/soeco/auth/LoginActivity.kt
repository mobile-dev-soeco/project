package com.example.soeco.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
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
    private lateinit var spinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loginButton = binding.btnLogin
        emailInput = binding.etUsername
        passwordInput = binding.etPassword
        forgotPassword = binding.tvForgotPassword
        spinner = binding.pbSpinner

        loginButton.setOnClickListener {
            onLoginClick()
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(application, ForgotPasswordActivity::class.java))
        }
    }

    private fun onLoginClick() {

        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        loginButton.visibility = View.GONE
        spinner.visibility = View.VISIBLE

        if (email.isNotEmpty() && password.isNotEmpty()){

            val userData = Credentials.emailPassword(email, password)

            realmAppServices.loginAsync(userData) {
                if (it.isSuccess) {
                    // Log to console
                    Log.v(TAG(), "User logged in successfully")
                    // Message to user
                    Toast.makeText(application, "Login successful", Toast.LENGTH_SHORT).show()
                    // Navigate to Auth to authenticate user to correct activity
                    startActivity(Intent(application, AuthActivity::class.java))
                    // Destroy the activity to remove from the back stack
                    finish()
                } else {
                    Log.e(TAG(), it.error.toString())
                    onLoginFailed("${it.error.message}")
                    loginButton.visibility = View.VISIBLE
                    spinner.visibility = View.GONE
                }
            }
        } else {
            onLoginFailed("Email and password required!")
            loginButton.visibility = View.VISIBLE
            spinner.visibility = View.GONE
        }
    }

    fun onLoginFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}