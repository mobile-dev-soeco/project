package com.example.soeco.ui.admin

import android.os.Bundle
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
import com.example.soeco.databinding.FragmentAdminHomeBinding
import com.example.soeco.ui.auth.AuthViewModel
import com.example.soeco.utils.viewModelFactory

class AdminHomeFragment : Fragment() {

    private val adminHomeViewModel by viewModels<AdminHomeViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentAdminHomeBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentAdminHomeBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adminHomeViewModel.logoutLiveData.observe(viewLifecycleOwner, Observer(::handleLogout))
        adminHomeViewModel.registerLiveData.observe(viewLifecycleOwner, Observer(::handleRegister))

        binding.btnLogout.setOnClickListener {
            adminHomeViewModel.logout()
        }

        binding.btnCreateUser.setOnClickListener {
            onCreateUser()
        }
    }

    private fun handleLogout(state: AdminHomeViewModel.LogoutResult) {
        when(state){
            is AdminHomeViewModel.LogoutResult.LogoutSuccess -> {
                Toast.makeText(requireActivity().applicationContext, "Logged out successfully", Toast.LENGTH_SHORT).show()
                val action = AdminHomeFragmentDirections.actionAdminHomeFragmentToNavGraph()
                navigation.navigate(action)
            }
            is AdminHomeViewModel.LogoutResult.LogoutError -> {
                Toast.makeText(requireActivity().applicationContext, adminHomeViewModel.registerLiveData.value.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleRegister(state: AdminHomeViewModel.RegisterResult) {
        when(state){
            is AdminHomeViewModel.RegisterResult.RegisterSuccess -> {
                Toast.makeText(requireActivity().applicationContext, "User registration successful", Toast.LENGTH_SHORT).show()
            }
            is AdminHomeViewModel.RegisterResult.RegisterError -> {
                Toast.makeText(requireActivity().applicationContext, adminHomeViewModel.registerLiveData.value.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onCreateUser() {
        val email = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val userType = binding.spUserType.selectedItem.toString().lowercase()

        adminHomeViewModel.registerUser(email, password, userType)
    }

}