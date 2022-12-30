package com.example.soeco.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.soeco.R
import com.example.soeco.databinding.FragmentAuthBinding
import com.example.soeco.utils.viewModelFactory
import com.example.soeco.ui.auth.AuthViewModel.AuthState.AuthSuccess

class AuthFragment: Fragment() {

    private val authViewModel by viewModels<AuthViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentAuthBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Observer auth state in view model
        authViewModel.authLiveData.observe(viewLifecycleOwner, Observer(::handleAuthState))
        // Initialize view model auth state by calling validate user method
        authViewModel.validateUser() // Check if user is logged in
    }

    // Handle the auth state change
    private fun handleAuthState(state: AuthViewModel.AuthState) {
        var destination: Int = R.id.action_authFragment_to_startFragment
        if (state == AuthSuccess) {
            when(authViewModel.getUserRole()){
                "admin" -> destination = R.id.action_authFragment_to_adminActivity
                "carpenter" -> destination = R.id.action_authFragment_to_carpentryActivity
                "delivery" -> destination = R.id.action_authFragment_to_deliveryActivity
            }
            Handler(Looper.getMainLooper()).postDelayed({
                navigation.navigate(destination)
                requireActivity().finish()
            }, 1000)
        } else {
            navigation.navigate(R.id.action_authFragment_to_startFragment)
        }
    }
}