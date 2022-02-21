package com.example.visioglobe.di

import com.example.visioglobe.network.mapper.IncidentNetworkMapper
import com.example.visioglobe.network.mapper.UserNetworkMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideDbFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseCloudStorage() : FirebaseStorage {
        return Firebase.storage
    }

    @Singleton
    @Provides
    fun provideUserMapper() : UserNetworkMapper {
        return UserNetworkMapper()
    }

    @Singleton
    @Provides
    fun provideIncidentMapper() : IncidentNetworkMapper {
        return IncidentNetworkMapper()
    }
}