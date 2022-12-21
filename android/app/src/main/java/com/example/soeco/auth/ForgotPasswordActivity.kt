package com.example.soeco.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soeco.databinding.ForgotPasswordActivityBinding
import com.example.soeco.realmAppServices

class ForgotPasswordActivity: AppCompatActivity() {

    private lateinit var binding: ForgotPasswordActivityBinding
    private lateinit var emailInput: EditText
    private lateinit var resetButton: Button
    private lateinit var spinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ForgotPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailInput = binding.etEmail
        resetButton = binding.btnResetPassword
        spinner = binding.pbSpinner

        resetButton.visibility = View.VISIBLE
        spinner.visibility = View.GONE

        resetButton.setOnClickListener {
            onResetClick()
        }
    }

    private fun onResetClick() {

        resetButton.visibility = View.GONE
        spinner.visibility = View.VISIBLE

        if (emailInput.text.isNotEmpty()) {
            val email = emailInput.text.toString()
            realmAppServices.emailPassword.sendResetPasswordEmailAsync(email){
                if (it.isSuccess) {
                    Toast.makeText(application, "Reset link sent to $email", Toast.LENGTH_LONG).show()
                    startActivity(Intent(application, AuthActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(application, it.error.message.toString(), Toast.LENGTH_SHORT).show()
                }
                resetButton.visibility = View.VISIBLE
                spinner.visibility = View.GONE
            }
        }
    }
}