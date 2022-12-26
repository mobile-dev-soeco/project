package com.example.soeco.ui.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

        // Observer to update visibility of button and progress spinner when logging in
        val loadingObserver = Observer<Boolean> { isLoading ->
            val isSpinnerVisible = if (isLoading) View.VISIBLE else View.GONE
            val isButtonVisible = if (isLoading) View.INVISIBLE else View.VISIBLE
            binding.pbSpinner.visibility = isSpinnerVisible
            binding.btnCreateUser.visibility = isButtonVisible
        }

        // Initial state
        binding.btnCreateUser.isEnabled = isUserInputValid()

        // Observers
        adminHomeViewModel.logoutLiveData.observe(viewLifecycleOwner, Observer(::handleLogout))
        adminHomeViewModel.registerLiveData.observe(viewLifecycleOwner, Observer(::handleRegister))
        adminHomeViewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)

        // Event listeners
        binding.btnLogout.setOnClickListener {
            adminHomeViewModel.logout()
        }

        binding.btnCreateUser.setOnClickListener {
            onCreateUserClick()
        }

        binding.etUsername.addTextChangedListener(inputWatcher)
        binding.etPassword.addTextChangedListener(inputWatcher)

        // Dropdown menu
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.user_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spUserType.adapter = adapter
        }
    }

    private fun onCreateUserClick() {
        val email = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val userType = binding.spUserType.selectedItem.toString().lowercase()

        adminHomeViewModel.registerUser(email, password, userType)
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

    private fun isUserInputValid() =
        (isEmailValid(binding.etUsername.text) && isPasswordValid(binding.etPassword.text))

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
            binding.btnCreateUser.isEnabled = isUserInputValid()
        }
    }

}