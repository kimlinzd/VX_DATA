package com.lepu.algorithm.ecg.utils;

/**
 * java 默认是大段模式
 */

public class DataTypeChangeHelper {

    /**
     * 将一个单字节的byte转换成32位的int
     *
     * @param b byte
     * @return convert result
     */
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * 将一个单字节的Byte转换成十六进制的数
     *
     * @param b byte
     * @return convert result
     */
    public static String byteToHex(byte b) {
        int i = b & 0xFF;
        return Integer.toHexString(i);
    }

    /**
     * 将一个4byte的数组转换成32位的int
     *
     * @param buf            bytes buffer
     * @return convert result
     */
    public static long unsigned4BytesToInt(byte[] buf, int pos) {
        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = pos;
        firstByte = (0x000000FF & ((int) buf[index]));
        secondByte = (0x000000FF & ((int) buf[index + 1]));
        thirdByte = (0x000000FF & ((int) buf[index + 2]));
        fourthByte = (0x000000FF & ((int) buf[index + 3]));
        index = index + 4;
        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    /**
     * 将16位的short转换成byte数组
     *
     * @param s short
     * @param little boolean  true 小端模式，false 大段模式
     * @return byte[] 长度为2
     */
    public static byte[] shortToByteArray(short s,boolean little) {
        byte[] targets = new byte[2];

        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }

        if(little){
            targets = FormatTransfer.bytesReverseOrder(targets);
        }

        return targets;
    }

    /**
     * 将32位整数转换成长度为4的byte数组
     *
     * @param s int
      *@param little boolean  true 小端模式，false 大段模式
     * @return byte[]
     */
    public static byte[] intToByteArray(int s,boolean little) {
        byte[] targets = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }

        if(little){
            targets = FormatTransfer.bytesReverseOrder(targets);
        }

        return targets;
    }

    /**
     * 将32位整数转换成长度为4的byte数组
     *
     * @param s int
     *@param little boolean  true 小端模式，false 大段模式
     * @return byte[]
     */
    public static byte[] floatToByteArray(float s,boolean little) {
        int value = Float.floatToIntBits(s);
        byte[] targets = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((value >>> offset) & 0xff);
        }

        if(little){
            targets = FormatTransfer.bytesReverseOrder(targets);
        }

        return targets;
    }

    /**
     * long to byte[]
     *
     * @param s long
     * @param little boolean  true 小端模式，false 大段模式
     * @return byte[]
     */
    public static byte[] longToByteArray(long s,boolean little) {
        byte[] targets = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }

        if(little){
            targets = FormatTransfer.bytesReverseOrder(targets);
        }

        return targets;
    }

    /**
     * 32位int转byte[]
     */
    public static byte[] int2byte(int res,boolean little) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。

        if(little){
            targets = FormatTransfer.bytesReverseOrder(targets);
        }
        return targets;
    }

    public static short[] toShortArray(byte[] src) {

        int count = src.length >> 1;
        short[] dest = new short[count];
        for (int i = 0; i < count; i++) {
            dest[i] = (short) (src[i * 2] << 8 | src[2 * i + 1] & 0xff);
        }
        return dest;
    }

    public static byte[] toByteArray(short[] src) {

        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[i * 2] = (byte) (src[i] >> 8);
            dest[i * 2 + 1] = (byte) (src[i] >> 0);
        }

        return dest;
    }


}

