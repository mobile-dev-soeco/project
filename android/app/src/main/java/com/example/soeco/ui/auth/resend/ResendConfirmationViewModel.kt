package com.example.soeco.ui.auth.resend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Repository
import com.example.soeco.utils.AuthResult

class ResendConfirmationViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resendResult = MutableLiveData<AuthResult>()
    val resendResult: LiveData<AuthResult>
        get() = _resendResult

    private val _resultMessage = MutableLiveData<String>()
    val resultMessage: LiveData<String>
        get() = _resultMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun sendEmail(email: String) {
        repository.resendConfirmationEmail(
            email,
            sendSuccess = {
                _resultMessage.value = "Email sent to $email"
                _resendResult.value = AuthResult.Success
                _isLoading.value = false
            },
            sendError = {
                Log.e(TAG(), it.message.toString())
                _resultMessage.value = "Failed to send email"
                _resendResult.value = AuthResult.Error
                _isLoading.value = false
            }
        )
        _isLoading.value = true
    }

    fun clearResult() {
        _resendResult.value = AuthResult.Handled
    }
}