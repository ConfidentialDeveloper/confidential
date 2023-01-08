package com.confidential.security;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class SmsPacket {
//
//    private int body_length;
//    private final int CHAR_MAX = 255;
//
//    private char version;
//    private char type;
//    private char tag;
//    private char pkt_idx;
//    private char pkt_length;
//    private char[] body;
//    private char checksum;
//
//    private boolean isEmpty = true;
//
//    public SmsPacket(int version, PacketType type, int tag, int pkt_idx, int pkt_length, String body, int maxSmsLength){
//
//        if (version<CHAR_MAX) this.version = (char)version; else throw new IllegalArgumentException();
//        this.type = (char) type.ordinal();
//        if (tag<CHAR_MAX) this.tag = (char)tag; else throw new IllegalArgumentException();
//        if (pkt_idx<CHAR_MAX && pkt_idx<pkt_length) this.pkt_idx = (char)pkt_idx; else throw new IllegalArgumentException();
//        if (pkt_length<CHAR_MAX) this.pkt_length = (char)pkt_length; else throw new IllegalArgumentException();
//
//        int maxBodyLength = SmsPacket.getMaxBodyLength(maxSmsLength);
//
//        if (body.length() < maxBodyLength){
//            this.body_length = body.length();
//            this.body = body.toCharArray();
//        }
//        else
//            throw new IllegalArgumentException();
//
//
//        // Calculate Checksum
//        this.checksum = 0;
//        this.checksum += this.version;
//        this.checksum += this.type;
//        this.checksum += this.pkt_idx;
//        this.checksum += this.pkt_length;
//        for (int i=0;i<this.body_length;i++){
//            this.checksum += this.body[i];
//        }
//
//        this.checksum = (char) Math.floorMod(this.checksum , CHAR_MAX);
//
//        this.isEmpty = false;
//
//
//    }
//
//    public String getString(){
//        if (isEmpty){
//            return "";
//        }else{
//            StringBuilder sb = new StringBuilder();
//            sb.append(this.version);
//            sb.append(this.type);
//            sb.append(this.tag);
//            sb.append(this.pkt_idx);
//            sb.append(this.pkt_length);
//            sb.append(this.body);
//            sb.append(this.checksum);
//
//            return sb.toString();
//        }
//
//    }
//
//    public static int getMaxBodyLength(int maxSmsLength){
//        return maxSmsLength
//                -1 // Version
//                -1 // Type
//                -1 // Tag
//                -1 // Packet index
//                -1 // Packet Length
//                -1; // Checksum
//    }
//



}
