package com.confidential.security;

import java.security.KeyPair;
import java.security.PublicKey;

public class ConfidentialCore {
//
//    private static final int PACKET_VERSION = 0;
//    private static final int MAX_SMS_LENGTH = 160;
//
//    private static int CurrentTag=0;
//
//    public ConfidentialCore(){
//
//
//    }
//
//
//    public static String[] getEncodedPublicKey(KeyPair kp){
//        return ConfidentialCore.getEncodedPublicKey(kp.getPublic());
//    }
//
//    public static String[] getEncodedPublicKey(PublicKey pk){
//        byte[] pk_encoded = pk.getEncoded();
//        char[] pk_char = new char[pk_encoded.length];
//
//        for(int a=0; a<pk_encoded.length ; a++){
//            pk_char[a] = (char) (127 + pk_encoded[a]);
//        }
//
//        String pk_string = new String(pk_char);
//        SmsPacket[] pk_smsPackets = ConfidentialCore.getSmsPacketFromString(pk_string);
//
//        String[] pk_smsStrings = new String[pk_smsPackets.length];
//
//        for (int i=0; i< pk_smsPackets.length; i++){
//            pk_smsStrings[i] = pk_smsPackets[i].getString();
//        }
//
//        return pk_smsStrings;
//
//    }
//
//
//    public static SmsPacket[] getSmsPacketFromString (String in){
//        int maxBodyLength = SmsPacket.getMaxBodyLength(MAX_SMS_LENGTH);
//        int numSmsPackets = (in.length() / maxBodyLength) + 1;
//
//
//        SmsPacket[] toReturn = new SmsPacket[numSmsPackets];
//
//        for (int idx=0; idx<numSmsPackets ; idx++){
//            int packetBegin =  idx*maxBodyLength;
//            int packetEnd = Math.min((idx+1)*maxBodyLength-1, in.length());
//
//            String inputSubString = in.substring(packetBegin, packetEnd);
//
//            PacketType type = PacketType.PUBLIC_KEY;
//
//            toReturn[idx] = new SmsPacket(
//                    PACKET_VERSION,
//                    type,
//                    ConfidentialCore.CurrentTag,idx,
//                    numSmsPackets,
//                    inputSubString,
//                    MAX_SMS_LENGTH
//            );
//
//        }
//        ConfidentialCore.CurrentTag++;
//        return toReturn;
//
//    }
//
//

}
