package com.example.visioglobe.repository

import android.content.Context
import com.example.visioglobe.domain.model.User
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun putBooleanValue(key: String, value: Boolean, context: Context)

    suspend fun getBooleanValue(key: String, defaultValue: Boolean, context: Context): Boolean

    suspend fun putStringValue(key: String, value: String, context: Context)

    suspend fun getStringValue(key: String, defaultValue: String, context: Context): String

    fun getStringValueFlow(key: String, defaultValue: String = "", context: Context): Flow<String>

    suspend fun saveUserInfo(user: User, context: Context)

    suspend fun getUserInfo(defaultValue: String, context: Context) : User

    suspend fun clearUserInfo(context: Context)
}