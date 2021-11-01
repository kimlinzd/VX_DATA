package com.lepu.serial.constant;

/**
 * 串口数据 文件的静态变量
 */
public class SerialDataFlieContent {
    public static final int PER_SCREEN_DATA_SECOND = 10;
    public static final int CACHE_DATA_SECOND = 6;//内存中缓存的数据 13秒
    public static final int CACHE_DATA_SECOND_DATA = CACHE_DATA_SECOND * PER_SCREEN_DATA_SECOND;//内存中缓存的数据 s   13*10
    //ECG缓存文件夹
    public static String ECG_PATH = "ECG_CACHE/";

}
