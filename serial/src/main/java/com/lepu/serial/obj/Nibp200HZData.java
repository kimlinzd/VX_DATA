package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 血压NIBP  实时原始数据（200Hz）
 */
public class Nibp200HZData implements Serializable,Cloneable {
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

    public Nibp200HZData() {
    }

    public Nibp200HZData(byte[] buf) {
        CUFF= ByteUtils.bytes2Short(buf[0],buf[1]);
        AD= ByteUtils.bytes2Short(buf[2],buf[3]);
        P1= ByteUtils.bytes2Short(buf[4],buf[5]);
        P2= ByteUtils.bytes2Short(buf[6],buf[7]);
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
    protected Nibp200HZData clone() throws CloneNotSupportedException {
        return (Nibp200HZData)super.clone();
    }
}
