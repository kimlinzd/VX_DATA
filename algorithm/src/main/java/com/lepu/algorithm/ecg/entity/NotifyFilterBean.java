package com.lepu.algorithm.ecg.entity;

/**
 * Created by wxd on 2019-05-24.
 */

public class NotifyFilterBean {

    //c120 滤波
    private boolean filterSuccess;//滤波成功，使用滤波的数据。否则使用原始的数据。目前 工频滤波使用。低通 肌电，不用这个返回值了
    private float[][] dataArray;//mv数据
    private int outDataLen;//输出数据长度

    //b120 滤波使用
    private short[][] shortDataArray;

    //
    private int[][] intDataArray;

    //起搏返回数据使用
    private short[] paceOutDataArray;

    public boolean isFilterSuccess() {
        return filterSuccess;
    }

    public void setFilterSuccess(boolean filterSuccess) {
        this.filterSuccess = filterSuccess;
    }

    public float[][] getDataArray() {
        return dataArray;
    }

    public void setDataArray(float[][] dataArray) {
        this.dataArray = dataArray;
    }

    public int getOutDataLen() {
        return outDataLen;
    }

    public void setOutDataLen(int outDataLen) {
        this.outDataLen = outDataLen;
    }

    public short[][] getShortDataArray() {
        return shortDataArray;
    }

    public void setShortDataArray(short[][] shortDataArray) {
        this.shortDataArray = shortDataArray;
    }

    public short[] getPaceOutDataArray() {
        return paceOutDataArray;
    }

    public void setPaceOutDataArray(short[] paceOutDataArray) {
        this.paceOutDataArray = paceOutDataArray;
    }

    public int[][] getIntDataArray() {
        return intDataArray;
    }

    public void setIntDataArray(int[][] intDataArray) {
        this.intDataArray = intDataArray;
    }
}
