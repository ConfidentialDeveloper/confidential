package com.confidential.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @ApplicationContext
    internal lateinit var application : Application

    @Provides
    fun getContext() : Context {
        return application.applicationContext
    }

}