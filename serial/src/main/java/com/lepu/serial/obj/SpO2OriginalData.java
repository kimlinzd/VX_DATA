package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 血氧SpO2  上传波形数据_原始数据
 */
public class SpO2OriginalData implements Serializable,Cloneable {
    int originalFlag;//是否有原始数据 0没有原始数据 1有原始数据
    int len;
    int[] BFFlag;//BF[x]为脉搏标志位 其中x代表波形数据索引 顺序为BF7 BF6 BF5 BF4 BF3 BF2 BF1 BF0
    short[][] spO2Original;

    public SpO2OriginalData() {
    }

    public SpO2OriginalData(byte[] buf) {
        originalFlag = buf[0] >> 4;
        len = buf[0] & 0x0f;
        BFFlag = new int[]{buf[1] >> 7 & 0x1,
                buf[1] >> 6 & 0x1,
                buf[1] >> 5 & 0x1,
                buf[1] >> 4 & 0x1,
                buf[1] >> 3 & 0x1,
                buf[1] >> 2 & 0x1,
                buf[1] >> 1 & 0x1,
                buf[1] >> 0 & 0x1};
        spO2Original = new short[len][5];
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < 4; j++) {
                    int index1 = j * 2 + i * 2 * 4 + 2;
                    int index2 = j * 2 + i * 2 * 4 + 3;
                    spO2Original[i][j] = (short) ByteUtils.bytes2Short(buf[index1], buf[index2]);
                }
            }
        }

    }

    @NonNull
    @Override
    protected SpO2OriginalData clone() throws CloneNotSupportedException {
        return (SpO2OriginalData)super.clone();
    }
}
