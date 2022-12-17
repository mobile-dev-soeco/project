package com.example.soeco.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soeco.databinding.ResetPasswordActivityBinding
import com.example.soeco.realmAppServices

class ResetPasswordActivity: AppCompatActivity() {

    private lateinit var binding: ResetPasswordActivityBinding
    private lateinit var passwordInput: EditText
    private lateinit var submitButton: Button
    private lateinit var spinner: ProgressBar

    private var token: String? = null
    private var tokenId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ResetPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        passwordInput = binding.etPassword
        submitButton = binding.btnSubmit
        spinner = binding.pbSpinner

        submitButton.setOnClickListener {
            handleSubmitClick()
        }
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
        submitButton.visibility = View.GONE
        spinner.visibility = View.VISIBLE

        if (token.isNullOrEmpty() || tokenId.isNullOrEmpty()){
            Toast.makeText(this, "Invalid reset token", Toast.LENGTH_SHORT).show()
        } else {
            val password = passwordInput.text.toString()

            if (password.isNotEmpty()) {

                realmAppServices.emailPassword.resetPasswordAsync(token, tokenId, password) {
                    if (it.isSuccess) {
                        Toast.makeText(application, "Password has been reset", Toast.LENGTH_LONG).show()
                        startActivity(Intent(application, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, it.error.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    submitButton.visibility = View.VISIBLE
                    spinner.visibility = View.GONE
                }
            } else {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
                submitButton.visibility = View.VISIBLE
                spinner.visibility = View.GONE
            }
        }
    }
}