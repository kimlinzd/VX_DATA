package com.lepu.algorithm.ecg.entity;

/**
 * Created by wxd on 2019-05-23.
 */

public class NotifyQrsDetectBean {

    private int[] qrsPosArray;
    private int qrsArrayLength;

    public int[] getQrsPosArray() {
        return qrsPosArray;
    }

    public void setQrsPosArray(int[] qrsPosArray) {
        this.qrsPosArray = qrsPosArray;
    }

    public int getQrsArrayLength() {
        return qrsArrayLength;
    }

    public void setQrsArrayLength(int qrsArrayLength) {
        this.qrsArrayLength = qrsArrayLength;
    }
}
