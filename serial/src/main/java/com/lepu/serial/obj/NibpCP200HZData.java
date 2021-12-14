package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 血压NIBP  实时原始数据（200Hz）
 */
public class NibpCP200HZData implements Serializable,Cloneable {
    /**
     *袖带压数据，单位：mmHg
     */
    int CUFF;

    /**
     *袖带压AD值数据
     */
    int AD;

    /**
     *脉搏波1数据
     */
    int P1;

    /**
     *脉搏波2数据
     */
    int P2;

    byte[] originalData;//原始数据 用于保存

    public NibpCP200HZData() {
    }

    public NibpCP200HZData(byte[] buf) {
        CUFF= ByteUtils.bytes2Short(buf[1],buf[0]);
        AD= ByteUtils.bytes2Short(buf[3],buf[2]);
        P1= ByteUtils.bytes2Short(buf[5],buf[4]);
        P2= ByteUtils.bytes2Short(buf[7],buf[6]);
        originalData=buf;
      }

    public byte[] getOriginalData() {
        return originalData;
    }

    public void setOriginalData(byte[] originalData) {
        this.originalData = originalData;
    }

    public int getCUFF() {
        return CUFF;
    }

    public void setCUFF(int CUFF) {
        this.CUFF = CUFF;
    }

    public int getAD() {
        return AD;
    }

    public void setAD(int AD) {
        this.AD = AD;
    }

    public int getP1() {
        return P1;
    }

    public void setP1(int p1) {
        P1 = p1;
    }

    public int getP2() {
        return P2;
    }

    public void setP2(int p2) {
        P2 = p2;
    }

    @NonNull
    @Override
    protected NibpCP200HZData clone() throws CloneNotSupportedException {
        return (NibpCP200HZData)super.clone();
    }
}
