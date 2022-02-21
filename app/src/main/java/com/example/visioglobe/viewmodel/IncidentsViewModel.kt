package com.example.visioglobe.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
class IncidentsViewModel @Inject constructor(
    private val incidentRepository: IncidentRepository,
    private val dateFormatter: DateFormatter
) :
    ViewModel() {

    var errorState: MutableState<Boolean> = mutableStateOf(false)
    var loadingState: MutableState<Boolean> = mutableStateOf(false)

    private var _incidentsLit: Flow<List<Incident>> = flow {
        incidentRepository.getIncidents().collect { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    emit(dataState.data)
                    resetState()
                }
                is DataState.Error -> errorState.value = true
                is DataState.Loading -> loadingState.value = true
            }
        }
    }.flowOn(Dispatchers.IO)

    val incidentList: Flow<List<Incident>>
        get() = _incidentsLit

    @VisibleForTesting
    fun resetState() {
        errorState.value = false
        loadingState.value = false
    }

    fun mapToDateToToString(date: Long): String {
        return dateFormatter.mapToDateString(date)
    }
}
