package com.example.soeco.ui.auth.resend

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.soeco.databinding.FragmentResendConfirmationBinding
import com.example.soeco.utils.viewModelFactory

class ResendConfirmationFragment : Fragment() {

    private val resendConfirmationViewModel by viewModels<ResendConfirmationViewModel> { viewModelFactory }

    private lateinit var binding: FragmentResendConfirmationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentResendConfirmationBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI initialisation
        binding.btnSendEmail.isEnabled = isEmailValid(binding.etEmailResend.text)

        // Observers
        resendConfirmationViewModel.resultMessageLiveData.observe(viewLifecycleOwner, Observer { message ->
            binding.tvResendMessage.text = message
        })

        resendConfirmationViewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                binding.btnSendEmail.visibility = View.INVISIBLE
                binding.pbSpinnerResend.visibility = View.VISIBLE
            } else {
                binding.btnSendEmail.visibility = View.VISIBLE
                binding.pbSpinnerResend.visibility = View.INVISIBLE
            }
        })


        // Event listeners
        binding.btnSendEmail.setOnClickListener {
            resendConfirmationViewModel.sendEmail(binding.etEmailResend.text.toString())
        }

        binding.etEmailResend.addTextChangedListener(inputWatcher)
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
            binding.btnSendEmail.isEnabled = isEmailValid(binding.etEmailResend.text)
        }
    }

}