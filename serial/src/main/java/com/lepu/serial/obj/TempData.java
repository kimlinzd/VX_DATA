package com.lepu.serial.obj;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 体温数据
 * Token	Type
 * 0x03	    0x00
 */
public class TempData implements Serializable ,Cloneable   {
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

    byte[] originalData;//原始数据 用于保存

    public TempData() {
    }

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

        originalData=buf;

    }


    public byte[] getOriginalData() {
        return originalData;
    }

    public void setOriginalData(byte[] originalData) {
        this.originalData = originalData;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int[] getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(int[] errFlag) {
        this.errFlag = errFlag;
    }

    public int[] getSensorFlag() {
        return sensorFlag;
    }

    public void setSensorFlag(int[] sensorFlag) {
        this.sensorFlag = sensorFlag;
    }

    public short[] getTempWave() {
        return tempWave;
    }

    public void setTempWave(short[] tempWave) {
        this.tempWave = tempWave;
    }

    @NonNull
    @Override
    protected TempData clone() throws CloneNotSupportedException {
        return (TempData)super.clone();
    }


}
