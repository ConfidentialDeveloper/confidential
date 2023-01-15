package com.confidential.security

import android.content.Context
import android.util.Base64
import com.confidential.data.ConfidentialPreference
import com.yakivmospan.scytale.Crypto
import com.yakivmospan.scytale.KeyProps
import com.yakivmospan.scytale.Store
import java.math.BigInteger
import java.security.KeyPair
import java.util.*
import javax.inject.Inject
import javax.security.auth.x500.X500Principal


class ConfidentialSecretManager @Inject constructor(
    private val context: Context,
    private val confidentialPreference: ConfidentialPreference
) {

    // Create store with specific name and password
    private val store = Store(context, KEY_STORE_NAME, KEY_STORE_PASSWORD.toCharArray())

    private val alias: String = "alias"
    private val password: CharArray = "password".toCharArray()
    private val keySize: Int = 512

    private val start: Calendar = Calendar.getInstance()
    private val end: Calendar = Calendar.getInstance()

    private val encryptionBlockSize: Int =
        keySize / 8 - 11 // as specified for RSA/ECB/PKCS1Padding keys
    private val decryptionBlockSize: Int = keySize / 8 // as specified for RSA/ECB/PKCS1Padding keys

    private val crypto: Crypto =
        Crypto("RSA/ECB/PKCS1Padding", encryptionBlockSize, decryptionBlockSize)

    private var keyPair: KeyPair

    init {
        // The Duration that the key is valid for
        end.add(Calendar.YEAR, 1)

        // Create a key store params, some of them are specific per platform
        // Check KeyProps doc for more info
        val keyProps: KeyProps = KeyProps.Builder()
            .setAlias(alias)
            .setPassword(password)
            .setKeySize(keySize)
            .setKeyType("RSA")
            .setSerialNumber(BigInteger.ONE)
            .setSubject(X500Principal("CN=$alias CA Certificate"))
            .setStartDate(start.time)
            .setEndDate(end.time)
            .setBlockModes("ECB")
            .setEncryptionPaddings("PKCS1Padding")
            .setSignatureAlgorithm("SHA256WithRSAEncryption")
            .build()

        // Generate KeyPair depending on KeyProps
        keyPair = store.generateAsymmetricKey(keyProps)
        println("Saman ${getPublicKey()}")
        confidentialPreference.storeString(getPublicKey())
        // Encrypt/Dencrypt data using buffer with or without Initialisation Vectors
        // This additional level of safety is required on 23 API level for
        // some algorithms. Specify encryption/decryption block size to use buffer for
        // large data when using block based algorithms (such as RSA)
    }

    fun encrypt(plainText: String): String {
        return crypto.encrypt(plainText, keyPair.public, false)
    }

    fun decrypt(cipherText: String): String {
        return crypto.decrypt(cipherText, keyPair.private, false)
    }

    private fun getPublicKey(): String {
        return encodeKey(keyPair.public.encoded)
    }

    private fun encodeKey(keyBytes: ByteArray) : String {
        return Base64.encodeToString(keyBytes, Base64.DEFAULT)
    }

    companion object {
        private const val KEY_STORE_NAME = "confidential"
        private const val KEY_STORE_PASSWORD = "temp"
    }
}