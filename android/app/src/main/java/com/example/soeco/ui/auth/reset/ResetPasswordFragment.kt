package com.example.soeco.ui.auth.reset

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.soeco.R
import com.example.soeco.databinding.FragmentResetPaswordBinding
import com.example.soeco.utils.ActionResult
import com.example.soeco.utils.viewModelFactory

class ResetPasswordFragment : Fragment() {

    private val viewModel by viewModels<ResetPaswordViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentResetPaswordBinding
    private var token: String? = null
    private var tokenId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentResetPaswordBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.isEnabled = isPasswordValid(binding.etPassword.text)

        viewModel.resetResult.observe(viewLifecycleOwner, Observer { result ->
            handleResult(result)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading){
                binding.btnSubmit.visibility = View.INVISIBLE
                binding.pbSpinner.visibility = View.VISIBLE
            } else {
                binding.btnSubmit.visibility = View.VISIBLE
                binding.pbSpinner.visibility = View.INVISIBLE
            }
        })

        // Event Listeners
        binding.btnSubmit.setOnClickListener {
            viewModel.resetPassword(
                token.toString(),
                tokenId.toString(),
                binding.etPassword.text.toString()
            )
        }

        binding.etPassword.addTextChangedListener(inputWatcher)
    }

    override fun onResume() {
        super.onResume()

        // Get data from intent deep link
        val intentData = requireActivity().intent.data
        token = intentData?.getQueryParameter("token")
        tokenId = intentData?.getQueryParameter("tokenId")
        Log.v("Intent data", "token: $token, tokenId: $tokenId")
    }

    private fun handleResult(result: ActionResult) {
        val toast = Toast.makeText(context, viewModel.resultMessage.value, Toast.LENGTH_SHORT)
        when(result){
            is ActionResult.Success -> {
                toast.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    navigation.navigate(R.id.action_resetPasword_to_loginFragment)
                }, 1000)
                viewModel.clearResult()
            }
            is ActionResult.Error -> {
                toast.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    navigation.navigate(R.id.action_resetPasword_to_forgotPassword)
                }, 1000)
                viewModel.clearResult()
            }
            else -> {}
        }
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
            binding.btnSubmit.isEnabled = isPasswordValid(binding.etPassword.text)
        }
    }
}
