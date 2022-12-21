package com.example.soeco.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.soeco.admin.AdminActivity
import com.example.soeco.utils.viewModelFactory

class AuthFragment: Fragment() {

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
                    "admin" -> Intent(requireActivity().applicationContext, AdminActivity::class.java)
                    else -> Intent(requireActivity().applicationContext, LoginActivity::class.java)
                }
            } else -> {
                Intent(requireActivity().applicationContext, LoginActivity::class.java)
            }
        }
        startActivity(intent)
    }

}