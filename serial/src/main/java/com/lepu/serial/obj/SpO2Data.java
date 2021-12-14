package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 *SpO2数据
 */
public class SpO2Data implements Serializable,Cloneable {


    int status0;//一级错误码
    int status1;//二级级错误码
    int spO2;//血氧饱和度
    int PR;//脉率数据
    int PI;//灌注度数据




    public SpO2Data() {
    }

    public SpO2Data(byte[] buf) {
        status0= ByteUtils.bytes2Short(buf[0], buf[1]);
        status1= ByteUtils.bytes2Short(buf[2], buf[3]);
        spO2=buf[4];
        PR= ByteUtils.bytes2Short(buf[5], buf[6]);
        PI= ByteUtils.bytes2Short(buf[7], buf[8]);
    }

    @NonNull
    @Override
    protected SpO2Data clone() throws CloneNotSupportedException {
        return (SpO2Data)super.clone();
    }
}
