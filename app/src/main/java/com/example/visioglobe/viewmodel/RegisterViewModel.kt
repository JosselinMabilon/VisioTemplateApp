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
import com.example.visioglobe.constants.*
import com.example.visioglobe.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val context: BaseApplication
) : ViewModel() {

    @VisibleForTesting
    var errorMessage: MutableState<String> =
        mutableStateOf(context.getString(R.string.register_incorrect_info))

    @VisibleForTesting
    var errorState: MutableState<Boolean> = mutableStateOf(false)

    private val _eventSignUpSuccess: MutableLiveData<Int> =
        MutableLiveData(ReturnFunction.DEFAULT.value)
    val eventSignUpSuccess: LiveData<Int> get() = _eventSignUpSuccess

    var stringBuilder = StringBuilder(PASSWORD_LENGTH)

    fun signUp(
        firstName: String = "",
        lastName: String = "",
        email: String = "",
        phone: String = "",
        password: String = "",
        confirmPassword: String = "",
        activity: Activity
    ) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorState.value = true
            errorMessage.value = context.getString(R.string.register_empty_field)
            _eventSignUpSuccess.value = ReturnFunction.FAILED.value
        } else if (!email.matches(EMAIL_PATTERN.toRegex())) {
            errorState.value = true
            errorMessage.value = context.getString(R.string.login_wrong_email_pattern)
            _eventSignUpSuccess.value = ReturnFunction.FAILED.value
        } else if (phone.length != PHONE_NUMBER_LENGTH) {
            errorState.value = true
            errorMessage.value = context.getString(R.string.register_check_phone_length)
            _eventSignUpSuccess.value = ReturnFunction.FAILED.value
        } else if (!password.matches(PASSWORD_PATTERN.toRegex())) {
            errorState.value = true
            errorMessage.value = context.getString(R.string.register_wrong_password_pattern)
            _eventSignUpSuccess.value = ReturnFunction.FAILED.value
        } else if (confirmPassword != password) {
            errorState.value = true
            errorMessage.value = context.getString(R.string.register_wrong_confirm_password)
            _eventSignUpSuccess.value = ReturnFunction.FAILED.value
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                if (registerRepository.signUp(
                        email = email,
                        password = password,
                        activity = activity
                    )
                ) {
                    registerRepository.writeNewUser(firstName, lastName, email, phone)
                    errorState.value = false
                    _eventSignUpSuccess.postValue(ReturnFunction.PASSED.value)
                } else {
                    errorState.value = true
                    errorMessage.value =
                        context.getString(R.string.register_incorrect_info)
                    _eventSignUpSuccess.postValue(ReturnFunction.FAILED.value)
                }
            }
        }
    }

    fun eventSignUpReset() {
        _eventSignUpSuccess.value = ReturnFunction.DEFAULT.value
    }

    fun generatePassword(): String {
        var i = 0

        stringBuilder.clear()
        while (i < PASSWORD_LENGTH) {
            val random = (PASSWORD_CHARACTERS.indices).random()
            stringBuilder.append(PASSWORD_CHARACTERS[random])
            i++
            if (i >= PASSWORD_LENGTH && !stringBuilder.toString()
                    .matches(PASSWORD_PATTERN.toRegex())
            ) {
                i = 0
                stringBuilder = StringBuilder(PASSWORD_LENGTH)
            }
        }
        return stringBuilder.toString()
    }
}