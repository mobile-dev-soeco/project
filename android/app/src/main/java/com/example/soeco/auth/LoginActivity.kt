package com.example.soeco.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soeco.TAG
import com.example.soeco.databinding.LoginActivityBinding
import com.example.soeco.realmAppServices
import io.realm.mongodb.Credentials
import org.bson.Document

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var loginButton: Button
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginButton = binding.btnLogin
        emailInput = binding.etUsername
        passwordInput = binding.etPassword

        loginButton.setOnClickListener {
            onLoginClick()
        }
    }

    private fun onLoginClick() {

        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        loginButton.isEnabled = false

        val userData = Credentials.customFunction(
            Document(
                mapOf(
                    "email" to email,
                    "password" to password,
                    "role" to null
                )
            )
        )

        realmAppServices.loginAsync(userData) {
            if (it.isSuccess) {

                Log.v(TAG(), "User logged in successfully")

                loginButton.isEnabled = true

                startActivity(Intent(application, AuthActivity::class.java))
                finish()
            } else {
                val errorMessage = it.error.toString()
                Log.e(TAG(), errorMessage)
                onLoginFailed(errorMessage)
                loginButton.isEnabled = true
            }
        }
    }

    private fun onLoginFailed(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

}