package com.example.visioglobe.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visioglobe.BaseApplication
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.repository.AccountRepository
import com.example.visioglobe.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val context: BaseApplication,
    private val dataStore: DataStoreRepository,
) : ViewModel() {

    @VisibleForTesting
    var logOutState: MutableState<Boolean> = mutableStateOf(false)

    @VisibleForTesting
    var userInfo: MutableState<User> = mutableStateOf(
        User(
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
    )

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.logOut()
            dataStore.clearUserInfo(context)
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            userInfo.value = dataStore.getUserInfo(defaultValue = "", context = context)
        }
    }

    fun logOutChangeToTrue() {
        logOutState.value = true
    }

    fun logOutChangeToFalse() {
        logOutState.value = false
    }
}