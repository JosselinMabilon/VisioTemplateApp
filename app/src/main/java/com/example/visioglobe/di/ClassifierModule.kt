package com.example.visioglobe.di

import com.example.visioglobe.ml.FurnitureClassifier
import com.example.visioglobe.ml.impl.FurnitureClassifierImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ClassifierModule {

    @Singleton
    @Provides
    fun provideFurnitureClassifier(): FurnitureClassifier {
        return FurnitureClassifierImpl()
    }
}