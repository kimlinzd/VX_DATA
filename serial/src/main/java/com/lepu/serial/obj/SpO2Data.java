package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * SpO2数据
 */
public class SpO2Data implements Serializable, Cloneable {


    /**
     * Bit0
     * SpO2传感器脱落标记
     * 0 正常
     * 1 SpO2传感器脱落
     */
    int SPO2_SENSOR_DROP_MARK;
    /**
     * Bit1
     * SpO2传感器未连接标记
     * 0 正常
     * 1 SpO2传感器未连接
     */
    int SPO2_SENSOR_NOT_CONNECTED_MARK;
    /**
     * Bit2：  SpO2环境光太强标记
     * 0 正常
     * 1 SpO2环境光太强
     */
    int SPO2_AMBIENT_LIGHT_IS_TOO_STRONG_MARK;
    /**
     * SpO2无搏动标记
     * 0 正常
     * 1 SpO2无搏动
     */
    int SPO2_NO_PULSATION_MARK;
    /**
     * Bit4：  SpO2传感器不兼容标记
     * 0 正常
     * 1 SpO2传感器不兼容
     */
    int SPO2_SENSOR_INCOMPATIBLE_MARK;

    /**
     * Bit5：  SpO2信号太差标记
     * 0 正常
     * 1 SpO2信号太差
     */
    int BAD_SPO2_SIGNAL_MARK;

    /**
     * Bit6：  SpO2干扰标记
     * 0 正常
     * 1 SpO2干扰
     */
    int SPO2_INTERFERENCE_FLAG;
    /**
     * Bit7：  SpO2传感器故障标记
     * 0 正常
     * 1 SpO2传感器故障
     */
    int SPO2_SENSOR_FAILURE_FLAG;

    /**
     * Bit8：  SpO2搜索脉搏标记
     * 0 正常
     * 1 SpO2搜索脉搏
     */
    int SPO2_SEARCH_PULSE_MARKER;
    /**
     * Bit9：  SpO2弱灌注标记
     * 0 正常
     * 1 SpO2弱灌注
     */
    int SPO2_WEAK_PERFUSION_MARKER;
    int status1;//二级级错误码  这里就不分析了 对前端没用 分析一级错误码显示给用户即可
    int spO2;//血氧饱和度
    int PR;//脉率数据
    int PI;//灌注度数据

    byte[] originalData;//原始数据 用于保存

    public SpO2Data() {
    }

    public SpO2Data(byte[] buf) {
        short err0 = (short) ByteUtils.bytes2Short(buf[0], buf[1]);
        SPO2_SENSOR_DROP_MARK = err0 >> 0 & 0x1;
        SPO2_SENSOR_NOT_CONNECTED_MARK = err0 >> 1 & 0x1;
        SPO2_AMBIENT_LIGHT_IS_TOO_STRONG_MARK = err0 >> 2 & 0x1;
        SPO2_NO_PULSATION_MARK = err0 >> 3 & 0x1;
        SPO2_SENSOR_INCOMPATIBLE_MARK = err0 >> 4 & 0x1;
        BAD_SPO2_SIGNAL_MARK = err0 >> 5 & 0x1;
        SPO2_INTERFERENCE_FLAG = err0 >> 6 & 0x1;
        SPO2_SENSOR_FAILURE_FLAG = err0 >> 7 & 0x1;
        SPO2_SEARCH_PULSE_MARKER = err0 >> 8 & 0x1;
        SPO2_WEAK_PERFUSION_MARKER = err0 >> 9 & 0x1;

        status1 = ByteUtils.bytes2Short(buf[2], buf[3]);
        spO2 = buf[4];
        PR = ByteUtils.bytes2Short(buf[5], buf[6]);
        PI = ByteUtils.bytes2Short(buf[7], buf[8]);

        originalData=buf;
    }


    public int getSPO2_SENSOR_DROP_MARK() {
        return SPO2_SENSOR_DROP_MARK;
    }

    public void setSPO2_SENSOR_DROP_MARK(int SPO2_SENSOR_DROP_MARK) {
        this.SPO2_SENSOR_DROP_MARK = SPO2_SENSOR_DROP_MARK;
    }

    public int getSPO2_SENSOR_NOT_CONNECTED_MARK() {
        return SPO2_SENSOR_NOT_CONNECTED_MARK;
    }

