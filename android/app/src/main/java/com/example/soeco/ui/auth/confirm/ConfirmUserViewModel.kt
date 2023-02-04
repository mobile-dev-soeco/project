package com.example.soeco.ui.auth.confirm

import androidx.lifecycle.*
import com.example.soeco.data.Repository
import com.example.soeco.utils.ActionResult

class ConfirmUserViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _confirmUserResult = MutableLiveData<ActionResult>()
    val confirmUserResult: LiveData<ActionResult>
        get() = _confirmUserResult

    private val _resultMessage = MutableLiveData<String>()
    val resultMessage: LiveData<String>
        get() = _resultMessage

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun isInvalidToken() {
        _resultMessage.value = "Token invalid or expired!"
        _confirmUserResult.value = ActionResult.Error
    }

    fun confirmUser(token: String, tokenId: String) {
        repository.confirmUser(
            token,
            tokenId,
            confirmSuccess = {
                _resultMessage.value = "User account confirmed!"
                _confirmUserResult.value = ActionResult.Success
                _isLoading.value = false
            },
            confirmError = {
                _resultMessage.value = "Confirmation failed!"
                _confirmUserResult.value = ActionResult.Error
                _isLoading.value = false
            }
        )
        _isLoading.value = true
    }

    fun clearResult() {
        _confirmUserResult.value = ActionResult.Handled
    }

}