package com.lepu.serial.obj;

import com.lepu.serial.uitl.ByteUtils;

/**
 * 体温数据
 * Token	Type
 * 0x03	    0x00
 */
public class TempData {
    int len;
    boolean sensorOff; //探头脱落标识。 0不脱落，1脱落
    int error; //0x00正常；0x01无体温模块；0x02自检失败
    int sensor;//探头脱落标识 0x00:体温模块正常  0x01:无体温模块 0x02：自检失败
    short[] tempWave;

    public TempData(byte[] buf) {
        len = buf[0] & 0x0f;
        sensorOff = ((buf[1] & 0x01) == 0x01);
        error = buf[1]>>4;
        sensor= buf[1] >> 0 & 0x1;
        if (len > 0) {
            tempWave = new short[len];
            for (int i=0; i<len; i++) {
                tempWave[i] = (short) ByteUtils.bytes2Short(buf[i*2+2], buf[i*2+3]);
            }
        }
    }


}
