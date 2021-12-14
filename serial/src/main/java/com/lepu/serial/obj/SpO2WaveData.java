package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * 血氧SpO2  波形数据
 */
public class SpO2WaveData implements Serializable, Cloneable {
    int len;//采样点数

    /**
     * 0:无标志  1：有标志    BF(x)为脉搏标志,其中(x)代表采样点索引
     */
    int[] flag;
    /**
     * 波形数据
     */
    short[] wave;

    public SpO2WaveData() {
    }

    public SpO2WaveData(byte[] buf) {
        len = buf[0] & 0x0f;
        flag=new int[len];

        if (len > 0) {
            wave = new short[len];
            for (int i = 0; i < len; i++) {
                flag[i]=buf[1] >> i & 0x1;
                wave[i] = buf[i + 2];
            }
        }
    }

    @NonNull
    @Override
    protected SpO2WaveData clone() throws CloneNotSupportedException {
        return (SpO2WaveData) super.clone();
    }
}
