package com.example.visioglobe.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.network.DataState
import com.example.visioglobe.repository.IncidentRepository
import com.example.visioglobe.util.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@HiltViewModel
class IncidentDetailViewModel @Inject constructor(
    private val incidentRepository: IncidentRepository,
    val dateFormatter: DateFormatter
) :
    ViewModel() {


    private val _selectedIncident = MutableLiveData<Incident>()
    val selectedIncident: LiveData<Incident>
        get() = _selectedIncident

    @VisibleForTesting
    val errorState: MutableState<Boolean> = mutableStateOf(false)

    @VisibleForTesting
    val loadingState: MutableState<Boolean> = mutableStateOf(false)

    val imagePath: Flow<String> = flow {
        selectedIncident.value?.imageId?.let {
            incidentRepository.loadImage(it)
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Success -> {
                            emit(dataState.data)
                            resetState()
                        }
                        is DataState.Error -> errorState.value = true
                        is DataState.Loading -> loadingState.value = true
                    }
                }
        }
    }.flowOn(Dispatchers.IO)

    fun setSelectedIncident(incident: Incident) {
        _selectedIncident.value = incident
    }

    @VisibleForTesting
    fun resetState() {
        errorState.value = false
        loadingState.value = false
    }

}
