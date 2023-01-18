package com.example.soeco.ui.auth.confirm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.soeco.R
import com.example.soeco.databinding.FragmentConfirmUserBinding
import com.example.soeco.utils.AuthResult
import com.example.soeco.utils.viewModelFactory

class ConfirmUserFragment : Fragment() {

    private val viewModel by viewModels<ConfirmUserViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentConfirmUserBinding
    private var token: String? = null
    private var tokenId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentConfirmUserBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.confirmUserResult.observe(viewLifecycleOwner) { result ->
            handleResult(result)
        }
    }

    override fun onResume() {
        super.onResume()

        // Get data from intent deep link
        val intentData = requireActivity().intent.data
        token = intentData?.getQueryParameter("token")
        tokenId = intentData?.getQueryParameter("tokenId")

        if (token.isNullOrEmpty() || tokenId.isNullOrEmpty()) {
            viewModel.isInvalidToken()
        } else {
            viewModel.confirmUser(token.toString(), tokenId.toString())
        }
    }

    private fun handleResult(result: AuthResult) {
        val toast = Toast.makeText(context, viewModel.resultMessage.value, Toast.LENGTH_SHORT)
        when(result){
            is AuthResult.Success -> {
                toast.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    navigation.navigate(R.id.action_confirmUserFragment_to_loginFragment)
                }, 1000)
                viewModel.clearResult()
            }
            is AuthResult.Error -> {
                toast.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    navigation.navigate(R.id.action_confirmUserFragment_to_resendConfirmation)
                }, 1000)
                viewModel.clearResult()
            }
            else -> {}
        }
    }
}