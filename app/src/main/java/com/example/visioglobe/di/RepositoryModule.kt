package com.example.visioglobe.di

import com.example.visioglobe.network.mapper.IncidentNetworkMapper
import com.example.visioglobe.network.mapper.UserNetworkMapper
import com.example.visioglobe.repository.*
import com.example.visioglobe.repository.impl.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRepository(
        auth: FirebaseAuth
    ): LoginRepository {
        return LoginRepositoryImpl(auth)
    }

    @Singleton
    @Provides
    fun provideRegisterRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): RegisterRepository {
        return RegisterRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(): DataStoreRepository {
        return DataStoreRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideAccountRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        mapper: UserNetworkMapper
    ): AccountRepository {
        return AccountRepositoryImpl(auth, firestore, mapper)
    }

    @Singleton
    @Provides
    fun provideIncidentRepository(
        firestore: FirebaseFirestore,
        firecloud: FirebaseStorage,
        mapper: IncidentNetworkMapper
    ): IncidentRepository {
        return IncidentRepositoryImpl(firestore, firecloud, mapper)
    }

    @Provides
    @Singleton
    fun provideAdminRepository(
        firestore: FirebaseFirestore,
        mapper: UserNetworkMapper
    ): AdminRepository {
        return AdminRepositoryImpl(firestore, mapper)
    }
}