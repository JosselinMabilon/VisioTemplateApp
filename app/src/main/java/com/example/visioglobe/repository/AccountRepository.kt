package com.example.visioglobe.repository

import com.example.visioglobe.domain.model.User

interface AccountRepository {

    suspend fun logOut()

    suspend fun getUserId(): String

    suspend fun getUserInfos(id: String): User
}