package com.lepu.serial.uitl;

public class ByteUtils {

    public static int byte2UInt(byte b) {
        return b & 0xff;
    }

    /*
     * @param 2 byte
     * @return
     */
    public static int bytes2UIntBig(byte b1, byte b2) {
        return (((b1 & 0xff) << 8) + (b2 & 0xff));
    }

    public static int bytes2UIntBig(byte b1, byte b2, byte b3, byte b4) {
        return (((b1 & 0xff) << 24) + ((b2 & 0xff) << 16) + ((b3 & 0xff) << 8) + (b4 & 0xff));
    }

    public static int bytes2Short(byte b1, byte b2) {
        return ((b1 & 0xFF) | (short) (b2 << 8));
    }

    public static byte[] add(byte[] ori, byte[] add) {
        if (ori == null) {
            return add;
        }

        byte[] n = new byte[ori.length + add.length];
        System.arraycopy(ori, 0, n, 0, ori.length);
        System.arraycopy(add, 0, n, ori.length, add.length);

        return n;
    }


    public static byte[] short2byte(short s) {
        byte[] b = new byte[2];
        b[0] = (byte) ((s >> 0) & 0xff);
        b[1] = (byte) ((s >> 8) & 0xff);
        return b;
    }

    public static void main(String[] args) {
     /*   byte a = (byte) 0x55;
        byte b = (byte) 0x03;
        short c = (short) bytes2Short(a, b);
        System.out.println(c + "");

       byte[] d= short2byte(c);

        byte e=d[1];
        byte f=d[0];

        short g = (short) bytes2Short(e, f);
        System.out.println(g + "");*/

        byte[] s = short2byte((short) 0x1FFC);
        short d = (short) bytes2Short(s[0], s[1]);
        System.out.println();


    }


}
