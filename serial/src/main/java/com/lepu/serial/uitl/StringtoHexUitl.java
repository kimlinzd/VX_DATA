package com.lepu.serial.uitl;


public class StringtoHexUitl {

    public static byte[] toByteArray(String s) {
        byte[] buf = new byte[s.length() / 2];
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) ((Character.digit(s.charAt(j++), 16) << 4) | Character
                    .digit(s.charAt(j++), 16));
        }
        return buf;
    }

    /**
     * byte[]转HexStr
     *
     * @param byteArray
     * @return
     */
    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    // 	获取单个bit值：b为传入的字节，i为第几位（范围0-7），如要获取bit0，则i=0
    public static int getBit(byte b, int i) {
        int bit = (int) ((b >> i) & 0x1);
        return bit;
    }

    //获取高四位
    public static int getHeight4(byte data) {
        int height = ((data & 0xf0) >> 4);
        return height;
    }

    //获取低四位
    public static int getLow4(byte data) {
        int low = (data & 0x0f);
        return low;
    }

    //获取多个连续的bit值： b为传入的字节，start是起始位，length是长度，如要获取bit0-bit4的值，则start为0，length为5
    public static int getBits(byte b, int start, int length) {
        int bit = (int) ((b >> start) & (0xFF >> (8 - length)));
        return bit;
    }

    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    public static void main(String[] args) {
        //包头AA 55 长度27 index 83 class F3 token 01 type 00 status0 04 status1 03
        //07 03 3C 00 00 00 00 00 00 C5 FF 00 00 00 00 D7 FF 00 00 00 00 F0 FF 00 00 00 00 01 00 6E
        //采样点数
        byte[] data = {(byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0x83, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03
                , (byte) 0x07, (byte) 0x03, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                , (byte) 0xC5, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xD7, (byte) 0xFF, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xF0, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x01, (byte) 0x00, (byte) 0x6E
        };


System.out.println(byteToBit((byte) 0x03));

    }


}
