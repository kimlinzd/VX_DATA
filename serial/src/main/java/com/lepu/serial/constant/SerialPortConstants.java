package com.lepu.serial.constant;

public  class SerialPortConstants {
    /**
     * 开始传输数据命令
     */
    public static final String CMD_SERIAL_START_TRAMSMIT_DATA = "AA 55 08 00 F0 00 04 E4";
    /**
     * 停止传输数据命令
     */
    public static final String CMD_SERIAL_STOP_TRAMSMIT_DATA = "AA 55 08 00 F0 00 05 BA";

    /**
     * SYNC_H		：同步包头高八位，固定为 0xAA
     */
    public static final byte SYNC_H = (byte) 0xAA;
    /**
     * SYNC_L		：同步包头低八位，固定为 0x55
     */
    public static final byte SYNC_L = (byte) 0x55;
    /*报文类型（Class字节）用于区分不同的报文便于处理。其范围为0xF0 ~ 0xFB。   start*/
    /**
     * 命令包
     */
    public static final byte CLASS_CMD = (byte) 0xF0;
    /**
     * 命令确认包（若有回复包，就不发确认包）
     */
    public static final byte CLASS_ACK = (byte) 0xF1;
    /**
     * 回复包
     */
    public static final byte CLASS_REPLY = (byte) 0xF2;
    /**
     * 数据包
     */
    public static final byte CLASS_DATA = (byte) 0xF3;
    /**
     * 如心跳包，异常状态包等等（主动传输）	双向
     */
    public static final byte CLASS_STATUS = (byte) 0xF4;
    /**
     * 升级包
     */
    public static final byte CLASS_UPDATA = (byte) 0xF5;
    /*报文类型（Class字节）用于区分不同的报文便于处理。其范围为0xF0 ~ 0xFB。   end*/


    /* Token      ：模块类型，用于区分不同的模块，如总模块，心电，血氧等。      start */
    /**
     * 命令
     */
    public static final byte TOKEN_MSG_CMD = (byte) 0x00;
    /**
     * 上传心电数据
     */
    public static final byte TOKEN_MSG_ECG = (byte) 0x01;
    /**
     * 上传呼吸RESP
     */
    public static final byte TOKEN_MSG_RESP = (byte) 0x02;
    /**
     * 上传体温数据
     */
    public static final byte TOKEN_MSG_TEMP = (byte) 0x03;
    /**
     * 上传实时袖带压  血压NIBP
     */
    public static final byte TOKEN_MSG_NIBP = (byte) 0x04;
    /**
     * 上传实时袖带压原始数据  血压NIBP
     */
    public static final byte TOKEN_MSG_ORIGINAL_NIBP = (byte) 0x04;
    /**
     * 上传波形数据_原始数据 血氧SpO2
     */
    public static final byte TOKEN_MSG_SPO2 = (byte) 0x06;
    /**
     * 上传SpO2数据  血氧SpO2
     */
    public static final byte TOKEN_MSG_ORIGINAL_SPO2 = (byte) 0x06;
    /* Token      ：模块类型，用于区分不同的模块，如总模块，心电，血氧等。      end   */


    /*  Type     ：内容种类，用于识别不同的内容，一个模块里有多种内容。    start*/
    /**
     * 命令:复位参数板   命令确定:收到复位命令
     */
    public static final byte TYPE_RESET = (byte) 0x00;
    /**
     * 命令:查询参数板信息  回复:参数板信息
     */
    public static final byte TYPE_PARAMETER_BOARD_INFO = (byte) 0x01;
    /**
     * 命令:查询参数板总参数 回复:总参数板信息
     */
    public static final byte TYPE_GENGRAL_PARAMETER_BOARD_INFO = (byte) 0x02;
    /**
     * 命令:设置病人类型  命令确定:收到设置病人类型
     */
    public static final byte TYPE_SET_PATIENT_TYPE = (byte) 0x03;
    /**
     * 命令:启动数据传输 命令确定:收到启动数据传输
     */
    public static final byte TYPE_START_DATA_TRANSFER = (byte) 0x04;
    /**
     * 命令:停止数据传输 命令确定:停止数据传输
     */
    public static final byte TYPE_STOP_DATA_TRANSFER = (byte) 0x05;
    /**
     * 数据:总参数板信息（主动上报）
     */
    public static final byte TYPE_GENGRAL_PARAMETER_BOARD_INFO_PROACTIVELY_REPORT = (byte) 0x06;
    /**
     * 心电ECG 上传心电数据
     */
    public static final byte TYPE_ECG = (byte) 0x00;
    /**
     * 呼吸RESP 上传呼吸数据
     */
    public static final byte TYPE_RESP = (byte) 0x00;
    /**
     * 体温TEMP 上传体温数据
     */
    public static final byte TYPE_TEMP = (byte) 0x00;
    /**
     * 血压NIBP 上传实时袖带压
     */
    public static final byte TYPE_NIBP = (byte) 0x00;
    /**
     * 血压NIBP 上传实时原始数据
     */
    public static final byte TYPE_NIBP_ORIGINAL = (byte) 0x01;
    /**
     * 血氧SpO2 上传波形数据_原始数据
     */
    public static final byte TYPE_SPO2_ORIGINAL = (byte) 0x00;
    /**
     * 血氧SpO2 上传SpO2数据
     */
    public static final byte TYPE_SPO2 = (byte) 0x01;

    /*  Type     ：内容种类，用于识别不同的内容，一个模块里有多种内容。    end*/
}
