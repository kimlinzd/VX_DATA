package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 血压NIBP  实时原始数据（200Hz）
 */
public class NibpOriginalData implements Serializable,Cloneable {
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

    public NibpOriginalData() {
    }

    public NibpOriginalData(byte[] buf) {
        CUFF= ByteUtils.bytes2Short(buf[0],buf[1]);
        AD= ByteUtils.bytes2Short(buf[0],buf[1]);
        P1= ByteUtils.bytes2Short(buf[0],buf[1]);
        P2= ByteUtils.bytes2Short(buf[0],buf[1]);
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
    protected NibpOriginalData clone() throws CloneNotSupportedException {
        return (NibpOriginalData)super.clone();
    }
}
