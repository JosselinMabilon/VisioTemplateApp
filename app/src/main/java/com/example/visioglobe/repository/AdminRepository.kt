package com.example.visioglobe.repository

import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.network.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AdminRepository {

    suspend fun getUsers(): Flow<DataState<List<User>>>

}