package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * 血压NIBP  实时原始数据（200Hz）
 */
public class NibpOriginalData implements Serializable,Cloneable {
    /**
     *袖带压数据，单位：mmHg
     */
    byte CUFF_H;
    /**
     *袖带压数据，单位：mmHg
     */
    byte CUFF_L;
    /**
     *袖带压AD值数据
     */
    byte AD_H;
    /**
     *袖带压AD值数据
     */
    byte AD_L;
    /**
     *脉搏波1数据
     */
    byte P1_H;
    /**
     *脉搏波1数据
     */
    byte P1_L;
    /**
     *脉搏波2数据
     */
    byte P2_H;
    /**
     *脉搏波2数据
     */
    byte P2_L;


    public NibpOriginalData(byte[] buf) {
        CUFF_H=buf[0];
        CUFF_L=buf[1];
        AD_H=buf[2];
        AD_L=buf[3];
        P1_H=buf[4];
        P1_L=buf[5];
        P2_H=buf[6];
        P2_L=buf[7];

    }

    public byte getCUFF_H() {
        return CUFF_H;
    }

    public void setCUFF_H(byte CUFF_H) {
        this.CUFF_H = CUFF_H;
    }

    public byte getCUFF_L() {
        return CUFF_L;
    }

    public void setCUFF_L(byte CUFF_L) {
        this.CUFF_L = CUFF_L;
    }

    public byte getAD_H() {
        return AD_H;
    }

    public void setAD_H(byte AD_H) {
        this.AD_H = AD_H;
    }

    public byte getAD_L() {
        return AD_L;
    }

    public void setAD_L(byte AD_L) {
        this.AD_L = AD_L;
    }

    public byte getP1_H() {
        return P1_H;
    }

    public void setP1_H(byte p1_H) {
        P1_H = p1_H;
    }

    public byte getP1_L() {
        return P1_L;
    }

    public void setP1_L(byte p1_L) {
        P1_L = p1_L;
    }

    public byte getP2_H() {
        return P2_H;
    }

    public void setP2_H(byte p2_H) {
        P2_H = p2_H;
    }

    public byte getP2_L() {
        return P2_L;
    }

    public void setP2_L(byte p2_L) {
        P2_L = p2_L;
    }

    @NonNull
    @Override
    protected NibpOriginalData clone() throws CloneNotSupportedException {
        return (NibpOriginalData)super.clone();
    }
}
