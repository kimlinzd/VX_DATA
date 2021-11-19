package com.lepu.serial.obj;

import com.lepu.serial.uitl.ByteUtils;

/**
 * 体温数据
 * Token	Type
 * 0x03	    0x00
 */
public class TempData {
    int num;//当前的采样点数，最大为4（默认值为1，固定不变）
    /**
     * 0x00正常；0x01无体温模块；0x02自检失败 顺序为 err3 err2 err1 err0
     */
    int[] errFlag;
    /**
     * 探头脱落标识。 0不脱落，1脱落
     */
    int[] sensorFlag;
    /**
     * 内容为：体温数据ShortData[num] 小端
     * 体温数据，单位：0.1摄氏度（其中数据为0x8000时代表测量范围外的无效值（--））
     */
    short[] tempWave;

    public TempData(byte[] buf) {
        num = buf[0] & 0x0f;
        errFlag = new int[]{buf[1] >> 7 & 0x1, buf[1] >> 6 & 0x1, buf[1] >> 5 & 0x1, buf[1] >> 4 & 0x1};
        sensorFlag = new int[]{buf[1] >> 3 & 0x1, buf[1] >> 2 & 0x1, buf[1] >> 1 & 0x1, buf[1] >> 0 & 0x1};
        if (num > 0) {
            tempWave = new short[num];
            for (int i = 0; i < num; i++) {
                tempWave[i] = (short) ByteUtils.bytes2Short(buf[i * 2 + 2], buf[i * 2 + 3]);
            }
        }

    }


}
