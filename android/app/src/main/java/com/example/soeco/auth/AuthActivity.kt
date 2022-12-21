package com.example.soeco.auth

import android.os.Bundle
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.soeco.R
import com.example.soeco.admin.AdminActivity
import com.example.soeco.auth.login.LoginActivity
import com.example.soeco.utils.viewModelFactory

class AuthActivity: AppCompatActivity(R.layout.activity_auth) {

    private val authViewModel by viewModels<AuthViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel.validateUser()
    }

    override fun onStart() {
        super.onStart()
        authViewModel.authLiveData.observe(this, Observer(::handleAuthState))
    }

    private fun handleAuthState(state: AuthViewModel.AuthState) {
        val intent = when (state) {
            AuthViewModel.AuthState.AuthSuccess -> {
                when (authViewModel.getUserRole()){
                    "admin" -> Intent(this, AdminActivity::class.java)
                    else -> Intent(this, LoginActivity::class.java)
                }
            } else -> {
                Intent(this, LoginActivity::class.java)
            }
        }
        startActivity(intent)
    }
}