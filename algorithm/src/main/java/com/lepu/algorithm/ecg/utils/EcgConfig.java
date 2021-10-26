package com.lepu.algorithm.ecg.utils;

import android.graphics.Color;


/**
 * Created by wuxd
 */
public class EcgConfig {
    public static final int BIG_GRID_COUNT_W = 51;//横向网格数量 10s数据50个网格，定标符号1个大网格=51
    public static final int BIG_GRID_COUNT_H = 30;  //纵向大网格数量 30.以前使用 高度网格计算
    public static final int SMALL_GRID_COUNT = 5;  //小网格数量 5

    public static int screenBgColor;
    public static int screenGridThinColor;
    public static int screenGridWideColor;
    public static int screenWaveColor;

    public static int pdfBgColor;
    public static int pdfGridThinColor;
    public static int pdfGridWideColor;
    public static int pdfWaveColor;

    public static int fontColorStandard;
    public static int fontColorHighlight;

    //public static float IN_CM = 2.54f;
    //public static int SPEED = Const.SAMPLE_RATE;      //每秒1000个点
    public static boolean LARGE_GRID_DIVIDER = false;//是否画 5个大格分割线

    public static final float RATE_MIN = 1.0f / 20;

    static {
        //屏幕画图
        screenBgColor = Color.BLACK;
        screenGridThinColor = Color.parseColor("#0a2f3a");
        screenGridWideColor = Color.parseColor("#126278");
        screenWaveColor = Color.parseColor("#00FF00");

        //pdf报告画图
        pdfBgColor = Color.WHITE;
        pdfGridThinColor = Color.parseColor("#40FF7F50");
        pdfGridWideColor = Color.parseColor("#FFFF7F50");
        pdfWaveColor = Color.BLACK;

        //导联名称颜色
        fontColorStandard = Color.WHITE;
        //fontColorHighlight = Color.parseColor("#00FF00");
        fontColorHighlight = Color.YELLOW;
    }
}