package com.confidential.di

import android.content.Context
import androidx.room.Room
import com.confidential.data.database.ConfidentialDatabase
import com.confidential.data.pereference.ConfidentialPreference
import com.confidential.security.ConfidentialSecretManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    fun getSecretManager(@ApplicationContext appContext: Context): ConfidentialSecretManager {
        return ConfidentialSecretManager(appContext, ConfidentialPreference(appContext))
    }

    @Provides
    fun getDatabase(@ApplicationContext appContext: Context): ConfidentialDatabase {
        return Room.databaseBuilder(
            appContext, ConfidentialDatabase::class.java, DATABASE_NAME
        ).build()
    }

    companion object {
        private const val DATABASE_NAME = "confidential_database"
    }
}