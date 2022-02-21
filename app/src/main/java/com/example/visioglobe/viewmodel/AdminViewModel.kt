package com.example.visioglobe.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visioglobe.di.qualifier.IoDispatcher
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.manager.AdminManager
import com.example.visioglobe.network.DataState
import com.example.visioglobe.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminManager: AdminManager,
    private val dataStore: DataStoreRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ViewModel() {

    private val _userId = mutableStateOf("")
    var userId: MutableState<String> = _userId

    var errorState: MutableState<Boolean> = mutableStateOf(false)
    var loadingState: MutableState<Boolean> = mutableStateOf(false)

    private var _usersList: Flow<List<User>> = flow {
        adminManager.getUsersOrderByPermission().collect { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    emit(
                        dataState.data.filterNot {
                            it.id == userId.value
                        }
                    )
                }
                is DataState.Error -> errorState.value = true
                is DataState.Loading -> loadingState.value = true
            }
        }
    }.flowOn(ioDispatcher)

    val usersList: Flow<List<User>>
        get() = _usersList

    fun checkUser(context: Context) {
        viewModelScope.launch(ioDispatcher) {
            val userInfoStored = dataStore.getUserInfo(defaultValue = "", context = context)
            _userId.value = userInfoStored.id
        }
    }
}


