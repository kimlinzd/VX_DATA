package com.lepu.algorithm.ecg.entity;

/**
 * Created by wxd on 2019-05-24.
 */

public class NotifyResampleBean {

    private float[] resampleDataArray;
    private int dataLen;

    public float[] getResampleDataArray() {
        return resampleDataArray;
    }

    public void setResampleDataArray(float[] resampleDataArray) {
        this.resampleDataArray = resampleDataArray;
    }

    public int getDataLen() {
        return dataLen;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }


}
