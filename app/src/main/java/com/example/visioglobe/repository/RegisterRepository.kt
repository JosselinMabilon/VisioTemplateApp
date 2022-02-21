package com.example.visioglobe.repository

import android.app.Activity

interface RegisterRepository {

    suspend fun signUp(email: String, password: String, activity: Activity): Boolean

    fun writeNewUser(firstName: String, lastName: String, email: String, phone: String, permission: String = "User")

}