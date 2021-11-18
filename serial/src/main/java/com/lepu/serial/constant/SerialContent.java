package com.lepu.serial.constant;

import com.lepu.serial.obj.EcgData;
import com.lepu.serial.obj.EcgDemoWave;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.uitl.ByteUtils;

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
    /**
     * 是否是测试数据
     */
    public static boolean IS_TEST_DATA = false;

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

    public static final byte[] TEST_ECG_DATA_HEAD = {(byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0x6B, (byte) 0xF3, (byte) 0x01, (byte) 0x00,
            (byte) 0x04, (byte) 0x03, (byte) 0x00, (byte) 0x03, (byte) 0x3C, (byte) 0x00, (byte) 0x00};

    /**
     * 测试数据
     */
    public static final byte[] TESTDATA = {(byte) 0xAA, (byte) 0x55, (byte) 0x08, (byte) 0xE1, (byte) 0xF1, (byte) 0x00, (byte) 0x04, (byte) 0x89,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xE2, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xEE,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xE3, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x64, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x41, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3C, (byte) 0xFE, (byte) 0xBC,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xE4, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D,
            (byte) 0xAA, (byte) 0x55, (byte) 0x14, (byte) 0xE5, (byte) 0xF3, (byte) 0x06, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x19,
            (byte) 0xAA, (byte) 0x55, (byte) 0x19, (byte) 0xE6, (byte) 0xF3, (byte) 0x04, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xF7,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xE7, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xB2, (byte) 0xEE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93, (byte) 0xE7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x42, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x94, (byte) 0xDA, (byte) 0xF1,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xE8, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4D,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xE9, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x71,
            (byte) 0xAA, (byte) 0x55, (byte) 0x14, (byte) 0xEA, (byte) 0xF3, (byte) 0x06, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x92,
            (byte) 0xAA, (byte) 0x55, (byte) 0x19, (byte) 0xEB, (byte) 0xF3, (byte) 0x04, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x81,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xEC, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0xE1, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x8E, (byte) 0xED, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x66, (byte) 0xF9, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x06, (byte) 0xEB,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xED, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x90,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xEE, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xA1,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xEF, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4B, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3E, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x91, (byte) 0xEE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8B, (byte) 0xE7, (byte) 0x42,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xF0, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x27,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xF1, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0F,
            (byte) 0xAA, (byte) 0x55, (byte) 0x14, (byte) 0xF2, (byte) 0xF3, (byte) 0x06, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x9E,
            (byte) 0xAA, (byte) 0x55, (byte) 0x19, (byte) 0xF3, (byte) 0xF3, (byte) 0x04, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xA0,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xF4, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4A, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, (byte) 0x9A, (byte) 0xDA, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x82, (byte) 0xED, (byte) 0x32,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xF5, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFA,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xF6, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xDF,
            (byte) 0xAA, (byte) 0x55, (byte) 0x14, (byte) 0xF7, (byte) 0xF3, (byte) 0x06, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0xAA, (byte) 0x55, (byte) 0x08, (byte) 0xE1, (byte) 0xF1, (byte) 0x00, (byte) 0x04, (byte) 0x89,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xE2, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xEE,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xE3, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x64, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x41, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3C, (byte) 0xFE, (byte) 0xBC,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xE4, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D,
            (byte) 0xAA, (byte) 0x55, (byte) 0x14, (byte) 0xE5, (byte) 0xF3, (byte) 0x06, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x19,
            (byte) 0xAA, (byte) 0x55, (byte) 0x19, (byte) 0xE6, (byte) 0xF3, (byte) 0x04, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xF7,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xE7, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xB2, (byte) 0xEE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93, (byte) 0xE7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x42, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x94, (byte) 0xDA, (byte) 0xF1,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xE8, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4D,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xE9, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x71,
            (byte) 0xAA, (byte) 0x55, (byte) 0x14, (byte) 0xEA, (byte) 0xF3, (byte) 0x06, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x92,
            (byte) 0xAA, (byte) 0x55, (byte) 0x19, (byte) 0xEB, (byte) 0xF3, (byte) 0x04, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x81,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xEC, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0xE1, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x8E, (byte) 0xED, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x66, (byte) 0xF9, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x06, (byte) 0xEB,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xED, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x90,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xEE, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xA1,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xEF, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4B, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3E, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x91, (byte) 0xEE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8B, (byte) 0xE7, (byte) 0x42,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xF0, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x27,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xF1, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0F,
            (byte) 0xAA, (byte) 0x55, (byte) 0x14, (byte) 0xF2, (byte) 0xF3, (byte) 0x06, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x9E,
            (byte) 0xAA, (byte) 0x55, (byte) 0x19, (byte) 0xF3, (byte) 0xF3, (byte) 0x04, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xA0,
            (byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0xF4, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x01, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4A, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, (byte) 0x9A, (byte) 0xDA, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x82, (byte) 0xED, (byte) 0x32,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0E, (byte) 0xF5, (byte) 0xF3, (byte) 0x02, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFA,
            (byte) 0xAA, (byte) 0x55, (byte) 0x0C, (byte) 0xF6, (byte) 0xF3, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xDF,
            (byte) 0xAA, (byte) 0x55, (byte) 0x14, (byte) 0xF7, (byte) 0xF3, (byte) 0x06, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10
    };

    public static void main(String[] args) {
        byte[] ecgdata = new byte[39];
        System.arraycopy(TEST_ECG_DATA_HEAD, 0, ecgdata, 0, 14);
        for (int i = 0; i < 4; i++) {
            System.arraycopy(ByteUtils.short2byte(EcgDemoWave.INSTANCE.getWaveI()[i]), 0, ecgdata, 14 + (i * 6), 2);
            System.arraycopy(ByteUtils.short2byte(EcgDemoWave.INSTANCE.getWaveII()[i]), 0, ecgdata, 14 + 2 + (i * 6), 2);
            System.arraycopy(ByteUtils.short2byte(EcgDemoWave.INSTANCE.getWaveV()[i]), 0, ecgdata, 14 + 4 + (i * 6), 2);
        }
        ecgdata[38] = (byte) 0x9A;

        //解析包
        SerialMsg serialMsg = new SerialMsg(ecgdata);
    //    EcgData ecgData =new EcgData(serialMsg.getContent().data);


        //测数据 拼接数据
        short[][] ecgTestShort = new short[3][4];
        for (int i = 0; i < ecgTestShort.length; i++) {
            for (int j = 0; j < ecgTestShort[0].length; j++) {
                if (i == 0) {
                    ecgTestShort[i][j] = EcgDemoWave.INSTANCE.getWaveI()[j ];
                } else if (i == 1) {
                    ecgTestShort[i][j] = EcgDemoWave.INSTANCE.getWaveII()[j ];
                } else if (i == 2) {
                    ecgTestShort[i][j] = EcgDemoWave.INSTANCE.getWaveV()[j ];
                }
            }
        }


        EcgData ecgData2=new EcgData(ecgTestShort);

        System.out.println();
    }


}
