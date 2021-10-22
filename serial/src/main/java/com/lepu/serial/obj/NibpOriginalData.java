package com.lepu.serial.obj;

import com.lepu.serial.uitl.ByteUtils;

/**
 * 血压NIBP 上传实时原始数据
 */
public class NibpOriginalData {
    int len;//当前采样数
    /**
     * 血压数据 Short Data[Num][4]小端
     * Data[][0]为 CUFF:袖带压数据，单位:mmHg
     * Data[][1]为 AD:袖带压AD值数据
     * Data[][2]为 P1:脉搏波1数据
     * Data[][3]为 DP2:脉搏波2数据
     */
    short[][] nibpOriginalWave;

    public NibpOriginalData(byte[] buf) {
        len = buf[0] & 0x0f;
        nibpOriginalWave=new short[len][4];
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < 4; j++) {
                    int index1 = j * 2 + i * 2 * 4 + 1;
                    int index2 = j * 2 + i * 2 * 4 + 2;
                    nibpOriginalWave[i][j] = (short) ByteUtils.bytes2Short(buf[index1], buf[index2]);
                }
            }


        }

    }
}
