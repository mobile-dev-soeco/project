package com.example.soeco.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.soeco.R
import com.example.soeco.databinding.FragmentLoginBinding
import com.example.soeco.utils.ActionResult
import com.example.soeco.utils.viewModelFactory

class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentLoginBinding

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
            val isButtonVisible = if (isLoading) View.INVISIBLE else View.VISIBLE
            binding.pbSpinner.visibility = isSpinnerVisible
            binding.btnLogin.visibility = isButtonVisible
        }

        // Set up observers
        viewModel.loginLiveData.observe(viewLifecycleOwner, Observer(::handleLoginResult))
        viewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)

        // Login button can not be clicked until user input is valid
        binding.btnLogin.isEnabled = isUserInputValid()

        // Change listeners on text fields
        binding.etEmail.addTextChangedListener(inputWatcher)
        binding.etPassword.addTextChangedListener(inputWatcher)

        // Button click listeners
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        binding.tvForgotPassword.setOnClickListener {
            navigation.navigate(R.id.action_loginFragment_to_forgotPassword)
        }
    }

    /** Login logic **/

    private fun isUserInputValid() =
        (isEmailValid(binding.etEmail.text.toString()) && isPasswordValid(binding.etPassword.text.toString()))

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

    private fun handleLoginResult(result: ActionResult) {
        when(result){
            is ActionResult.Success -> {
                Toast.makeText(context, viewModel.loginResultMessage.value, Toast.LENGTH_SHORT).show()
                navigation.navigate(R.id.action_loginFragment_to_authFragment)
                viewModel.clearResult()
            }
            is ActionResult.Error -> {
                Toast.makeText(context, viewModel.loginResultMessage.value, Toast.LENGTH_SHORT).show()
                viewModel.clearResult()
            }
            else -> {}
        }
    }
}