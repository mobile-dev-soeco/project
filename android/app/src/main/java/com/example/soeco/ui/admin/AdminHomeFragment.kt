package com.example.soeco.ui.admin

import android.os.Bundle
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

        binding.btnLogout.setOnClickListener {
            adminHomeViewModel.logout()
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
                Toast.makeText(requireActivity().applicationContext, "Logout Failed", Toast.LENGTH_SHORT).show()
                // TODO: Handle message using LogoutError object
            }
        }
    }

}

// TODO: Old logic below to be applied to the new layouts when ready

//    private fun onCreateUser() {
//
//        val email = emailInput.text.toString()
//        val password = passwordInput.text.toString()
//        val userType = typeSelector.selectedItem.toString().lowercase()
//
//        // Disable the button to avoid user clicks while the function runs.
//        createUserButton.isEnabled = false
//
//        val userData = listOf(email, password, userType)
//
//        val functionsManager = realmAppServices.getFunctions(realmAppServices.currentUser())
//
//        // Add a user to the database
//        functionsManager.callFunctionAsync("registerUser2", userData, String::class.java) { addUser ->
//            if (addUser.isSuccess){
//                // Register the user to realm authentication
//                Log.v("User registration", "User was added to database")
//                realmAppServices.emailPassword.registerUserAsync(email, password) { registerUser ->
//                    if (registerUser.isSuccess) {
//                        Log.v("User registration", "User was added to emailPassword auth service.")
//                    } else {
//                        // User could not be added to realm authentication. Remove the user from the database.
//                        Log.e("User registration", "emailPassword registration failed. Error: ${registerUser.error.message}")
//                        functionsManager.callFunctionAsync("DeleteUser", null, Void::class.java) { deleteUser ->
//                            if (deleteUser.isSuccess) {
//                                Log.v("User registration", "User was deleted from database")
//                            } else {
//                                Log.e("User registration", "User deletion failed. Error: ${deleteUser.error.message}")
//                            }
//                        }
//                    }
//                }
//            } else {
//                // User could not be added to database.
//                Log.v("User registration", "Database registration failed. Error: ${addUser.error.message}")
//            }
//        }
//        createUserButton.isEnabled = true
//    }
//
//    private fun onLogout() {
//
//        logoutButton.isEnabled = false
//
//        realmAppServices.currentUser()?.logOutAsync {
//            if(it.isSuccess) {
//                Log.v(TAG(), "User logged out")
//                logoutButton.isEnabled = true
//                startActivity(Intent(application, AuthActivity::class.java))
//                finish() // Destroy activity to remove it from back stack
//            } else {
//                logoutButton.isEnabled = true
//                Log.e(TAG(), it.error.toString())
//            }
//        }
//    }