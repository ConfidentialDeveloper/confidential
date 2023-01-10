package com.confidential.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class ConfidentialCore {

//    private static final int PACKET_VERSION = 0;
//    private static final int MAX_SMS_LENGTH = 160;
//    private static final int MAX_PACKETS_IN_BUFFER = 100;
//
//    private static int currentOutboundTag = 0;
//
//    private SmsPacket[] packetBuffer;
//    private int numPacketsInBuffer;
//    private boolean readyToDecode;
//    private PacketType packetType;
//
//    private PublicKey decodedPublicKey;
//    private String decodedMessage;
//
//    public PublicKey getDecodedPublicKey() {
//        return decodedPublicKey;
//    }
//
//    public String getDecodedMessage() {
//        return decodedMessage;
//    }
//
//    public ConfidentialCore() {
//        clearPacketBuffer();
//    }
//
//    // Send Encrypted message to this function to decode to one/multiple strings to send over sms
//    public static String[] encodeMessage(String in) {
//        SmsPacket[] message_smsPackets = ConfidentialCore.getSmsPacketFromString(in, PacketType.MESSAGE);
//
//        String[] message_smsStrings = new String[message_smsPackets.length];
//
//        for (int i = 0; i < message_smsPackets.length; i++) {
//            message_smsStrings[i] = message_smsPackets[i].getString();
//        }
//        return message_smsStrings;
//
//    }
//
//    public static String[] encodePublicKey(KeyPair kp) {
//        return ConfidentialCore.encodePublicKey(kp.getPublic());
//    }
//
//    public static String[] encodePublicKey(PublicKey pk) {
//        byte[] pk_encoded = pk.getEncoded();
//        char[] pk_char = new char[pk_encoded.length];
//
//        for (int a = 0; a < pk_encoded.length; a++) {
//            pk_char[a] = (char) (127 + pk_encoded[a]);
//        }
//
//        String pk_string = new String(pk_char);
//        SmsPacket[] pk_smsPackets = ConfidentialCore.getSmsPacketFromString(pk_string, PacketType.PUBLIC_KEY);
//
//        String[] pk_smsStrings = new String[pk_smsPackets.length];
//
//        for (int i = 0; i < pk_smsPackets.length; i++) {
//            pk_smsStrings[i] = pk_smsPackets[i].getString();
//        }
//
//        return pk_smsStrings;
//
//    }
//
//    public static SmsPacket decodePacketFromSmsString(String encodedPacket) {
//
//        SmsPacket toReturn = new SmsPacket(encodedPacket);
//
//        if (toReturn.isValid()) {
//            return toReturn;
//        } else {
//            throw new RuntimeException("The input packet is not valid");
//        }
//
//
//    }
//
//    private static SmsPacket[] getSmsPacketFromString(String in, PacketType pt) {
//        int maxBodyLength = SmsPacket.getMaxBodyLength(MAX_SMS_LENGTH);
//        char[] in_chars = in.toCharArray();
//        int numSmsPackets = (in_chars.length / maxBodyLength) + 1;
//
//
//        SmsPacket[] toReturn = new SmsPacket[numSmsPackets];
//
//        for (int idx = 0; idx < numSmsPackets; idx++) {
//            int packetBegin = idx * maxBodyLength;
//            int packetEnd = Math.min((idx + 1) * maxBodyLength, in_chars.length);
//
//            String inputSubString = in.substring(packetBegin, packetEnd);
//
//            toReturn[idx] = new SmsPacket(
//                    PACKET_VERSION,
//                    pt,
//                    ConfidentialCore.currentOutboundTag,
//                    idx,
//                    numSmsPackets,
//                    inputSubString,
//                    MAX_SMS_LENGTH
//            );
//
//        }
//        ConfidentialCore.currentOutboundTag++;
//        return toReturn;
//
//    }
//
//    public void clearPacketBuffer() {
//        this.numPacketsInBuffer = 0;
//        this.readyToDecode = false;
//        this.decodedPublicKey = null;
//        this.decodedMessage = null;
//        this.packetType = null;
//    }
//
//
//
//    public boolean addPacketString(String newPacketString)  throws NoSuchAlgorithmException, InvalidKeySpecException{
//        SmsPacket newPacket = decodePacketFromSmsString(newPacketString);
//        return this.addPacketToBuffer(newPacket);
//    }
//
//    // Only accepts packets with the same tag as the first packet. Flush buffer using clearPacketBuffer() for new tags.
//    // Returns true if there is a packet is ready and decoded.
//    private boolean addPacketToBuffer(SmsPacket newPacket) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        if (this.numPacketsInBuffer >= MAX_PACKETS_IN_BUFFER) {
//            return false;
//        } else {
//            if (this.numPacketsInBuffer != 0) {
//                if (this.packetBuffer[0].getTag() != newPacket.getTag()) {
//                    return false;
//                }
//            } else {
//                this.packetBuffer = new SmsPacket[newPacket.getPkt_length()];
//            }
//            this.packetBuffer[newPacket.getPkt_idx()] = new SmsPacket(newPacket);
//
//            this.numPacketsInBuffer++;
//            if (this.numPacketsInBuffer == newPacket.getPkt_length()) {
//                this.readyToDecode = true;
//                this.decode();
//                return true;
//            }
//            return false;
//        }
//    }
//
//    private void decode() throws NoSuchAlgorithmException, InvalidKeySpecException {
//        if (!readyToDecode) {
//            throw new RuntimeException("The message is not ready to decode...");
//        } else {
//
//            String decodedPacketString = "";
//
//            for (int i = 0; i < this.numPacketsInBuffer; i++) {
//                decodedPacketString += new String(this.packetBuffer[i].getBody());
//            }
//
//            char[] decodedPacketCharArr = decodedPacketString.toCharArray();
//
//
//            this.packetType = PacketType.values[this.packetBuffer[0].getType()];
//
//            if (packetType == PacketType.PUBLIC_KEY) {
//                byte[] pk_byte = new byte[decodedPacketCharArr.length];
//
//                for (int i = 0; i < decodedPacketCharArr.length; i++) {
//                    pk_byte[i] = (byte) (decodedPacketCharArr[i] - 127);
//                }
//
//
//                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pk_byte);
//                this.decodedPublicKey = keyFactory.generatePublic(publicKeySpec);
//
//                return;
//            }
//            if (packetType == PacketType.MESSAGE) {
//                this.decodedMessage = decodedPacketString;
//            }
//
//        }
//    }


}
