package com.example.visioglobe.manager.impl

import com.example.visioglobe.domain.model.User
import com.example.visioglobe.manager.AdminManager
import com.example.visioglobe.network.DataState
import com.example.visioglobe.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import timber.log.Timber

class AdminManagerImpl(private val adminRepository: AdminRepository) : AdminManager {

    override suspend fun getUsersOrderByPermission(): Flow<DataState<List<User>>> {
        val result = adminRepository.getUsers()
        return result.mapNotNull { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    Timber.tag(TAG)
                        .i("User mapResult : ${dataState.data.sortedBy { it.permission }}")
                    DataState.Success(
                        dataState.data
                            .sortedBy { it.permission }
                    )
                }
                else -> {
                    dataState
                }
            }
        }
    }

    companion object {
        private val TAG = "AdminManager"
    }
}