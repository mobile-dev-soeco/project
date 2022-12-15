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

        val userData = listOf(email, password, userType)

        val functionsManager = realmAppServices.getFunctions(realmAppServices.currentUser())

        functionsManager.callFunctionAsync("registerUser2", userData, String::class.java) { addUser ->
            if (addUser.isSuccess){
                Log.v("User registration", "User was added to database")
                realmAppServices.emailPassword.registerUserAsync(email, password) { registerUser ->
                    if (registerUser.isSuccess) {
                        Log.v("User registration", "User was added to emailPassword auth service.")
                    } else {
                        Log.e("User registration", "emailPassword registration failed. Error: ${registerUser.error.message}")
                        functionsManager.callFunctionAsync("DeleteUser", null, Void::class.java) { deleteUser ->
                            if (deleteUser.isSuccess) {
                                Log.v("User registration", "User was deleted from database")
                            } else {
                                Log.e("User registration", "User deletion failed. Error: ${deleteUser.error.message}")
                            }
                        }
                    }
                }
            } else {
                Log.v("User registration", "Database registration failed. Error: ${deleteUser.error.message}")
            }
        }
        createUserButton.isEnabled = true
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