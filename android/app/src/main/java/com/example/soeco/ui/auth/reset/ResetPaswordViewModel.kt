package com.example.soeco.ui.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Repository
import com.example.soeco.utils.ActionResult

class ResetPaswordViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _resetResult = MutableLiveData<ActionResult>()
    val resetResult: LiveData<ActionResult>
        get() = _resetResult

    private val _resultMessage = MutableLiveData<String>()
    val resultMessage: LiveData<String>
        get() = _resultMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun resetPassword(token: String, tokenId: String, newPassword: String) {
        repository.resetPassword(
            token,
            tokenId,
            newPassword,
            resetSuccess = {
                _resultMessage.value = "Password reset successfully"
                _resetResult.value = ActionResult.Success
                _isLoading.value = false
            },
            resetError = {
                _resultMessage.value = "Password reset failed. Try Again"
                _resetResult.value = ActionResult.Error
                _isLoading.value = false
            }
        )
        _isLoading.value = true
    }

    fun clearResult() {
        _resetResult.value = ActionResult.Handled
    }

}