package com.example.visioglobe.di

import com.example.visioglobe.domain.mapper.FurnitureNameMapper
import com.example.visioglobe.domain.mapper.impl.FurnitureNameMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Singleton
    @Provides
    fun provideFurnitureNameMapper(): FurnitureNameMapper {
        return FurnitureNameMapperImpl()
    }
}