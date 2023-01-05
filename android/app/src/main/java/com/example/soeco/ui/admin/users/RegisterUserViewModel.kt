package com.example.soeco.ui.admin.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Repository

class RegisterUserViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _registerLiveData = MutableLiveData<RegisterResult>()
    val registerLiveData: LiveData<RegisterResult>
        get() = _registerLiveData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun registerUser(firstname: String, lastName: String, email: String, password: String, userType: String) {
        repository.registerUser(
            firstname,
            lastName,
            email,
            password,
            userType,
            registerSuccess = {
                _registerLiveData.value = RegisterResult.RegisterSuccess
                _isLoading.value = false
            },
            registerError = {
                _registerLiveData.value = RegisterResult.RegisterError(it?.message)
                _isLoading.value = false
            }
        )
        _isLoading.value = true
    }

    sealed class RegisterResult {
        object RegisterSuccess: RegisterResult()
        class RegisterError(val errorMsg: String?): RegisterResult()
    }
}