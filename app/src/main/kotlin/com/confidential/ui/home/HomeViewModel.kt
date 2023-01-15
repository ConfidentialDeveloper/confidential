package com.confidential.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.confidential.security.ConfidentialSecretManager
import com.confidential.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val confidentialSecretManager: ConfidentialSecretManager,
) : BaseViewModel(application) {

    private val _receivedSMS: MutableLiveData<String> = MutableLiveData()
    val receivedSMS: LiveData<String> = _receivedSMS

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun encrypt(plainText: String): String {
        return confidentialSecretManager.encrypt(plainText)
    }

    fun decrypt(cipherText: String): String {
        return confidentialSecretManager.decrypt(cipherText)
    }
}