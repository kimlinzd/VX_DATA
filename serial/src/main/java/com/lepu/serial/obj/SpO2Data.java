package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 *SpO2数据
 */
public class SpO2Data implements Serializable,Cloneable {


    int LF;// bit5 LF:低灌注度 0:灌注度正常  1:低灌注度
    int MD;// bit4 MD:动作检测0:没有动作 1:有动作
    int SL;// bit3 SL:脉搏波识别超时状态0:无超时 1:超时
    int PS;// bit2 PS:脉搏波识别状态 0:识别完毕 1:正在识别
    int PO;// bit1 PO 探头脱落状态 0: 探头正常 1:探头脱落
    int PD;// bit0 PD:探头在位状态0 :探头连接 1:探头没连接
    int saturation;//血氧饱和度 0~100 血氧饱和度  127:无效数据
    /**
     *这个还没定义好 先不取
     */
    short[][] spO2wave;

    public SpO2Data() {
    }

    public SpO2Data(byte[] buf) {
        PD=buf[0] >> 0 & 0x1;
        PO=buf[0] >> 1 & 0x1;
        PS=buf[0] >> 2 & 0x1;
        SL=buf[0] >> 3 & 0x1;
        MD=buf[0] >> 4 & 0x1;
        LF=buf[0] >> 5 & 0x1;
        saturation=buf[1]& 0xff;

    }

    @NonNull
    @Override
    protected SpO2Data clone() throws CloneNotSupportedException {
        return (SpO2Data)super.clone();
    }
}
