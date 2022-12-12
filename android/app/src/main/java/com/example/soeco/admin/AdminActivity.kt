package com.example.soeco.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soeco.R
import com.example.soeco.TAG
import com.example.soeco.auth.AuthActivity
import com.example.soeco.databinding.AdminActivityBinding
import com.example.soeco.realmAppServices
import io.realm.mongodb.Credentials
import org.bson.Document

class AdminActivity: AppCompatActivity() {

    private lateinit var binding: AdminActivityBinding
    private lateinit var logoutButton: Button
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var createUserButton: Button
    private lateinit var typeSelector: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AdminActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        logoutButton = binding.btnLogout
        createUserButton = binding.btnCreateUser
        emailInput = binding.etUsername
        passwordInput = binding.etPassword
        typeSelector = binding.spUserType

        logoutButton.setOnClickListener {
            onLogout()
        }

        createUserButton.setOnClickListener {
            onCreateUser()
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.user_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeSelector.adapter = adapter
        }
    }

    private fun onCreateUser() {

        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        val userType = typeSelector.selectedItem.toString().lowercase()

        // Disable the button to avoid user clicks while the function runs.
        createUserButton.isEnabled = false

        val userData = Credentials.customFunction(
            Document(
                mapOf(
                    "email" to email,
                    "password" to password,
                    "role" to userType
                )
            )
        )

        // The logic to decide to register new user or login existing user runs in
        // the database function. loginAsync is used for both.
        realmAppServices.loginAsync(userData) {
            if (it.isSuccess){
                Log.v(TAG(), "User registration successful")

                // Reset the input fields
                emailInput.setText("")
                passwordInput.setText("")
                typeSelector.setSelection(0)
            } else {
                Toast.makeText(baseContext, it.error.message, Toast.LENGTH_LONG).show()
                Log.e(TAG(), it.error.message.toString())
            }
            // Re-enable the button when the function ends
            createUserButton.isEnabled = true
        }
    }

    private fun onLogout() {

        logoutButton.isEnabled = false

        realmAppServices.currentUser()?.logOutAsync {
            if(it.isSuccess) {
                Log.v(TAG(), "User logged out")
                logoutButton.isEnabled = true
                startActivity(Intent(application, AuthActivity::class.java))
                finish() // Destroy activity to remove it from back stack
            } else {
                logoutButton.isEnabled = true
                Log.e(TAG(), it.error.toString())
            }
        }
    }
}