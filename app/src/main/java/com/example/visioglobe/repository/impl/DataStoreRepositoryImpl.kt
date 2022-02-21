package com.example.visioglobe.repository.impl

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.visioglobe.constants.*
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepositoryImpl : DataStoreRepository {

    private val Context.userDataStore by preferencesDataStore(name = "userInfo")

    override suspend fun putBooleanValue(key: String, value: Boolean, context: Context) {
        context.userDataStore.edit { pref ->
            pref[booleanPreferencesKey(key)] = value
        }
    }

    override suspend fun getBooleanValue(
        key: String,
        defaultValue: Boolean,
        context: Context
    ): Boolean {
        val valueFlow: Flow<Boolean> = context.userDataStore.data.map { info ->
            info[booleanPreferencesKey(key)] ?: defaultValue
        }
        return valueFlow.first()
    }

    override suspend fun putStringValue(key: String, value: String, context: Context) {
        context.userDataStore.edit { pref ->
            pref[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun getStringValue(
        key: String,
        defaultValue: String,
        context: Context
    ): String {
        val valueFlow: Flow<String> = context.userDataStore.data.map { info ->
            info[stringPreferencesKey(key)] ?: defaultValue
        }
        return valueFlow.first()
    }

    override fun getStringValueFlow(
        key: String,
        defaultValue: String,
        context: Context
    ) = context.userDataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw  exception
        }
    }.map { info ->
        info[stringPreferencesKey(key)] ?: defaultValue
    }

    override suspend fun saveUserInfo(user: User, context: Context) {
        context.userDataStore.edit { pref ->
            pref[stringPreferencesKey(idAttribute)] = user.id
            pref[stringPreferencesKey(emailAttribute)] = user.email
            pref[stringPreferencesKey(firstNameAttribute)] = user.firstName
            pref[stringPreferencesKey(lastNameAttribute)] = user.lastName
            pref[stringPreferencesKey(phoneAttribute)] = user.phone
            pref[stringPreferencesKey(permissionAttribute)] = user.permission
            pref[stringPreferencesKey(siteAttribute)] = user.site
        }
    }

    override suspend fun getUserInfo(defaultValue: String, context: Context): User {
        return User(
            getStringValue(idAttribute, "", context),
            getStringValue(emailAttribute, "", context),
            getStringValue(firstNameAttribute, "", context),
            getStringValue(lastNameAttribute, "", context),
            getStringValue(permissionAttribute, "", context),
            getStringValue(phoneAttribute, "", context),
            getStringValue(siteAttribute, "", context)
        )
    }

    override suspend fun clearUserInfo(context: Context) {
        context.userDataStore.edit { pref ->
            pref.clear()
        }
    }
}