package com.example.visioglobe.di

import com.example.visioglobe.manager.AdminManager
import com.example.visioglobe.manager.impl.AdminManagerImpl
import com.example.visioglobe.repository.AdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ManagerModule {

    @Singleton
    @Provides
    fun provideAdminManager(
        adminRepository: AdminRepository
    ): AdminManager {
        return AdminManagerImpl(adminRepository)
    }
}
