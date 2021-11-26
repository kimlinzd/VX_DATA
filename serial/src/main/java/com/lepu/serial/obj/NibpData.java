package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 血压NIBP 实时袖带压（5Hz）
 */

public class NibpData implements Serializable,Cloneable{
    /**
     *波形数据高位
     */
   byte DB1;
    /**
     *波形数据低位
     */
   byte DB2;
    /**
     *保留
     */
   byte DB3;

    public NibpData(byte[] buf) {
        DB1=buf[0];
        DB2=buf[1];
        DB3=buf[2];
    }

    public byte getDB1() {
        return DB1;
    }

    public void setDB1(byte DB1) {
        this.DB1 = DB1;
    }

    public byte getDB2() {
        return DB2;
    }

    public void setDB2(byte DB2) {
        this.DB2 = DB2;
    }

    public byte getDB3() {
        return DB3;
    }

    public void setDB3(byte DB3) {
        this.DB3 = DB3;
    }

    @NonNull
    @Override
    protected NibpData clone() throws CloneNotSupportedException {
        return (NibpData)super.clone();
    }
}
