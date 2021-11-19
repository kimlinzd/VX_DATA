package com.lepu.serial.obj;


import com.lepu.serial.uitl.ByteUtils;

/**
 * 呼吸数据
 * Token	Type
 * 0x02	    0x00
 */
public class RespData {

    int dataFormat; //0:波形数据; 1:原始数据
    int leadIndex;//0x0(LA) 0x1(LL)
    int num;//当前采样点数，最大值为4（默认值为1，固定不变）
    /**
     * 0:无标志 1：有标志 B（x）为肺动标志，Apnea(x)为窒息报警标志，其中（x）代表采样点索引·
     * 顺序为 B3 B2 B1 B0 Apnea3 Apnea2 Apnea1 Apnea0
     */
    int[] flag;
    int apenaDelay;//窒息报警时间，单位：秒
    int rr; // 呼吸率
    short[] respWave;//呼吸数据

    public RespData(byte[] buf) {
        dataFormat = buf[0] >> 6;
        leadIndex = buf[0] >> 4 & 3;
        num = buf[0] & 0x0f;
        flag = new int[]{buf[1] >> 7 & 0x1, buf[1] >> 6 & 0x1, buf[1] >> 5 & 0x1, buf[1] >> 4 & 0x1, buf[1] >> 3 & 0x1, buf[1] >> 2 & 0x1, buf[1] >> 1 & 0x1, buf[1] >> 0 & 0x1};
        apenaDelay = buf[2] & 0xff;
        rr = (buf[3] & 0xff + ((buf[4] & 0xff) << 8));
        if (num > 0) {
            respWave = new short[num];
            for (int i=0; i<num; i++) {
                respWave[i] = (short) ByteUtils.bytes2Short(buf[i*2+5], buf[i*2+6]);
            }
        }

     /*   format = buf[0]>>4;
        len = buf[0] & 0x0f;
        lungMove = (buf[1] & 0x01) == 0x01;
        asphyxia = (buf[1] & 0x10) == 0x10;

        if (len > 0) {
            respWave = new short[len];
            for (int i=0; i<len; i++) {
                respWave[i] = (short) ByteUtils.bytes2Short(buf[i*2+4], buf[i*2+5]);
            }
        }*/
    }

    public static void main(String[] args) {

    }

}
