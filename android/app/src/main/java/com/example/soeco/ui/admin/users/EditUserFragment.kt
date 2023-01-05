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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.soeco.R
import com.example.soeco.databinding.FragmentEditUserBinding
import com.example.soeco.utils.viewModelFactory

class EditUserFragment : Fragment() {

    private val usersViewModel by viewModels<UsersViewModel> { viewModelFactory }
    private val navigation by lazy { findNavController() }

    private lateinit var binding: FragmentEditUserBinding

    private lateinit var adapter: ArrayAdapter<CharSequence?>

    val args: EditUserFragmentArgs by navArgs()

    val MAX_INPUT_LENGTH = 50

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentEditUserBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dropdown menu
        adapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.user_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spUserType.adapter = adapter
        }

        // Get position of user role in dropdown menu
        val position = adapter.getPosition(args.role.capitalize())

        binding.apply {
            etName.setText(args.name)
            etSurname.setText(args.surname)
            spUserType.setSelection(position) // Set selected to the users role
            etName.addTextChangedListener(inputWatcher)
            etSurname.addTextChangedListener(inputWatcher)
        }

        binding.btnUpdate.isEnabled = validateInputs()

        binding.btnUpdate.setOnClickListener {
            updateUser(
                args.email,
                binding.etName.text.toString(),
                binding.etSurname.text.toString(),
                binding.spUserType.selectedItem.toString()
            )
        }

    }

    private fun updateUser(
        email: String,
        firstname: String,
        lastname: String,
        role: String
    ) {
        usersViewModel.updateUser(
            email,
            firstname,
            lastname,
            role,
            onSuccess = {
                Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
                navigation.navigate(R.id.action_editUserFragment_to_usersFragment)
            },
            onError = {
                Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun validateInputs(): Boolean {
        with(binding) {
            return etName.text.isNotEmpty() &&
                    etSurname.text.isNotEmpty() &&
                    etName.text.length <= MAX_INPUT_LENGTH &&
                    etSurname.text.length <= MAX_INPUT_LENGTH
        }
    }

    private val inputWatcher = object: TextWatcher {

        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.btnUpdate.isEnabled = validateInputs()
        }
    }

}