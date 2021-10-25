package com.lepu.serial.obj;

import com.lepu.serial.uitl.ByteUtils;

/**
 * 血压NIBP 上传试试袖带压
 */
public class NibpData {
    int len;//当前采样数
    short[] nibpWave;

    public NibpData(byte[] buf) {
        len = buf[0] & 0x0f;
        if (len > 0) {
            nibpWave = new short[len];
            for (int i=0; i<len; i++) {
                nibpWave[i] = (short) ByteUtils.bytes2Short(buf[i*2+2], buf[i*2+3]);
            }
        }

    }
}
