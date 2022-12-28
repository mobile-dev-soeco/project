package com.example.soeco.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.soeco.R

class AuthActivity: AppCompatActivity(R.layout.activity_auth) {

    private val navigation: NavController by lazy { findNavController(R.id.auth_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigation.handleDeepLink(intent)
    }

}