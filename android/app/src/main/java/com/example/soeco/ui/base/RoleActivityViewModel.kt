package com.example.soeco.ui.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Repository

class RoleActivityViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _isAuthorized = MutableLiveData<Boolean>()
    val isAuthorized: LiveData<Boolean>
    get() = _isAuthorized

    init {
        _isAuthorized.value = repository.isUserLoggedIn()
    }

    fun logout() {
        repository.logout(
            logoutSuccess = {
                Log.v(TAG(), "User logged out")
                _isAuthorized.value = false
            },
            logoutError = {
                Log.e(TAG(), "logout failed")
            }
        )
    }

}
