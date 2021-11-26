package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.enums.NibpMsmEnum;
import com.lepu.serial.enums.NipbpWmEnum;
import com.lepu.serial.enums.PatientTypeEnum;

import java.io.Serializable;

/**
 * 血压模块工作状态
 */
public class NibpWorkingStatus implements Serializable , Cloneable {
    /**
     * 病人类型
     */
    PatientTypeEnum patientTypeEnum;

    /**
     * 初始充气压（单位：mmHg）
     */
    int sp;
    /**
     * 测量状态
     */
    NibpMsmEnum nibpMsmEnum;

    /**
     * 波形传输模式
     */
    NipbpWmEnum nipbpWmEnum;

    public NibpWorkingStatus() {
    }

    public NibpWorkingStatus(byte[] buf) {
        patientTypeEnum = PatientTypeEnum.getPatientTypeEnum(buf[0] >> 5);
        sp = ((buf[0]) >> 0 & (0xFF >> 3)) + ((buf[1] & 0xf0) >> 4);
        nibpMsmEnum = NibpMsmEnum.getNibpMSM(buf[1] & 0x0f);
        nipbpWmEnum = NipbpWmEnum.getNipbpWmEnum(((buf[2]) >> 0 & (0xFF >> 3)));

    }


    public PatientTypeEnum getPatientTypeEnum() {
        return patientTypeEnum;
    }

    public void setPatientTypeEnum(PatientTypeEnum patientTypeEnum) {
        this.patientTypeEnum = patientTypeEnum;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public NibpMsmEnum getNibpMsmEnum() {
        return nibpMsmEnum;
    }

    public void setNibpMsmEnum(NibpMsmEnum nibpMsmEnum) {
        this.nibpMsmEnum = nibpMsmEnum;
    }

    public NipbpWmEnum getNipbpWmEnum() {
        return nipbpWmEnum;
    }

    public void setNipbpWmEnum(NipbpWmEnum nipbpWmEnum) {
        this.nipbpWmEnum = nipbpWmEnum;
    }

    @NonNull
    @Override
    protected NibpWorkingStatus clone() throws CloneNotSupportedException {
        return (NibpWorkingStatus)super.clone();
    }
}
