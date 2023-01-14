package com.confidential.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfidentialPreference @Inject constructor(@ApplicationContext context : Context){
    private val prefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun retrieveString(): String {
        return prefs.getString(PUBLIC_KEY_PREF_TAG, "")!!
    }

    fun storeString(query: String) {
        prefs.edit().putString(PUBLIC_KEY_PREF_TAG, query).apply()
    }

    companion object{
        private const val SHARED_PREFERENCE_NAME = "ConfidentialPreference"
        private const val PUBLIC_KEY_PREF_TAG = "publicKey"
    }
}