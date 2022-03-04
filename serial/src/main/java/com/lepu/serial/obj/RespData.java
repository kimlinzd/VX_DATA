package com.lepu.serial.obj;


import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 呼吸数据
 * Token	Type
 * 0x02	    0x00
 */
public class RespData implements Serializable,Cloneable {

    int dataFormat; //0:波形数据; 1:原始数据
    int leadIndex;//0x0(LA) 0x1(LL)
    int num;//当前采样点数，最大值为4（默认值为1，固定不变）
    /**
     * 0:无标志 1：有标志 B（x）为肺动标志，Apnea(x)为窒息报警标志，其中（x）代表采样点索引·
     * 顺序为 B3 B2 B1 B0 Apnea3 Apnea2 Apnea1 Apnea0
     */
    int[] flag;
    int apneaDelay;//窒息报警时间，单位：秒
    int rr; // 呼吸率
    short[] respWave;//呼吸数据

    byte[] originalData;//原始数据 用于保存

    public RespData() {
    }

    public RespData(byte[] buf) {
        dataFormat = buf[0] >> 6;
        leadIndex = buf[0] >> 4 & 3;
        num = buf[0] & 0x0f;
        flag = new int[]{buf[1] >> 7 & 0x1, buf[1] >> 6 & 0x1, buf[1] >> 5 & 0x1, buf[1] >> 4 & 0x1, buf[1] >> 3 & 0x1, buf[1] >> 2 & 0x1, buf[1] >> 1 & 0x1, buf[1] >> 0 & 0x1};
        apneaDelay = buf[2] & 0xff;
       rr = (buf[3] & 0xff | ((short)(buf[4]) << 8));
        //     rr=ByteUtils.bytes2Short(buf[3],buf[4]);
        if (num > 0) {
            respWave = new short[num];
            for (int i=0; i<num; i++) {
                respWave[i] = (short) ByteUtils.bytes2Short(buf[i*2+5], buf[i*2+6]);
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

    public int getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(int dataFormat) {
        this.dataFormat = dataFormat;
    }

    public int getLeadIndex() {
        return leadIndex;
    }

    public void setLeadIndex(int leadIndex) {
        this.leadIndex = leadIndex;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int[] getFlag() {
        return flag;
    }

    public void setFlag(int[] flag) {
        this.flag = flag;
    }

    public int getApneaDelay() {
        return apneaDelay;
    }

    public void setApneaDelay(int apneaDelay) {
        this.apneaDelay = apneaDelay;
    }

    public int getRr() {
        return rr;
    }

    public void setRr(int rr) {
        this.rr = rr;
    }

    public short[] getRespWave() {
        return respWave;
    }

    public void setRespWave(short[] respWave) {
        this.respWave = respWave;
    }

    @NonNull
    @Override
    protected RespData clone() throws CloneNotSupportedException {
        return (RespData)super.clone();
    }
}
