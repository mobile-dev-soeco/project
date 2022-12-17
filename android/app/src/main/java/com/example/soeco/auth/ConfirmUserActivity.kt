package com.example.soeco.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.soeco.R
import com.example.soeco.TAG
import com.example.soeco.databinding.ConfirmUserActivityBinding
import com.example.soeco.realmAppServices

class ConfirmUserActivity: AppCompatActivity() {

    private lateinit var binding: ConfirmUserActivityBinding
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var buttonSpinner: ProgressBar
    private lateinit var actionButton: Button
    private lateinit var emailInput: EditText
    private lateinit var actionForm: ConstraintLayout
    private lateinit var responseText: TextView

    private var token: String? = null
    private var tokenId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ConfirmUserActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingSpinner = binding.pbSpinner
        buttonSpinner = binding.pbButtonSpinner
        actionButton = binding.btnAction
        emailInput = binding.etEmail
        actionForm = binding.clActionForm
        responseText = binding.tvResponseText

        actionButton.setOnClickListener {
            handleActionButtonClick()
        }
    }

    override fun onResume() {
        super.onResume()

        // Get intent data
        val data: Uri? = intent?.data

        // Get token parameters from query
        token = data?.getQueryParameter("token")
        tokenId = data?.getQueryParameter("tokenId")

        if(token != null && tokenId != null) {
            realmAppServices.emailPassword.confirmUserAsync(token, tokenId) {
                if (it.isSuccess) {
                    Toast.makeText(
                        this,
                        getString(R.string.email_confirm_success_msg),
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(application, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, it.error.message.toString(), Toast.LENGTH_LONG).show()
                    actionForm.visibility = View.VISIBLE
                    loadingSpinner.visibility = View.GONE
                }
            }
        } else {
            Toast.makeText(this, "Token is invalid", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun handleActionButtonClick() {

        actionButton.visibility = View.GONE
        buttonSpinner.visibility = View.VISIBLE

        val email = emailInput.text.toString()

        if (email.isNotEmpty()) {
            // Attempt to send a new confirmation email
            realmAppServices.emailPassword.resendConfirmationEmailAsync(email) {
                if (it.isSuccess) {
                    Toast.makeText(this, "Email sent to $email", Toast.LENGTH_SHORT).show()
                } else {
                    // User may already be confirmed and should be redirected to login
                    if (it.error.errorCode.name == "USER_ALREADY_CONFIRMED"){
                        Toast.makeText(application, getString(R.string.account_already_confirmed), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(application, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, it.error.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                actionButton.visibility = View.VISIBLE
                buttonSpinner.visibility = View.GONE
            }
        } else {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            actionButton.visibility = View.VISIBLE
            buttonSpinner.visibility = View.GONE
        }
    }
}