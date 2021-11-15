package com.lepu.serial.constant;

/**
 * 串口数据保存 文件的静态变量
 */
public class SaveDataFlieContent {
    //ECG缓存文件夹
    public static final String ECG_PATH = "ECG_CACHE/";
    /**
     * 保留空间大小 GB
     */
    public static final double RESERVED_SPACE = 0.5;
    /**
     * 储存的患者唯一ID 唯一标识
     */
    public static String PATITENT_ID = "";
    /**
     * ECG数据最多储存多少个小时
     */
    public static final int ECG_DATA_SAVE_MAX_TIME=140;
    /**
     * ECG数据单个文件最多储存多少个小时
     */
    public static final int ECG_DATA_SINGLE_FILE_SAVE_MAX_TIME=4;

}
