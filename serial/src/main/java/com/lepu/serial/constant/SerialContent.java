package com.lepu.serial.constant;

import androidx.annotation.Nullable;

public class SerialContent {
    public byte token;
    public byte type;
    public byte[] data;
    /**
     * SYNC_H		：同步包头高八位，固定为 0xAA
     */
    public static final byte SYNC_H = (byte) 0xAA;
    /**
     * SYNC_L		：同步包头低八位，固定为 0x55
     */
    public static final byte SYNC_L = (byte) 0x55;

    public static final byte TOKEN_COMMON = 0x00;
    public static final byte TYPE_RESET = 0x00; //复位参数板
    public static final byte TYPE_INFO = 0x01; //查询参数板信息
    public static final byte TYPE_PARAM = 0x02; //查询总参数板参数
    public static final byte TYPE_PATIENT = 0x03; //设置病人类型
    public static final byte TYPE_DATA_START = 0x04; //启动数据传输
    public static final byte TYPE_DATA_STOP = 0x05; //停止数据传输
    public static final byte TYPE_MAIN_INFO = 0x06; //总参数板信息（主动上报）
    public static final byte TOKEN_ECG = 0x01;// 上传心电数据
    public static final byte TYPE_DATA_ECG = 0x00;
    public static final byte TOKEN_RESP = 0x02;    //上传呼吸RESP
    public static final byte TYPE_DATA_RESP = 0x00;
    public static final byte TOKEN_TEMP = 0x03;    //上传体温数据
    public static final byte TYPE_DATA_TEMP = 0x00;
    public static final byte TOKEN_NIBP = 0x04;    //血压NIBP
    public static final byte TYPE_DATA_NIBP = 0x00; //上传实时袖带压 血压NIBP
    public static final byte TYPE_DATA_NIBP_ORIGINAL = 0x01;//上传实时原始数据 血压NIBP

    public static final byte TOKEN_SP02 = 0x06;     //血氧SpO2
    public static final byte TYPE_DATA_SP02 = 0x00; //上传波形数据_原始数据 血氧SpO2
    public static final byte TYPE_DATA_SP02_ORIGINAL = 0x00;//上传SpO2数据 血氧SpO2

    public SerialContent(byte[] buf) {
        token = buf[0];
        type = buf[1];
        int len = buf.length - 2;
        if (len > 0) {
            data = new byte[len];
            System.arraycopy(buf, 2, data, 0, len);
        }
    }

    public SerialContent(byte token, byte type, @Nullable byte[] data) {
        this.token = token;
        this.type = type;
        this.data = data;
    }

    public byte[] toBytes() {
        int len = data == null ? 2 : (data.length + 2);
        byte[] buf = new byte[len];
        buf[0] = token;
        buf[1] = type;
        if (len > 2) {
            System.arraycopy(data, 0, buf, 2, len - 2);
        }

        return buf;
    }
}
