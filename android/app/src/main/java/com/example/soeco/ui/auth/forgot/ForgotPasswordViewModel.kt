package com.example.soeco.ui.auth.forgot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Repository

class ForgotPasswordViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultTextLiveData = MutableLiveData<String>()
    val resultTextLiveData: LiveData<String>
        get() = _resultTextLiveData

    private val _shouldNavigate = MutableLiveData<Boolean>(false)
    val shouldNavigate: LiveData<Boolean>
        get() = _shouldNavigate

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun sendPasswordResetEmail(email: String) {
        repository.sendPasswordResetEmail(
            email,
            sendSuccess = {
                _resultTextLiveData.value = "Reset link sent to $email"
                _isLoading.value = false
                _shouldNavigate.value = true
            },
            sendError = {
                Log.e(TAG(), it?.errorMessage.toString())
                _resultTextLiveData.value = "Password reset failed"
                _isLoading.value = false
                _shouldNavigate.value = true
            }
        )
        _isLoading.value = true
    }
}