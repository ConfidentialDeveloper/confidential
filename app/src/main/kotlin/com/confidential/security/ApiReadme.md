
# Confidential Core API

This file explains the algorithms, packet structure and functionality of the security API, a.k.a. "ConfidentialCore".

# Purpose

This API provides the following functionalities:  
1- Encryption  
- Generating public and private key pairs  
- Encrypting messages using public key  
- Decrypting messages using private key  
2- Encoding  
- Packing a message into a pre-defined packet  
- Breaking down a long message into multiple packets  
- Merging multiple packets to reconstruct the original message

# Usage

In this section, we explain how this API can be used in the application's code.

## Encryption
Encryption is done via the `Encryptor` class. The functions are based on `java.security.***` libraries. The Encryption algorithm used is [RSA](https://en.wikipedia.org/wiki/RSA_(cryptosystem)) with customizable key size (RSA-512, RSA-1024, RSA-2048). All of the functions in this class are static, i.e. there is no need to instantiate an object from this class. Below are the details of the available functions in this class:

---
### static KeyPair generateKeyPair(int keySize)
This function generates a keypair for the RSA algorithm.

input: int Keysize
: This is the key size of the RSA algorithm. Allowed values for this input are **512**, **1024**, and **2048**. Larger key size yield better encryption security, but also introduces longer Public Key and Encrypted Messages.

output: KeyPair keyPair
: The output is in the form of `java.security.KeyPair`. User can get the public or private key from the KeyPair object as shown in the example below:
```java
KeyPair kp = generateKeyPair(2048);
PublicKey pk = kp.getPublic();
PrivateKey pk = kp.getPrivate();
```
---
### static String encrypt(String plainText, PublicKey publicKey)

This function encrypts a given string given a public key.

input: plainText
: The plain (raw) text to encrypt.

input: publicKey
: The public key to encrypt the raw text string. The public key should be a `java.security.PublicKey` type.

output: String cipheredText
: Encrypted text in the form of String, encoded in Base64 format. (`java.util.Base64`)

---

### static String decrypt(String cipherText, PrivateKey privateKey)
This function decrypts a given string given a private key and returns the raw text.

input: cipherText
: The encrypted (ciphered) text to dencrypt.

input: privateKey
: The private key to encrypt the raw text string. The private key should be a `java.security.PrivateKey` type, and must match the public key that was used to encrypt the cipher text.

output: String plainText
: Decrypted text in the form of String.


## Encoding