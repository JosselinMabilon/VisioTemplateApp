package com.example.visioglobe.manager;

import com.example.visioglobe.domain.model.User;
import com.example.visioglobe.network.DataState;

import kotlinx.coroutines.flow.Flow;

interface AdminManager {

    suspend fun getUsersOrderByPermission(): Flow<DataState<List<User>>>

}