    public void setSPO2_SENSOR_NOT_CONNECTED_MARK(int SPO2_SENSOR_NOT_CONNECTED_MARK) {
        this.SPO2_SENSOR_NOT_CONNECTED_MARK = SPO2_SENSOR_NOT_CONNECTED_MARK;
    }

    public int getSPO2_AMBIENT_LIGHT_IS_TOO_STRONG_MARK() {
        return SPO2_AMBIENT_LIGHT_IS_TOO_STRONG_MARK;
    }

    public void setSPO2_AMBIENT_LIGHT_IS_TOO_STRONG_MARK(int SPO2_AMBIENT_LIGHT_IS_TOO_STRONG_MARK) {
        this.SPO2_AMBIENT_LIGHT_IS_TOO_STRONG_MARK = SPO2_AMBIENT_LIGHT_IS_TOO_STRONG_MARK;
    }

    public int getSPO2_NO_PULSATION_MARK() {
        return SPO2_NO_PULSATION_MARK;
    }

    public void setSPO2_NO_PULSATION_MARK(int SPO2_NO_PULSATION_MARK) {
        this.SPO2_NO_PULSATION_MARK = SPO2_NO_PULSATION_MARK;
    }

    public int getSPO2_SENSOR_INCOMPATIBLE_MARK() {
        return SPO2_SENSOR_INCOMPATIBLE_MARK;
    }

    public void setSPO2_SENSOR_INCOMPATIBLE_MARK(int SPO2_SENSOR_INCOMPATIBLE_MARK) {
        this.SPO2_SENSOR_INCOMPATIBLE_MARK = SPO2_SENSOR_INCOMPATIBLE_MARK;
    }

    public int getBAD_SPO2_SIGNAL_MARK() {
        return BAD_SPO2_SIGNAL_MARK;
    }

    public void setBAD_SPO2_SIGNAL_MARK(int BAD_SPO2_SIGNAL_MARK) {
        this.BAD_SPO2_SIGNAL_MARK = BAD_SPO2_SIGNAL_MARK;
    }

    public int getSPO2_INTERFERENCE_FLAG() {
        return SPO2_INTERFERENCE_FLAG;
    }

    public void setSPO2_INTERFERENCE_FLAG(int SPO2_INTERFERENCE_FLAG) {
        this.SPO2_INTERFERENCE_FLAG = SPO2_INTERFERENCE_FLAG;
    }

    public int getSPO2_SENSOR_FAILURE_FLAG() {
        return SPO2_SENSOR_FAILURE_FLAG;
    }

    public void setSPO2_SENSOR_FAILURE_FLAG(int SPO2_SENSOR_FAILURE_FLAG) {
        this.SPO2_SENSOR_FAILURE_FLAG = SPO2_SENSOR_FAILURE_FLAG;
    }

    public int getSPO2_SEARCH_PULSE_MARKER() {
        return SPO2_SEARCH_PULSE_MARKER;
    }

    public void setSPO2_SEARCH_PULSE_MARKER(int SPO2_SEARCH_PULSE_MARKER) {
        this.SPO2_SEARCH_PULSE_MARKER = SPO2_SEARCH_PULSE_MARKER;
    }

    public int getSPO2_WEAK_PERFUSION_MARKER() {
        return SPO2_WEAK_PERFUSION_MARKER;
    }

    public void setSPO2_WEAK_PERFUSION_MARKER(int SPO2_WEAK_PERFUSION_MARKER) {
        this.SPO2_WEAK_PERFUSION_MARKER = SPO2_WEAK_PERFUSION_MARKER;
    }

    public int getStatus1() {
        return status1;
    }

    public void setStatus1(int status1) {
        this.status1 = status1;
    }

    public int getSpO2() {
        return spO2;
    }

    public void setSpO2(int spO2) {
        this.spO2 = spO2;
    }

    public int getPR() {
        return PR;
    }

    public void setPR(int PR) {
        this.PR = PR;
    }

    public int getPI() {
        return PI;
    }

    public void setPI(int PI) {
        this.PI = PI;
    }

    public byte[] getOriginalData() {
        return originalData;
    }

    public void setOriginalData(byte[] originalData) {
        this.originalData = originalData;
    }

    @NonNull
    @Override
    protected SpO2Data clone() throws CloneNotSupportedException {
        return (SpO2Data)super.clone();
    }
}
