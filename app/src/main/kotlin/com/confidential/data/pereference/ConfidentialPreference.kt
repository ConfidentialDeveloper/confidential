package com.confidential.data.pereference

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfidentialPreference @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun retrievePublicKeyString(): String {
        return prefs.getString(PUBLIC_KEY_PREF_TAG, "")!!
    }

    fun storePublicKeyString(query: String) {
        prefs.edit().putString(PUBLIC_KEY_PREF_TAG, query).apply()
    }

    fun retrieveOtherPartyPublicKeyString(): String {
        return prefs.getString(PUBLIC_KEY_OTHER_PARTY_PREF_TAG, "")!!
    }

    fun storeOtherPartyPublicKeyString(query: String) {
        prefs.edit().putString(PUBLIC_KEY_OTHER_PARTY_PREF_TAG, query).apply()
    }

    fun removeOtherPartyPublicKeyString() {
        prefs.edit().remove(PUBLIC_KEY_OTHER_PARTY_PREF_TAG).apply()
    }

    fun removePublicKeyString() {
        prefs.edit().remove(PUBLIC_KEY_PREF_TAG).apply()
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "ConfidentialPreference"
        private const val PUBLIC_KEY_PREF_TAG = "publicKey"
        private const val PUBLIC_KEY_OTHER_PARTY_PREF_TAG = "otherPartyPublicKey"
    }
}