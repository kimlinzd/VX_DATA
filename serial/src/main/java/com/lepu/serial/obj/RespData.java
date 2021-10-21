package com.lepu.serial.obj;


import com.lepu.serial.uitl.ByteUtils;

/**
 * 呼吸数据
 * Token	Type
 * 0x02	    0x00
 */
public class RespData {

    int format; //0:波形数据; 1:原始数据
    int len;
    boolean lungMove; // 肺动
    boolean asphyxia; // 窒息
    int rr; // 呼吸率
    short[] respWave;

    public RespData(byte[] buf) {
        format = buf[0]>>4;
        len = buf[0] & 0x0f;
        lungMove = (buf[1] & 0x01) == 0x01;
        asphyxia = (buf[1] & 0x10) == 0x10;
        rr = (buf[2] & 0xff + ((buf[3] & 0xff) << 8));
        if (len > 0) {
            respWave = new short[len];
            for (int i=0; i<len; i++) {
                respWave[i] = (short) ByteUtils.bytes2Short(buf[i+4], buf[i+5]);
            }
        }
    }

}
