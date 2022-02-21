package com.example.visioglobe.viewmodel

import android.app.Activity
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visioglobe.BaseApplication
import com.example.visioglobe.R
import com.example.visioglobe.constants.EMAIL_PATTERN
import com.example.visioglobe.constants.ReturnFunction
import com.example.visioglobe.repository.AccountRepository
import com.example.visioglobe.repository.DataStoreRepository
import com.example.visioglobe.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val context: BaseApplication,
    private val dataStore: DataStoreRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    var isConnected: MutableState<Boolean> = mutableStateOf(false)

    @VisibleForTesting
    var errorMessage: MutableState<String> =
        mutableStateOf(context.getString(R.string.login_incorrect_info))

    var errorState: MutableState<Boolean> = mutableStateOf(false)

    private val _eventSignInSuccess: MutableLiveData<Int> =
        MutableLiveData(ReturnFunction.DEFAULT.value)
    val eventSignInSuccess: LiveData<Int> get() = _eventSignInSuccess

    private val _eventResetPasswordSuccess: MutableLiveData<Int> =
        MutableLiveData(ReturnFunction.DEFAULT.value)
    val eventResetPasswordSuccess: LiveData<Int> get() = _eventResetPasswordSuccess

    @VisibleForTesting
    suspend fun getUserId(): String {
        return accountRepository.getUserId()
    }

    fun userIsConnected() {
        viewModelScope.launch {
            isConnected.value = loginRepository.userIsConnected()
        }
    }

    fun signIn(email: String = "", password: String = "", activity: Activity) {
        if (email.isEmpty() || password.isEmpty()) {
            errorState.value = true
            errorMessage.value = context.getString(R.string.login_empty_field)
            _eventSignInSuccess.value = ReturnFunction.FAILED.value
        } else if (!email.matches(EMAIL_PATTERN.toRegex())) {
            errorState.value = true
            errorMessage.value = context.getString(R.string.login_wrong_email_pattern)
            _eventSignInSuccess.value = ReturnFunction.FAILED.value
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                if (loginRepository.signIn(
                        email = email,
                        password = password,
                        activity = activity
                    )
                ) {
                    saveUserInfo()
                    errorState.value = false
                    _eventSignInSuccess.postValue(ReturnFunction.PASSED.value)
                } else {
                    errorState.value = true
                    errorMessage.value = context.getString(R.string.login_incorrect_info)
                    _eventSignInSuccess.postValue(ReturnFunction.FAILED.value)
                }
            }
        }
    }

    fun eventSignInReset() {
        _eventSignInSuccess.value = ReturnFunction.DEFAULT.value
    }

    fun saveUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val userInfo = accountRepository.getUserInfos(getUserId())
            dataStore.saveUserInfo(userInfo, context)
        }
    }

    fun resetPassword(email: String = "") {
        if (email.isEmpty()) {
            errorState.value = true
            errorMessage.value = context.getString(R.string.register_empty_field)
            _eventResetPasswordSuccess.value = ReturnFunction.FAILED.value
        } else if (!email.matches(EMAIL_PATTERN.toRegex())) {
            errorState.value = true
            errorMessage.value = context.getString(R.string.login_wrong_email_pattern)
            _eventResetPasswordSuccess.value = ReturnFunction.FAILED.value
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                if (loginRepository.resetPassword(email = email)) {
                    errorState.value = false
                    _eventResetPasswordSuccess.postValue(ReturnFunction.PASSED.value)
                } else {
                    errorState.value = true
                    errorMessage.value = context.getString(R.string.reset_password_error_msg)
                    _eventResetPasswordSuccess.postValue(ReturnFunction.FAILED.value)
                }
            }
        }
    }

    fun eventResetPasswordReset() {
        _eventResetPasswordSuccess.value = ReturnFunction.DEFAULT.value
    }
}