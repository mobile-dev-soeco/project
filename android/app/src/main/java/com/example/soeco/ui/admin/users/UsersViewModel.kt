package com.example.soeco.ui.admin.users

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.soeco.TAG
import com.example.soeco.data.Models.CustomData
import com.example.soeco.data.Repository
import io.realm.DynamicRealm.Transaction.OnSuccess

class UsersViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _usersLiveData = MutableLiveData<List<CustomData>>(emptyList())
    val usersLiveData: LiveData<List<CustomData>>
        get() = _usersLiveData

    fun updateUser(
        email: String,
        firstname: String,
        lastname: String,
        role: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository.updateUser(email, firstname, lastname, role, onSuccess, onError)
    }

    fun deleteUser(user: CustomData, onSuccess: (email: String) -> Unit, onError: (Exception) -> Unit) {
        repository.deleteUser(user, onSuccess, onError)
    }

    fun getUsers() {
        repository.getUsers(
            onSuccess = {
                _usersLiveData.value = it
                Log.v(TAG(), it.toString())
            },
            onError = {
                Log.e("getUsers", it.message.toString())
            }
        )
    }
}