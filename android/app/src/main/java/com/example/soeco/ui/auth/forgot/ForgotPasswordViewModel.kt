package com.example.soeco.ui.auth.forgot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Repository
import com.example.soeco.utils.AuthResult

class ForgotPasswordViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultLiveData = MutableLiveData<AuthResult>()
    val resultLiveData: LiveData<AuthResult>
        get() = _resultLiveData

    private val _resultMessage = MutableLiveData<String>()
    val resultMessage: LiveData<String>
        get() = _resultMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun sendPasswordResetEmail(email: String) {
        repository.sendPasswordResetEmail(
            email,
            sendSuccess = {
                _resultMessage.value = "Reset link sent to $email"
                _resultLiveData.value = AuthResult.Success
                _isLoading.value = false
            },
            sendError = {
                Log.e(TAG(), it?.errorMessage.toString())
                _resultMessage.value = it?.errorMessage ?: "Password reset failed"
                _resultLiveData.value = AuthResult.Error
                _isLoading.value = false
            }
        )
        _isLoading.value = true
    }

    fun clearResult() {
        _resultLiveData.value = AuthResult.Handled
    }
}