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
import com.example.soeco.R
import com.example.soeco.databinding.ConfirmUserActivityBinding
import com.example.soeco.realmAppServices

// TODO: Add an option to login under the resend email button.

class ConfirmUserActivity: AppCompatActivity() {

    private lateinit var binding: ConfirmUserActivityBinding
    private lateinit var spinner: ProgressBar
    private lateinit var actionButton: Button
    private lateinit var emailInput: EditText
    private lateinit var actionForm: LinearLayout
    private lateinit var responseText: TextView

    private var token: String? = null
    private var tokenId: String? = null
    private var userConfirmed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ConfirmUserActivityBinding.inflate(layoutInflater)

        spinner = binding.pbSpinner
        actionButton = binding.btnAction
        emailInput = binding.etEmail
        actionForm = binding.llActionForm
        responseText = binding.tvResponseText

        actionButton.setOnClickListener {
            handleActionButtonClick()
        }
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setUiAwaitingResponse()
    }

    override fun onResume() {
        super.onResume()

        // Get intent data
        val data: Uri? = intent?.data

        // Get token parameters from query
        token = data?.getQueryParameter("token")
        tokenId = data?.getQueryParameter("tokenId")

        if(token != null && tokenId != null){

            if (!userConfirmed) {

                realmAppServices.emailPassword.confirmUserAsync(token, tokenId) {
                    if(it.isSuccess){
                        Log.v("User Confirmation", "User confirmed")
                        Toast.makeText(this, getString(R.string.email_confirm_success_msg), Toast.LENGTH_LONG).show()
                        startActivity(Intent(application, AuthActivity::class.java))
                        finish()
                    }
                    else {
                        Log.e("User Confirmation", "Error: ${it.error.message}")
                        setUiFailureResponse()
                    }
                }
            }
            else {
                setUiFailureResponse()
            }
        }
        else {
            setUiFailureResponse()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        userConfirmed = false
    }

    private fun setUiAwaitingResponse() {
        userConfirmed = false
        spinner.visibility = View.VISIBLE
        actionForm.visibility = View.GONE
        actionButton.visibility = View.GONE
        emailInput.visibility = View.GONE
    }

    private fun setUiFailureResponse() {
        userConfirmed = false
        spinner.visibility = View.GONE
        actionButton.visibility = View.VISIBLE
        emailInput.visibility = View.VISIBLE
        actionForm.visibility = View.VISIBLE
        responseText.text = FAILURE_MSG
        actionButton.text = getString(R.string.resend_email)
    }

    private fun handleActionButtonClick() {
        if(userConfirmed){
            startActivity(Intent(application, LoginActivity::class.java))
            finish()
        } else {
            if (emailInput.text.isNotEmpty()) {
                realmAppServices.emailPassword.resendConfirmationEmail(emailInput.text.toString())
            } else {
                Toast.makeText(this, getString(R.string.empty_email_warning), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val SUCCESS_MSG = "User account confirmed, please login!"
        const val FAILURE_MSG = "Email confirmation failed. This could mean you have already confirmed your email, or that the token has expired and you need a new one."
    }

}