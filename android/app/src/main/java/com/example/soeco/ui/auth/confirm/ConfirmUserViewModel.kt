package com.example.soeco.ui.auth.confirm

import android.util.Log
import androidx.lifecycle.*
import com.example.soeco.data.Repository
import java.util.*
import kotlin.concurrent.timerTask

class ConfirmUserViewModel(
    val repository: Repository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val timer = Timer()

    private val _confirmedUserLiveData = MutableLiveData<ConfirmResult>()
    val confirmedUserLiveData: LiveData<ConfirmResult>
        get() = _confirmedUserLiveData

    private val _resultTextLiveData = MutableLiveData<String>()
    val resultTextLiveData: LiveData<String>
        get() = _resultTextLiveData

    private val _shouldNavigateTo = MutableLiveData<String>("")
    val shouldNavigateTo: LiveData<String>
        get() = _shouldNavigateTo

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun isInvalidToken() {
        _resultTextLiveData.value = "Token invalid or expired."
        _shouldNavigateTo.value = "resend"
    }

    fun confirmUser(token: String, tokenId: String) {
        repository.confirmUser(
            token,
            tokenId,
            confirmSuccess = {
                _confirmedUserLiveData.value = ConfirmResult.ConfirmSuccess
                _resultTextLiveData.value = "User account confirmed!"
                _shouldNavigateTo.value = "login"
            },
            confirmError = {
                _confirmedUserLiveData.value = ConfirmResult.ConfirmError(it.message)
                _resultTextLiveData.value = it.message
                Log.e("Confirmation", it.message.toString())
                _shouldNavigateTo.value = "resend"
            }
        )
        _isLoading.value = false
    }

    sealed class ConfirmResult {
        object ConfirmSuccess: ConfirmResult()
        class ConfirmError(val errorMsg: String?): ConfirmResult()
    }

}