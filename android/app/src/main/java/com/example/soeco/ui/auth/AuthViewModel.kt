package com.example.soeco.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Repository

class AuthViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _authLiveData = MutableLiveData<AuthState>()
    val authLiveData: LiveData<AuthState>
        get() = _authLiveData

    fun validateUser() {
        _authLiveData.value = if (repository.restoreLoggedInUser() == null) {
            AuthState.AuthDenied
        } else {
            AuthState.AuthSuccess
        }
    }

    fun getUserRole(): String {
        return repository.getUserRole()
    }

    sealed class AuthState {
        object AuthSuccess: AuthState()
        object AuthDenied: AuthState()
    }

}