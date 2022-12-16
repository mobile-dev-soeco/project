package com.example.soeco.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soeco.databinding.ResetPasswordActivityBinding
import com.example.soeco.realmAppServices

class ResetPasswordActivity: AppCompatActivity() {

    private lateinit var binding: ResetPasswordActivityBinding
    private lateinit var passwordInput: EditText
    private lateinit var submitButton: Button

    private var token: String? = null
    private var tokenId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ResetPasswordActivityBinding.inflate(layoutInflater)

        passwordInput = binding.etPassword
        submitButton = binding.btnSubmit

        submitButton.setOnClickListener {
            handleSubmitClick()
        }

        setContentView(binding.root)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        // Get intent data
        val data: Uri? = intent?.data

        // Get token parameters from query
        token = data?.getQueryParameter("token")
        tokenId = data?.getQueryParameter("tokenId")
    }

    private fun handleSubmitClick() {
        submitButton.isEnabled = false

        if (passwordInput.text.isNotEmpty()) {

            val newPassword = passwordInput.text.toString()

            realmAppServices.emailPassword.resetPasswordAsync(token, tokenId, newPassword) {

                if (it.isSuccess) {
                    Toast.makeText(application, "Password has been reset", Toast.LENGTH_LONG).show()
                    startActivity(Intent(application, LoginActivity::class.java))
                    submitButton.isEnabled = true
                    finish()
                } else {
                    Toast.makeText(this, it.error.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
        }
    }
}