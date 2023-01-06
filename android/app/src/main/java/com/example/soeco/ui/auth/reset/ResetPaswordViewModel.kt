package com.example.soeco.ui.auth.reset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Repository
import com.example.soeco.ui.auth.confirm.ConfirmUserViewModel

class ResetPaswordViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resultTextLiveData = MutableLiveData<String>()
    val resultTextLiveData: LiveData<String>
        get() = _resultTextLiveData

    private val _shouldNavigateTo = MutableLiveData<String>("")
    val shouldNavigateTo: LiveData<String>
        get() = _shouldNavigateTo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun resetPassword(token: String, tokenId: String, newPassword: String) {
        repository.resetPassword(
            token,
            tokenId,
            newPassword,
            resetSuccess = {
                Log.v(TAG(), "Password reset successfully")
                _resultTextLiveData.value = "Password reset Successfully"
                _shouldNavigateTo.value = "login"
                _isLoading.value = false
            },
            resetError = {
                Log.v(TAG(), "Password reset failed: ${it?.message}")
                _resultTextLiveData.value = "Password reset failed"
                _shouldNavigateTo.value = "forgotPassword"
                _isLoading.value = false
            }
        )
        _isLoading.value = true
    }

}