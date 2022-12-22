package com.example.soeco.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.soeco.RealmApplication
import com.example.soeco.ui.admin.AdminHomeViewModel
import com.example.soeco.ui.auth.login.LoginViewModel
import com.example.soeco.ui.auth.AuthViewModel
import com.example.soeco.ui.auth.confirm.ConfirmUserViewModel
import com.example.soeco.ui.auth.forgot.ForgotPasswordViewModel
import com.example.soeco.ui.auth.reset.ResetPaswordViewModel
import com.example.soeco.ui.dashboard.DashBoardViewModel

val viewModelFactory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T =
        with(modelClass) {
            // Get application object from extras
            val application = checkNotNull(extras[APPLICATION_KEY]) as RealmApplication
            val savedStateHandle = extras.createSavedStateHandle()
            val repository = application.repository
            when {
                // Can add any viewModels here that need to access the repository
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository, savedStateHandle)
                isAssignableFrom(AuthViewModel::class.java) ->
                    AuthViewModel(repository, savedStateHandle)
                isAssignableFrom(ConfirmUserViewModel::class.java) ->
                    ConfirmUserViewModel(repository, savedStateHandle)
                isAssignableFrom(ResetPaswordViewModel::class.java) ->
                    ResetPaswordViewModel(repository, savedStateHandle)
                isAssignableFrom(ForgotPasswordViewModel::class.java) ->
                    ForgotPasswordViewModel(repository, savedStateHandle)
                isAssignableFrom(AdminHomeViewModel::class.java) ->
                    AdminHomeViewModel(repository, savedStateHandle)
                isAssignableFrom(DashBoardViewModel::class.java) ->
                    DashBoardViewModel(repository, savedStateHandle)
                else ->
                    throw IllegalArgumentException("Unknown viewModel class: ${modelClass.name}")
            }
        } as T
}