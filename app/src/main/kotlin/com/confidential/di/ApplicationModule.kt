package com.confidential.di

import android.content.Context
import com.confidential.data.ConfidentialPreference
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
}