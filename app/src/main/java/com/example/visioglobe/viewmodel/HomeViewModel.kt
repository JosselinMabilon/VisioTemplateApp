package com.example.visioglobe.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visioglobe.repository.LoginRepository
import com.visioglobe.visiomoveessential.enums.VMEViewMode
import com.visioglobe.visiomoveessential.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    var isConnected: MutableState<Boolean> = mutableStateOf(false)

    fun userIsConnected() {
        viewModelScope.launch {
            isConnected.value = loginRepository.userIsConnected()
        }
    }

    fun setUpCamera(poi: String): VMECameraUpdate {
        return VMECameraUpdateBuilder()
            .setTargets(arrayListOf(poi))
            .setHeading(VMECameraHeading.newCurrent())
            .setViewMode(VMEViewMode.FLOOR)
            .setPitch(VMECameraPitch.newPitch((-60).toDouble()))
            .setDistanceRange(
                VMECameraDistanceRange.newRadiusRange(
                    (50).toDouble(),
                    (150).toDouble()
                )
            )
            .build()
    }
}