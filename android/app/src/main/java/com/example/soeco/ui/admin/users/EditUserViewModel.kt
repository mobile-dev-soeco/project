package com.example.soeco.ui.admin.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Repository

class EditUserViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _updateResult = MutableLiveData<Result>()
    val updateResult: LiveData<Result>
        get() = _updateResult

    fun updateUser(
        email: String,
        firstname: String,
        lastname: String,
        role: String
    ) {
        repository.updateUser(
            email,
            firstname,
            lastname,
            role,
            onSuccess = {
                _updateResult.value = Result.Success
                _loading.value = false
            },
            onError = {
                _updateResult.value = Result.Error
                _loading.value = false
            }
        )
        _loading.value = true
    }

    sealed class Result {
        object Success: Result()
        object Error: Result()
    }
}