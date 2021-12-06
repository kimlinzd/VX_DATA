package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 血压NIBP 实时袖带压（5Hz）
 */

public class NibpCP5HZData implements Serializable,Cloneable{
    /**
     *波形数据
     */
   int DB1;
    /**
     *保留
     */
   byte DB3;

    public NibpCP5HZData() {
    }

    public NibpCP5HZData(byte[] buf) {
        DB1=ByteUtils.bytes2Short(buf[1],buf[0]);
         DB3=buf[2];
    }

    public int getDB1() {
        return DB1;
    }

    public void setDB1(int DB1) {
        this.DB1 = DB1;
    }

    public byte getDB3() {
        return DB3;
    }

    public void setDB3(byte DB3) {
        this.DB3 = DB3;
    }

    @NonNull
    @Override
    protected NibpCP5HZData clone() throws CloneNotSupportedException {
        return (NibpCP5HZData)super.clone();
    }
}
