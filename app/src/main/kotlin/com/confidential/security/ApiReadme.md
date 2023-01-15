

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


# Encryption
Encryption is done via the `Encryptor` class. The functions are based on `java.security.***` libraries. The Encryption algorithm used is [RSA](https://en.wikipedia.org/wiki/RSA_(cryptosystem)) with customizable key size (RSA-512, RSA-1024, RSA-2048). All of the functions in this class are static, i.e. there is no need to instantiate an object from this class. Below are the details of the available functions in this class:

## static KeyPair generateKeyPair(int keySize)
This function generates a keypair for the RSA algorithm.

input: int Keysize  
: This is the key size of the RSA algorithm. Allowed values for this input are **512**, **1024**, and **2048**. Larger key size yield better encryption security, but also introduces longer Public Key and Encrypted Messages.

output: KeyPair keyPair  
: The output is in the form of `java.security.KeyPair`. User can get the public or private key from the KeyPair object as shown in the example below:
```java  
KeyPair kp = generateKeyPair(2048);  
PublicKey pk = kp.getPublic();  
PrivateKey pvk = kp.getPrivate();  
``` 
## static String encrypt(String plainText, PublicKey publicKey)

This function encrypts a given string given a public key.
```java
String plainMessage = "A secret message!";
String encryptedMessage = Encryptor.encrypt(plainMessage, pk);
```

input: plainText  
: The plain (raw) text to encrypt.

input: publicKey  
: The public key to encrypt the raw text string. The public key should be a `java.security.PublicKey` type.

output: String cipheredText  
: Encrypted text in the form of String, encoded in Base64 format. (`java.util.Base64`)


## static String decrypt(String cipherText, PrivateKey privateKey)
This function decrypts a given string given a private key and returns the raw text.

input: cipherText  
: The encrypted (ciphered) text to dencrypt.

input: privateKey  
: The private key to encrypt the raw text string. The private key should be a `java.security.PrivateKey` type, and must match the public key that was used to encrypt the cipher text.

output: String plainText  
: Decrypted text in the form of String.


# Encoding & Decoding

In this section we explain the functions that can be used to encode a public key or an encrypted message into one or multiple packets. These packets then can be sent over SMS. This is done via the `ConfidentialCore` class.

## Packet Structure

Each packet is constructed as follows:  
|**Field**|**Size (Bytes)**|**Notes**|  
|---|---|---|  
|Version|1|Packet version is included in each packet to ensure backward compatibility|  
|Type|1|Indicates the type of the content the packet is carrying. See PacketType Enum for more information|
|Tag|1|In case a message is broken into multiple packets, they all have the same tag to make decoding them easier|
|Packet Index|1|A zero-based value that indicates the index of a packet in a series (in case of multi-packet message).|
|Packet Length|1|Indicates how many packets a message is divided into|
|Body|154|Content of a packet|
|Checksum|1|Sum of all the values in the packet, modulus 256. We use this to detect if a packet is encoded by this program or not|

This data structure is stored in the `SmsPacket` class. We skip the description of most of the functions in class, since the user does not need to access it to use the API.

### Unpack a packet
It is possible to unpack a packet and read its different fields (as explained in the table above) by doing the following:
```java
String packetString = "---A received packet---";
SmsPacket unpackedPacket = new SmsPacket (packetString);
if (unpackedPacket.isValid())
{
	char version = unpackedPacket.getVersion();
	char type = unpackedPacket.getType();  
	PacketType type_enum = PacketType.values()[type]; // Optional
	char tag = unpackedPacket.getTag();
	char packetIndex = unpackedPacket.getPacketIndex();
	char packetLength = unpackedPacket.getPacketLength();
}
else
{
	// The received packet does not have the correct structure, or is corrupted. 
}
```

## Encoding
In the context of this API, Encoding is defined as turning a public key or an encrypted message into one or multiple Strings that fit into an SMS. Encoding is done via the following static functions of `ConfidentialCore` class:

- `public static String[] encodePublicKey(KeyPair kp)`
- `public static String[] encodePublicKey(PublicKey pk)`
- `public static String[] encodeMessage(String in)`

### Encoding a Public Key
```java
KeyPair kp = generateKeyPair(2048);  
PublicKey pk = kp.getPublic();  

String[] encodedPublicKey = ConfidentialCore.encodePublicKey(kp);
// or
String[] encodedPublicKey = ConfidentialCore.encodePublicKey(pk);
```
### Encoding a message
```java
String plainMessage = "A secret message!";
String encryptedMessage = Encryptor.encrypt(plainMessage, pk);
String[] encodedMessage = encodeMessage(message);
```
*Note: It is possible to encode a plain, unencrypted, message. However, it is not advised.*

## Decoding
The `ConfidentialCore` class uses a buffer to gather multiple messages until enough messages are gathered and ready to be decoded. To decode a public key or an encrypted message, follow these steps:
### Step #1: Instantiate an object from ConfidentialCore class
```java
ConfidentialCore cc = new ConfidentialCore();
```
### Step #2: Add packets in string format to the buffer

```java
boolean isReady = cc.addPacketString(msg);
```
Keep adding messages until this function returns true. At this time, there are enough packets in the buffer to decode.
*Note: Never add more messages to the buffer if `addPacketString` returns true*
### Step #3: Get the decoded Public Key or Message
```java
if (isReady)
{
	PacketType pt = cc.getDecodedPacketType();
	if (pt == PacketType.MESSAGE)
		String decodedMessage = cc.getDecodedMessage();
	if (pt == PacketType.PUBLIC_KEY)
		PublicKey decodedPublicKey = cc.getDecodedPublicKey();
}
else
{
	// Not enough packets to decode anything
}

```
### Step #4: Clear the buffer or instantiate a new CC object before adding more packets
```java
if (isReady)
{
	// ... get decoded stuff...
	
	cc.clearPacketBuffer();
	// or
	cc = new ConfidentialCore();
}
```