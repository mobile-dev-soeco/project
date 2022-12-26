package com.example.soeco.ui.auth.resend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Repository

class ResendConfirmationViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultMessageLiveData = MutableLiveData<String>()
    val resultMessageLiveData: LiveData<String>
        get() = _resultMessageLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun sendEmail(email: String) {
        repository.resendConfirmationEmail(
            email,
            sendSuccess = {
                Log.v(TAG(), "confirmation email sent")
                _resultMessageLiveData.value = "User confirmation email sent to $email"
                _isLoading.value = false
            },
            sendError = {
                Log.e(TAG(), it.message.toString())
                _resultMessageLiveData.value = "An error occurred when sending the email"
                _isLoading.value = false
            }
        )
        _isLoading.value = true
    }
}