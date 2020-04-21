package com.wyl.monitor.util;

import java.nio.ByteBuffer;

public class HexString {


    private final static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};//十六进制的组成元素

    /**
     * 字节数组转16进制String，无分隔，如：FE00120F0E
     *
     * @param array  字节数组
     * @param offset 起始
     * @param length 长度
     * @return
     */
    public static String toHexString(byte[] array, int offset, int length) {
        char[] buf = new char[length * 2];

        int bufIndex = 0;
        for (int i = offset; i < offset + length; i++) {
            byte b = array[i];
            buf[bufIndex++] = HEX_DIGITS[(b >>> 4) & 0x0F];
            buf[bufIndex++] = HEX_DIGITS[b & 0x0F];
        }

        return new String(buf);
    }

    public static String toHexOneString(byte b) {

        return HEX_DIGITS[(b >>> 4) & 0x0F] + "" + HEX_DIGITS[b & 0x0F];
    }

    /**
     * 字节转数组
     *
     * @param b
     * @return
     */
    public static byte[] toByteArray(byte b) {
        byte[] array = new byte[1];
        array[0] = b;
        return array;
    }

    /**
     * int到byte[] 由高位到低位
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < 4; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }


//    public static void main(String[] s){
//        int a = (0x02 & 0xFF)<< 8  | (0x58 & 0xFF);//持续时间
//            System.out.println(a +"");
//    }

    /**
     * int转字节数组
     *
     * @param i
     * @return
     */
    public static byte[] toByteArray(int i) {
        byte[] array = new byte[4];

        array[3] = (byte) (i & 0xFF);
        array[2] = (byte) ((i >> 8) & 0xFF);
        array[1] = (byte) ((i >> 16) & 0xFF);
        array[0] = (byte) ((i >> 24) & 0xFF);

        return array;
    }

    private static ByteBuffer buffer = ByteBuffer.allocate(8);
    //byte 数组与 long 的相互转换
    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    /**
     * int转字节数组
     *
     * @param i
     * @return
     */
    public static byte[] TowByteArray(int i) {
        byte[] array = new byte[2];

        array[1] = (byte) (i & 0xFF);
        array[0] = (byte) ((i >> 8) & 0xFF);

        return array;
    }

    /**
     * int转字节数组
     *
     * @param value
     * @return
     */
    public static byte[] FourByteArray(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 十六进制转int
     *
     * @param c
     * @return
     */
    public static int toByte(char c) {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 10);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 10);

        throw new RuntimeException("Invalid hex char '" + c + "'");
    }

    /**
     * Hex转byte[]，两种情况，Hex长度为奇数最后一个字符会被舍去
     */
    public static byte[] hexTobytes(String hex) {
        if (hex.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hex.length() / 2];
            int j = 0;
            for(int i = 0; i < hex.length(); i+=2) {
                result[j++] = (byte)Integer.parseInt(hex.substring(i,i+2), 16);
            }
            return result;
        }
    }


    public static String hexToSix(int data,int length){
            int hexLength = Integer.toHexString(data).length();
            String hex = Integer.toHexString(data);
                if(hexLength%2 == 1){
                    for(int i = 0; i <= length; i++){
                        if(hex.length() == length){
                            return  hex;
                        }else {
                            hex = "0" + hex;
                        }
                    }
                }else{
                    if(hexLength == length){
                        return  hex;
                    }else{
                        for(int i = 0; i <= length; i++){
                            if(Integer.toHexString(data).length() == length){
                                return  hex;
                            }else {
                                hex = "0" + hex;
                            }
                        }
                    }
                }

          return hex;
    }

    /**
     *
     * @param bytes
     * @return 将二进制转换为十六进制字符输出
     */
    public static String BinaryToHexString(byte[] bytes){
         String hexStr =  "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for(int i=0;i<bytes.length;i++){
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
            result +=hex+" ";
        }
        return result;
    }

    public static byte[] hexStr2Byte(String hex) {
        ByteBuffer bf = ByteBuffer.allocate(hex.length() / 2);
        for (int i = 0; i < hex.length(); i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            bf.put(b);
        }
        return bf.array();
    }


}
