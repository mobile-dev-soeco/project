package com.example.soeco.ui.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Repository

class AdminHomeViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _logoutLiveData = MutableLiveData<LogoutResult>()
    val logoutLiveData: LiveData<LogoutResult>
        get() = _logoutLiveData

    private val _registerLiveData = MutableLiveData<RegisterResult>()
    val registerLiveData: LiveData<RegisterResult>
        get() = _registerLiveData

    fun logout() {
        repository.logout(
            logoutSuccess = {
                Log.v(TAG(), "Logout successful")
                _logoutLiveData.value = LogoutResult.LogoutSuccess
            },
            logoutError = {
                Log.e(TAG(), "An error occurred when logging out")
                _logoutLiveData.value = LogoutResult.LogoutError(it?.message)
            }
        )
    }

    fun registerUser(email: String, password: String, userType: String) {
        repository.registerUser(
            email,
            password,
            userType,
            registerSuccess = {
                _registerLiveData.value = RegisterResult.RegisterSuccess
            },
            registerError = {
                _registerLiveData.value = RegisterResult.RegisterError(it?.message)
            }
        )
    }

    sealed class LogoutResult {
        object LogoutSuccess: LogoutResult()
        class LogoutError(val errorMsg: String?): LogoutResult()
    }

    sealed class RegisterResult {
        object RegisterSuccess: RegisterResult()
        class RegisterError(val errorMsg: String?): RegisterResult()
    }

}