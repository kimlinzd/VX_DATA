package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 体温数据
 * Token	Type
 * 0x03	    0x00
 */
public class TempData implements Serializable, Cloneable {
    /**
     * 0x00正常；0x01无体温模块；0x02自检失败 顺序为 err3 err2 err1 err0
     */
    boolean errFlag;
    /**
     * 当前的采样点数，最大为4（默认值为1，固定不变）
     */
    int num;
    /**
     * T1探头脱落标识。 0不脱落，1脱落
     */
    boolean sensorFlagT1;
    /**
     * T2探头脱落标识。 0不脱落，1脱落
     */
    boolean sensorFlagT2;

    /**
     * 内容为：体温数据ShortData[num] 小端
     * 体温数据，单位：0.1摄氏度（其中数据为0x8000时代表测量范围外的无效值（--））
     */
    short temp1;

    /**
     * 内容为：体温数据ShortData[num] 小端
     * 体温数据，单位：0.1摄氏度（其中数据为0x8000时代表测量范围外的无效值（--））
     */
    short temp2;

    byte[] originalData;//原始数据 用于保存

    public TempData() {
    }

    public TempData(byte[] buf) {

        errFlag = (buf[0] >> 4 & 0x1) == 1;
        num = buf[0] & 0x0f;
        sensorFlagT1 = (buf[1] >> 0 & 0x01) == 1;
        sensorFlagT2 = (buf[1] >> 4 & 0x01) == 1;

        if (num > 0) {
            temp1 = (short) ByteUtils.bytes2Short(buf[2], buf[3]);
            if (buf.length > 4) {
                temp2 = (short) ByteUtils.bytes2Short(buf[4], buf[5]);
            }
        /*    tempWave = new short[num];
            for (int i = 0; i < num; i++) {
                tempWave[i] = (short) ByteUtils.bytes2Short(buf[i * 2 + 2], buf[i * 2 + 3]);
            }*/
        }

        originalData = buf;

    }


    public boolean isErrFlag() {
        return errFlag;
    }

    public void setErrFlag(boolean errFlag) {
        this.errFlag = errFlag;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSensorFlagT1() {
        return sensorFlagT1;
    }

    public void setSensorFlagT1(boolean sensorFlagT1) {
        this.sensorFlagT1 = sensorFlagT1;
    }

    public boolean isSensorFlagT2() {
        return sensorFlagT2;
    }

    public void setSensorFlagT2(boolean sensorFlagT2) {
        this.sensorFlagT2 = sensorFlagT2;
    }

    public short getTemp1() {
        return temp1;
    }

    public void setTemp1(short temp1) {
        this.temp1 = temp1;
    }

    public short getTemp2() {
        return temp2;
    }

    public void setTemp2(short temp2) {
        this.temp2 = temp2;
    }

    public byte[] getOriginalData() {
        return originalData;
    }

    public void setOriginalData(byte[] originalData) {
        this.originalData = originalData;
    }

    @NonNull
    @Override
    protected TempData clone() throws CloneNotSupportedException {
        return (TempData)super.clone();
    }


}
