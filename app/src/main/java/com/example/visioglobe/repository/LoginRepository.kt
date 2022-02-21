package com.example.visioglobe.repository

import android.app.Activity

interface LoginRepository {

    suspend fun userIsConnected(): Boolean

    suspend fun signIn(email: String, password: String, activity: Activity): Boolean

    suspend fun resetPassword(email: String): Boolean
}