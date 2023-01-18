package com.example.soeco.ui.auth.forgot

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.soeco.R
import com.example.soeco.databinding.FragmentForgotPasswordBinding
import com.example.soeco.utils.viewModelFactory
import com.example.soeco.utils.AuthResult

class ForgotPasswordFragment : Fragment() {

    private val viewModel by viewModels<ForgotPasswordViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentForgotPasswordBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnResetPassword.isEnabled = isEmailValid(binding.etEmail.text)

        // Observers
        viewModel.resultLiveData.observe(viewLifecycleOwner) { result ->
            handleResult(result)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.btnResetPassword.visibility = View.INVISIBLE
                binding.pbSpinner.visibility = View.VISIBLE
            } else {
                binding.btnResetPassword.visibility = View.VISIBLE
                binding.pbSpinner.visibility = View.INVISIBLE
            }
        }

        // Event Listeners
        binding.btnResetPassword.setOnClickListener {
            viewModel.sendPasswordResetEmail(binding.etEmail.text.toString())
            binding.etEmail.setText("")
        }

        binding.etEmail.addTextChangedListener(inputWatcher)
    }

    private fun handleResult(result: AuthResult) {
        val toast = Toast.makeText(context, viewModel.resultMessage.value, Toast.LENGTH_SHORT)
        when(result){
            is AuthResult.Success -> {
                toast.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    navigation.navigate(R.id.action_forgotPassword_to_loginFragment)
                }, 1000)
                viewModel.clearResult()
            }
            is AuthResult.Error -> {
                toast.show()
                viewModel.clearResult()
            }
            else -> {}
        }
    }

    private fun isEmailValid(target: CharSequence): Boolean{
        return target.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private val inputWatcher = object: TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.btnResetPassword.isEnabled = isEmailValid(binding.etEmail.text)
        }
    }
}