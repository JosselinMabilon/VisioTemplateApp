package com.example.visioglobe.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.visioglobe.BaseApplication
import com.example.visioglobe.constants.permissionAttribute
import com.example.visioglobe.repository.DataStoreRepository
import com.example.visioglobe.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val context: BaseApplication,
) : ViewModel() {

    var isConnected: MutableState<Boolean> = mutableStateOf(false)

    private var _isAdmin: LiveData<Boolean> =
        dataStoreRepository.getStringValueFlow(key = permissionAttribute, context = context)
            .map { permission ->
                permission == ADMIN
            }.asLiveData(Dispatchers.Main)

    val isAdmin: LiveData<Boolean>
        get() = _isAdmin


    fun userIsConnected() {
        viewModelScope.launch {
            isConnected.value = loginRepository.userIsConnected()
        }
    }

    companion object {
        private const val ADMIN = "admin"
    }
}