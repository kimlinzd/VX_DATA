package com.lepu.serial.constant;

import androidx.annotation.Nullable;

import com.lepu.serial.obj.EcgData;
import com.lepu.serial.obj.EcgDemoWave;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.uitl.ByteUtils;

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
    /********************************参数板整体******************************************/
    public static final byte TOKEN_PARAM = 0x00;
    public static final byte TYPE_RESET = 0x00; //复位参数板
    public static final byte TYPE_VERSION_INFO = 0x01; //查询参数板信息
    public static final byte TYPE_SET_PARAM = 0x02; //设置总参数
    public static final byte TYPE_PATIENT = 0x03; //设置病人类型
    public static final byte TYPE_DATA_START = 0x04; //启动数据传输
    public static final byte TYPE_DATA_STOP = 0x05; //停止数据传输

    /********************************心电ECG******************************************/
    public static final byte TOKEN_ECG = 0x01;
    public static final byte TYPE_ECG_DATA = 0x00;// 上传心电数据
    public static final byte TYPE_ECG_LEAD_MODE = 0x01;// 设置导联模式
    public static final byte TYPE_CALIBRATION_SIGNAL = 0x02;// 设置定标信号

    /********************************呼吸RESP******************************************/
    public static final byte TOKEN_RESP = 0x02;
    public static final byte TYPE_RESP_DATA = 0x00; //上传呼吸RESP
    public static final byte TYPE_RESP_LEAD = 0x01; //设置呼吸导联
    public static final byte TYPE_SUFFOCATION_ALARM_TIME = 0x02; //设置窒息报警时间

    /********************************体温TEMP******************************************/
    public static final byte TOKEN_TEMP = 0x03;
    public static final byte TYPE_TEMP_DATA = 0x00;//上传体温数据

    /********************************血压NIBP******************************************/

    public static final byte TOKEN_NIBP = 0x04;
    public static final byte TYPE_NIBP_REPLY_PACKET = 0x01; //应答包
    public static final byte NIBP_REPLY_PACKET_0=(byte)0x4F;
    public static final byte NIBP_REPLY_PACKET_K=(byte)0x4B;
    public static final byte NIBP_REPLY_PACKET_B=(byte)0x42;
    public static final byte NIBP_REPLY_PACKET_A=(byte)0x41;
    public static final byte NIBP_REPLY_PACKET_N=(byte)0x4E;
    public static final byte NIBP_REPLY_PACKET_S=(byte)0x53;
    public static final byte NIBP_REPLY_PACKET_R=(byte)0x52;
    public static final byte TOKEN_NIBP_DATA_5HZ = 0x02;//实时袖带压数据（5Hz）
    public static final byte TOKEN_NIBP_DATA_200HZ = 0x03;//实时原始数据（200Hz）
    public static final byte TOKEN_NIBP_BLOOD_PRESSURE_PARAM_MODULE_STATUS  = 0x04;//血压参数和模块状态
    public static final byte TOKEN_NIBP_WORKING_STATUS_OF_BLOOD_PRESSURE_MODULE  = 0x05;//血压模块工作状态
    public static final byte TOKEN_NIBP_BLOOD_PRESSURE_MODULE_INFO  = 0x06;//血压模块信息
    public static final byte TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT  = 0x01;//开始手动血压测量
    public static final byte TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT  = 0x02;//开始连续测量
    public static final byte TOKEN_NIBP_CANCEL_MEASUREMENT  = 0x03;//取消测量
    public static final byte TOKEN_NIBP_SET_PATIENT_TYPE  = 0x04;// 设置病人类型
    public static final byte TOKEN_NIBP_SET_INITIAL_INFLATION_PRESSURE  = 0x05;// 设置初始充气压
    public static final byte TOKEN_NIBP_SET_WAVE_TRANSMISSION_MODE  = 0x06;// 设置波形传输模式
    public static final byte TOKEN_NIBP_READ_BLOOD_PRESSURE_PARAMETERS  = 0x07;// 读取血压参数
    public static final byte TOKEN_NIBP_READ_THE_WORKING_STATUS_OF_THE_BLOOD_PRESSURE_MODULE  = 0x08;// 读取血压模块工作状态
    public static final byte TOKEN_NIBP_READ_BLOOD_PRESSURE_MODULE_INFO  = 0x09;// 读取血压模块信息
    public static final byte TOKEN_NIBP_CONTROL_PUMP  = 0x0A;//控制泵
    public static final byte TOKEN_NIBP_CONTROL_QUICK_VALVE  = 0x0B;//控制快阀
    public static final byte TOKEN_NIBP_CONTROL_SLOW_VALVE  = 0x0C;//控制慢阀
    public static final byte TOKEN_NIBP_SLEEP_MODE  = 0x0D;//睡眠模式
    public static final byte TOKEN_NIBP_RESET_MODULE  = 0x0E;//复位模块
    public static final byte TOKEN_NIBP_AUXILIARY_VENIPUNCTURE  = 0x0F;//辅助静脉穿刺
    public static final byte TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1  = 0x10;//压力校验模式1（内部充气源）
    public static final byte TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2  = 0x11;//压力校验模式2（外部充气源）
    public static final byte TOKEN_NIBP_LEAK_DETECTION  = 0x12;//漏气检测
    public static final byte TOKEN_NIBP_CALIBRATE_THE_PRESSURE_SENSOR  = (byte) 0xF0;//校准压力传感器
    public static final byte TOKEN_NIBP_PRESSURE_REPLY   = (byte) 0x20;//血压参数应答

    /********************************血氧SpO2******************************************/
    public static final byte TOKEN_SP02 = 0x06;     //血氧SpO2
    public static final byte TYPE_DATA_SP02_WAVE = 0x01;//血氧SpO2 波形数据
    public static final byte TYPE_DATA_SP02 = 0x2; //  血氧SpO2 数据



   /* public static final byte TYPE_DATA_NIBP = 0x00; //上传实时袖带压 血压NIBP
    public static final byte TYPE_DATA_NIBP_ORIGINAL = 0x01;//上传实时原始数据 血压NIBP*/




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
    /*    short[][] ecgTestShort = new short[3][4];
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


        EcgData ecgData2=new EcgData(ecgTestShort);*/

        System.out.println();
    }


}
