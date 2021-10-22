package com.lepu.algorithm.ecg.entity;

/**
 * @author wxd
 */
public class NotifyEcgDataBean {
    private short[] leadNameIndex;
    private short leadOffFlag;
    private int leadEcgLen;
    private  int leadNum;
    private short pacePosition[];//这个有效
    private short pacePositionH[];//这个无效
    private short[][] leadEcgBuf;
    private int heartRate;
    private short[]  qrsPos;
    private short qrsPosLen;
    private short leftlen;

    public short[] getLeadNameIndex() {
        return leadNameIndex;
    }

    public void setLeadNameIndex(short[] leadNameIndex) {
        this.leadNameIndex = leadNameIndex;
    }

    public short getLeadOffFlag() {
        return leadOffFlag;
    }

    public void setLeadOffFlag(short leadOffFlag) {
        this.leadOffFlag = leadOffFlag;
    }

    public int getLeadEcgLen() {
        return leadEcgLen;
    }

    public void setLeadEcgLen(int leadEcgLen) {
        this.leadEcgLen = leadEcgLen;
    }

    public int getLeadNum() {
        return leadNum;
    }

    public void setLeadNum(int leadNum) {
        this.leadNum = leadNum;
    }

    public short[][] getLeadEcgBuf() {
        return leadEcgBuf;
    }

    public void setLeadEcgBuf(short[][] leadEcgBuf) {
        this.leadEcgBuf = leadEcgBuf;
    }

    public short[] getQrsPos() {
        return qrsPos;
    }

    public void setQrsPos(short[] qrsPos) {
        this.qrsPos = qrsPos;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public short getQrsPosLen() {
        return qrsPosLen;
    }

    public void setQrsPosLen(short qrsPosLen) {
        this.qrsPosLen = qrsPosLen;
    }

    public short getLeftlen() {
        return leftlen;
    }

    public void setLeftlen(short leftlen) {
        this.leftlen = leftlen;
    }

    public short[] getPacePosition() {
        return pacePosition;
    }

    public void setPacePosition(short[] pacePosition) {
        this.pacePosition = pacePosition;
    }

    public short[] getPacePositionH() {
        return pacePositionH;
    }

    public void setPacePositionH(short[] pacePositionH) {
        this.pacePositionH = pacePositionH;
    }
}
