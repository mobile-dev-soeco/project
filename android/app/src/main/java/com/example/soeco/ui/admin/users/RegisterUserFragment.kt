package com.example.soeco.ui.admin.users

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.soeco.R
import com.example.soeco.databinding.FragmentRegisterUserBinding
import com.example.soeco.utils.viewModelFactory

class RegisterUserFragment : Fragment() {

    private val registerUserViewModel by viewModels<RegisterUserViewModel> { viewModelFactory }
    private lateinit var binding: FragmentRegisterUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentRegisterUserBinding.inflate(inflater, container, false)
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
        registerUserViewModel.registerLiveData.observe(viewLifecycleOwner, Observer(::handleRegister))
        registerUserViewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)

        binding.btnCreateUser.setOnClickListener {
            onCreateUserClick()
        }

        binding.etEmail.addTextChangedListener(inputWatcher)
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
        val firstname = binding.etName.text.toString()
        val lastname = binding.etSurname.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val userType = binding.spUserType.selectedItem.toString().lowercase()

        registerUserViewModel.registerUser(firstname, lastname, email, password, userType)
    }

    private fun handleRegister(state: RegisterUserViewModel.RegisterResult) {
        when(state){
            is RegisterUserViewModel.RegisterResult.RegisterSuccess -> {
                binding.apply {
                    etName.setText("")
                    etSurname.setText("")
                    etEmail.setText("")
                    etPassword.setText("")
                    spUserType.setSelection(0)
                }
                Toast.makeText(requireActivity().applicationContext, "User Registration successful", Toast.LENGTH_SHORT).show()
            }
            is RegisterUserViewModel.RegisterResult.RegisterError -> {
                Toast.makeText(requireActivity().applicationContext, registerUserViewModel.registerLiveData.value.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

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
            binding.btnCreateUser.isEnabled = isUserInputValid()
        }
    }
}