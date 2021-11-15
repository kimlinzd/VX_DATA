package com.lepu.serial.uitl;

import android.content.Context;

import com.lepu.serial.constant.SerialContent;

import java.io.InputStream;

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
    public static short[] shortadd(short[] ori, short[] add) {
        if (ori == null) {
            return add;
        }

        short[] n = new short[ori.length + add.length];
        System.arraycopy(ori, 0, n, 0, ori.length);
        System.arraycopy(add, 0, n, ori.length, add.length);

        return n;
    }

    /**
     * 串口读取专用
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     * @功能 读取流
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        int count = 0;
        while (count == 0 && !SerialContent.IS_TEST_DATA) {
            count = inStream.available();
        }
        byte[] b = new byte[count];
        inStream.read(b);
        return b;
    }

    /**
     * 获取测试数据
     * @param context
     * @return
     */
    public static byte[] getFromAssets(Context context) {
        byte[] result = new byte[0];
        try {
            InputStream in = context.getResources().getAssets().open("ecg_test_data.DAT");
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
