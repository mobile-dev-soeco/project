package com.example.soeco.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.data.Repository

class LoginViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _loginLiveData = MutableLiveData<LoginResult>()
    val loginLiveData: LiveData<LoginResult>
        get() = _loginLiveData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun login(email: String, password: String) {
        repository.login(
            email,
            password,
            loginSuccess = {
                Log.v("Authentication", "Logged in as $it")
                _loginLiveData.value = LoginResult.LoginSuccess
                _isLoading.value = false
            },
            loginError = {
                Log.e("Authentication", "Login failed: ${it?.errorMessage}")
                _loginLiveData.value = LoginResult.LoginError("Login Failed")
                _isLoading.value = false
            }
        )
        _isLoading.value = true
    }

    sealed class LoginResult {
        object LoginSuccess: LoginResult()
        class LoginError(val errorMsg: String?): LoginResult()
    }


}