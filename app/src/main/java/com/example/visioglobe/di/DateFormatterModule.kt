package com.example.visioglobe.di

import com.example.visioglobe.util.DateFormatter
import com.example.visioglobe.util.impl.DateFormatterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DateFormatterModule {

    @Singleton
    @Provides
    fun provideDateFormatter(): DateFormatter {
        return DateFormatterImpl()
    }

}