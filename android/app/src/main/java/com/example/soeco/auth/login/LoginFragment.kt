package com.example.soeco.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.soeco.databinding.FragmentLoginBinding
import com.example.soeco.utils.viewModelFactory

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentLoginBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        // Observer to update visibility of button and progress spinner when logging in
        val loadingObserver = Observer<Boolean> { isLoading ->
            val isSpinnerVisible = if (isLoading) View.VISIBLE else View.GONE
            val isButtonVisible = if (isLoading) View.GONE else View.VISIBLE
            binding.pbSpinner.visibility = isSpinnerVisible
            binding.btnLogin.visibility = isButtonVisible
        }

        val loginMessageObserver = Observer<String> { message ->
            Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        // Set up observers
        loginViewModel.loginLiveData.observe(viewLifecycleOwner, Observer(::handleLoginResult))
        loginViewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)
        loginViewModel.loginResultMessage.observe(viewLifecycleOwner, loginMessageObserver)

        // Login button can not be clicked until user input is valid
        binding.btnLogin.isEnabled = isUserInputValid()

        // Change listeners on text fields
        binding.etEmail.addTextChangedListener(inputWatcher)
        binding.etPassword.addTextChangedListener(inputWatcher)

        // Button click listeners
        binding.btnLogin.setOnClickListener {
            loginViewModel.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }

    /** Login logic **/

    private fun isUserInputValid() =
        (isEmailValid(binding.etEmail.text) && isPasswordValid(binding.etPassword.text))

    private fun isEmailValid(target: CharSequence): Boolean{
        return target.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun isPasswordValid(target: CharSequence): Boolean{
        return target.isNotEmpty() && target.length >= 6
    }

    private val inputWatcher = object: TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.btnLogin.isEnabled = isUserInputValid()
        }
    }

    private fun handleLoginResult(loginResult: LoginViewModel.LoginResult) {
        when(loginResult){
            is LoginViewModel.LoginResult.LoginSuccess -> {
                Log.v("Login", "Login success")
                // Navigate back to splash screen
            }
            is LoginViewModel.LoginResult.LoginError -> {
                Log.v("Login", "Login failed: ${loginViewModel.loginLiveData.value}")
                // Show an error to the user
            }
        }
    }
}