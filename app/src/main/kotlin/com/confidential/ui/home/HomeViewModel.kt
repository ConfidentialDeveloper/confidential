package com.confidential.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.confidential.data.pereference.ConfidentialPreference
import com.confidential.security.ConfidentialSecretManager
import com.confidential.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val confidentialSecretManager: ConfidentialSecretManager,
    private val confidentialPreference: ConfidentialPreference
) : BaseViewModel(application) {

    private val _sendPublicKey: MutableLiveData<Boolean> = MutableLiveData()
    val sendPublicKey: LiveData<Boolean> = _sendPublicKey

    private val _sendMessage: MutableLiveData<Boolean> = MutableLiveData()
    val sendMessage: LiveData<Boolean> = _sendMessage

    private val _decryptionUnsuccessful: MutableLiveData<Boolean> = MutableLiveData()
    val decryptionUnsuccessful: LiveData<Boolean> = _decryptionUnsuccessful

    private val _verifyConnection: MutableLiveData<Boolean> = MutableLiveData()
    val verifyConnection: LiveData<Boolean> = _verifyConnection

    private var isPublicKey = false

    val form = Form()

    val publicKey: String = confidentialPreference.retrievePublicKeyString()

    fun encrypt(plainText: String): String {
        return confidentialSecretManager.encrypt(plainText)
    }

    fun decrypt(cipherText: String): String? {
        return confidentialSecretManager.decrypt(cipherText)
    }

    fun decryptionIsUnsuccessful() {
        _decryptionUnsuccessful.postValue(true)
    }

    fun storeOtherPartyPublicKeyString(publicKey: String) {
        confidentialPreference.storeOtherPartyPublicKeyString(publicKey)
    }

    fun sendPublicKey() {
        isPublicKey = true
        if (form.validate()) {
            _sendPublicKey.postValue(true)
        }
    }

    fun sendMessage() {
        isPublicKey = false
        if (form.validate()) {
            _sendMessage.postValue(true)
        }
    }

    fun clearAndResendPublicKey() {
        confidentialPreference.removePublicKeyString()
        confidentialPreference.removeOtherPartyPublicKeyString()
        confidentialPreference.storePublicKeyString(confidentialSecretManager.getPublicKey())
        sendPublicKey()
    }

    fun verifyConnection() {
        _verifyConnection.postValue(true)
    }

    fun refinePublicKeyValue(publicKey: String): String {
        return publicKey.replace(
            HomeFragment.PUBLIC_KEY_SMS_TAG,
            ""
        ).trim()
    }

    inner class Form {
        var phoneNumber: MutableLiveData<String> = MutableLiveData("")
        var message: MutableLiveData<String> = MutableLiveData("")

        val error = FormError()
        fun validate(): Boolean {
            var isValid = true

            if (phoneNumber.value.isNullOrEmpty()) {
                error.phoneNumberError.postValue("Enter phone number.")
                isValid = false
            }
            if (message.value.isNullOrEmpty() && isPublicKey.not()) {
                error.messageError.postValue("Enter a message.")
                isValid = false
            }

            return isValid
        }

    }

    inner class FormError {
        var phoneNumberError: MutableLiveData<String> = MutableLiveData("")
        var messageError: MutableLiveData<String> = MutableLiveData("")

        fun clearPhoneNumberError() {
            phoneNumberError.postValue("")
        }

        fun clearMessageError() {
            messageError.postValue("")
        }
    }

}