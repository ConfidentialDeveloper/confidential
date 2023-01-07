package com.confidential.security

import java.nio.charset.StandardCharsets
import java.security.*
import java.util.*
import javax.crypto.Cipher


object ConfidentialSecretManager {
//        @Throws(Exception::class)
//        fun generateKeyPair(keySize: Int): KeyPair {
//            val generator = KeyPairGenerator.getInstance("RSA")
//            generator.initialize(keySize, SecureRandom())
//            return generator.generateKeyPair()
//        }
//
//        @Throws(Exception::class)
//        fun encrypt(plainText: String, publicKey: PublicKey?): String {
//            val encryptCipher = Cipher.getInstance("RSA")
//            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey)
//            val cipherText = encryptCipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
//            return Base64.getEncoder().encodeToString(cipherText)
//        }
//
//        @Throws(Exception::class)
//        fun decrypt(cipherText: String?, privateKey: PrivateKey?): String {
//            val bytes: ByteArray = Base64.getDecoder().decode(cipherText)
//            val decriptCipher = Cipher.getInstance("RSA")
//            decriptCipher.init(Cipher.DECRYPT_MODE, privateKey)
//            return String(decriptCipher.doFinal(bytes), StandardCharsets.UTF_8)
//        }


}