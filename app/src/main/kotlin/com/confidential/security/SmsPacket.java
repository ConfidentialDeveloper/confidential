package com.confidential.security;


public class SmsPacket {
//
//    private final int CHAR_MAX = 256;
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
//    private int body_length;
//
//    public SmsPacket(int version, PacketType type, int tag, int pkt_idx, int pkt_length, String body, int maxSmsLength) {
//
//        if (version < CHAR_MAX) this.version = (char) version;
//        else throw new IllegalArgumentException();
//        this.type = (char) type.ordinal();
//        if (tag < CHAR_MAX) this.tag = (char) tag;
//        else throw new IllegalArgumentException();
//        if (pkt_idx < CHAR_MAX && pkt_idx < pkt_length) this.pkt_idx = (char) pkt_idx;
//        else throw new IllegalArgumentException();
//        if (pkt_length < CHAR_MAX) this.pkt_length = (char) pkt_length;
//        else throw new IllegalArgumentException();
//
//        int maxBodyLength = SmsPacket.getMaxBodyLength(maxSmsLength);
//
//        char[] body_char = body.toCharArray();
//        this.body_length = body_char.length;
//
//        if (this.body_length <= maxBodyLength) {
//            this.body = body_char;
//        } else
//            throw new IllegalArgumentException();
//
//
//        // Calculate Checksum
//        this.checksum = 0;
//        this.checksum += this.version;
//        this.checksum += this.type;
//        this.checksum += this.tag;
//        this.checksum += this.pkt_idx;
//        this.checksum += this.pkt_length;
//
//        for (int i = 0; i < this.body_length; i++) {
//            this.checksum += this.body[i];
//        }
//
//        this.checksum = (char) Math.floorMod(this.checksum, CHAR_MAX);
//
//        this.isEmpty = false;
//
//
//    }
//
//    public SmsPacket(SmsPacket toClone) {
//        this.version = toClone.version;
//        this.type = toClone.type;
//        this.tag = toClone.tag;
//        this.pkt_idx = toClone.pkt_idx;
//        this.pkt_length = toClone.pkt_length;
//        this.checksum = toClone.checksum;
//        this.isEmpty = toClone.isEmpty;
//        this.body_length = toClone.body_length;
//
//        if (!toClone.isEmpty && toClone.body_length != 0) {
//            this.body = new char[this.body_length];
//            for (int i = 0; i < this.body_length; i++) {
//                this.body[i] = toClone.body[i];
//            }
//        }
//    }
//
//    public SmsPacket(String toDecode) {
//        char[] toDecodeCharArray = toDecode.toCharArray();
//
//        // Check checksum:
//        char checksumValidator = 0;
//
//        for (int i = 0; i < toDecodeCharArray.length - 1; i++) { // Skip the checksum bit itself
//            checksumValidator += toDecodeCharArray[i];
//        }
//        checksumValidator = (char) Math.floorMod(checksumValidator, CHAR_MAX);
//        boolean checksumValid = (checksumValidator == toDecodeCharArray[toDecodeCharArray.length - 1]);
//
//        if (!checksumValid) {
//            // The provided string is not a packet
//            this.isEmpty = true;
//        } else {
//            this.version = toDecodeCharArray[0];
//            this.type = toDecodeCharArray[1];
//            this.tag = toDecodeCharArray[2];
//            this.pkt_idx = toDecodeCharArray[3];
//            this.pkt_length = toDecodeCharArray[4];
//            this.checksum = (char) checksumValidator;
//
//            this.body_length = SmsPacket.getMaxBodyLength(toDecodeCharArray.length);
//            this.body = new char[this.body_length];
//            System.arraycopy(toDecodeCharArray, 5, this.body, 0, this.body_length);
//            this.isEmpty = false;
//        }
//
//    }
//
//
//    public String getString() {
//        if (isEmpty) {
//            return "";
//        } else {
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
//    public static int getMaxBodyLength(int maxSmsLength) {
//        return maxSmsLength
//                - 1 // Version
//                - 1 // Type
//                - 1 // Tag
//                - 1 // Packet index
//                - 1 // Packet Length
//                - 1; // Checksum
//    }
//
//    public boolean isValid() {
//        return !this.isEmpty;
//    }
//
//    public char getVersion() {
//        return version;
//    }
//
//    public char getType() {
//        return type;
//    }
//
//    public char getTag() {
//        return tag;
//    }
//
//    public char getPkt_idx() {
//        return pkt_idx;
//    }
//
//    public char getPkt_length() {
//        return pkt_length;
//    }
//
//    public char[] getBody() {
//        return body;
//    }
//
//    public char getChecksum() {
//        return checksum;
//    }
//
//    public int getBody_length() {
//        return body_length;
//    }

}
