package com.example.soeco.ui.auth.confirm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.soeco.R
import com.example.soeco.databinding.FragmentConfirmUserBinding
import com.example.soeco.utils.viewModelFactory
import java.util.*
import kotlin.concurrent.timerTask

class ConfirmUserFragment : Fragment() {

    private val confirmUserViewModel by viewModels<ConfirmUserViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentConfirmUserBinding
    private var token: String? = null
    private var tokenId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentConfirmUserBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observers
        val navObserver = Observer<String> { destination ->
            when(destination){
                "login" -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigation.navigate(R.id.action_confirmUserFragment_to_loginFragment)
                    }, 2000)
                }
                "resend" -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigation.navigate(R.id.action_confirmUserFragment_to_resendConfirmation)
                    }, 2000)
                }
            }
        }

        val messageObserver = Observer<String> { message ->
            binding.tvResultText.text = message
        }

        confirmUserViewModel.shouldNavigateTo.observe(viewLifecycleOwner, navObserver)
        confirmUserViewModel.resultTextLiveData.observe(viewLifecycleOwner, messageObserver)

    }

    override fun onResume() {
        super.onResume()

        // Get data from intent deep link
        val intentData = requireActivity().intent.data
        token = intentData?.getQueryParameter("token")
        tokenId = intentData?.getQueryParameter("tokenId")
        Log.v("Intent data", "token: $token, tokenId: $tokenId")

        if (token.isNullOrEmpty() || tokenId.isNullOrEmpty()) {
            confirmUserViewModel.isInvalidToken()
        } else {
            confirmUserViewModel.confirmUser(token.toString(), tokenId.toString())
        }
    }

}

//private lateinit var binding: ConfirmUserActivityBinding
//private lateinit var loadingSpinner: ProgressBar
//private lateinit var buttonSpinner: ProgressBar
//private lateinit var actionButton: Button
//private lateinit var emailInput: EditText
//private lateinit var actionForm: ConstraintLayout
//private lateinit var responseText: TextView
//
//private var token: String? = null
//private var tokenId: String? = null
//
//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//
//    binding = ConfirmUserActivityBinding.inflate(layoutInflater)
//    setContentView(binding.root)
//
//    loadingSpinner = binding.pbSpinner
//    buttonSpinner = binding.pbButtonSpinner
//    actionButton = binding.btnAction
//    emailInput = binding.etEmail
//    actionForm = binding.clActionForm
//    responseText = binding.tvResponseText
//
//    actionButton.setOnClickListener {
//        handleActionButtonClick()
//    }
//}
//
//override fun onResume() {
//    super.onResume()
//
//    // Get intent data
//    val data: Uri? = intent?.data
//
//    // Get token parameters from query
//    token = data?.getQueryParameter("token")
//    tokenId = data?.getQueryParameter("tokenId")
//
//    if(token != null && tokenId != null) {
//        realmAppServices.emailPassword.confirmUserAsync(token, tokenId) {
//            if (it.isSuccess) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.email_confirm_success_msg),
//                    Toast.LENGTH_LONG
//                ).show()
//                startActivity(Intent(application, LoginActivity::class.java))
//                finish()
//            } else {
//                Toast.makeText(this, it.error.message.toString(), Toast.LENGTH_LONG).show()
//                actionForm.visibility = View.VISIBLE
//                loadingSpinner.visibility = View.GONE
//            }
//        }
//    } else {
//        Toast.makeText(this, "Token is invalid", Toast.LENGTH_SHORT).show()
//    }
//}
//
//override fun onNewIntent(intent: Intent?) {
//    super.onNewIntent(intent)
//    setIntent(intent)
//}
//
//private fun handleActionButtonClick() {
//
//    actionButton.visibility = View.GONE
//    buttonSpinner.visibility = View.VISIBLE
//
//    val email = emailInput.text.toString()
//
//    if (email.isNotEmpty()) {
//        // Attempt to send a new confirmation email
//        realmAppServices.emailPassword.resendConfirmationEmailAsync(email) {
//            if (it.isSuccess) {
//                Toast.makeText(this, "Email sent to $email", Toast.LENGTH_SHORT).show()
//            } else {
//                // User may already be confirmed and should be redirected to login
//                if (it.error.errorCode.name == "USER_ALREADY_CONFIRMED"){
//                    Toast.makeText(application, getString(R.string.account_already_confirmed), Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(application, LoginActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this, it.error.message.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//            actionButton.visibility = View.VISIBLE
//            buttonSpinner.visibility = View.GONE
//        }
//    } else {
//        Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
//        actionButton.visibility = View.VISIBLE
//        buttonSpinner.visibility = View.GONE
//    }
//}