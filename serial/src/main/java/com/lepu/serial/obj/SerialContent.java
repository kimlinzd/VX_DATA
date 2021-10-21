package com.lepu.serial.obj;

import androidx.annotation.Nullable;

public class SerialContent {
    byte token;
    byte type;
    byte[] data;

    public static byte TOKEN_COMMON = 0x00;
        public static byte TYPE_RESET = 0x00; //复位参数板
        public static byte TYPE_INFO = 0x01; //查询参数板信息
        public static byte TYPE_PARAM = 0x02; //查询总参数板参数
        public static byte TYPE_PATIENT = 0x03; //设置病人类型
        public static byte TYPE_DATA_START = 0x04; //启动数据传输
        public static byte TYPE_DATA_STOP = 0x05; //停止数据传输
        public static byte TYPE_MAIN_INFO = 0x06; //总参数板信息（主动上报）
    public static byte TOKEN_ECG = 0x01;
        public static byte TYPE_DATA_ECG = 0x00; // 上传心电数据
    public static byte TOKEN_RESP = 0x02;
        public static byte TYPE_DATA_RESP = 0x00; // 上传心电数据
    public static byte TOKEN_TEMP = 0x03;
        public static byte TYPE_DATA_TEMP = 0x00; // 上传心电数据
    public static byte TOKEN_NIBP = 0x04;
        public static byte TYPE_DATA_NIBP = 0x00; // 上传心电数据
    public static byte TOKEN_OXI = 0x06;
        public static byte TYPE_DATA_OXI = 0x00; // 上传心电数据

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
        int len = data == null ? 2 : (data.length+2);
        byte[] buf = new byte[len];
        buf[0] = token;
        buf[1] = type;
        if (len > 2) {
            System.arraycopy(data, 0, buf, 2, len-2);
        }

        return buf;
    }
}
