package com.lepu.serial.uitl;

import android.content.Context;

import com.lepu.serial.constant.SerialContent;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

    public static short byte2short(byte[] b){
        short l = 0;
        for (int i = 0; i < 2; i++) {
            l<<=8; //<<=和我们的 +=是一样的，意思就是 l = l << 8
            l |= (b[i] & 0xff); //和上面也是一样的  l = l | (b[i]&0xff)
        }
        return l;
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

    /**
     * int转换为小端byte[]（高位放在高地址中）
     *
     * @param iValue
     * @return
     */
    public byte[] Int2Bytes_LE(int iValue) {
        byte[] rst = new byte[4];
        // 先写int的最后一个字节
        rst[0] = (byte) (iValue & 0xFF);
        // int 倒数第二个字节
        rst[1] = (byte) ((iValue & 0xFF00) >> 8);
        // int 倒数第三个字节
        rst[2] = (byte) ((iValue & 0xFF0000) >> 16);
        // int 第一个字节
        rst[3] = (byte) ((iValue & 0xFF000000) >> 24);
        return rst;
    }

    /**
     * 将short型数据转为byte数组 长度是2
     *
     * @param x
     * @param byteOrder 大小端
     * @return
     */
    public static byte[] shortToBytes(short x, ByteOrder byteOrder) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(byteOrder);
        buffer.putShort(x);
        return buffer.array();
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
        short a = 250;
        byte[] b = shortToBytes(a, ByteOrder.BIG_ENDIAN);

        byte[] c = new byte[2];
        c[0] = 250 & 0xff00;
        c[1] = (byte) (250 & 0xff);
        //  short , short & 0xff
        System.out.println();


    }


}
