package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.enums.Nibp01ErrEnum;
import com.lepu.serial.enums.Nibp02ErrEnum;
import com.lepu.serial.enums.NibpMsmEnum;
import com.lepu.serial.enums.PatientTypeEnum;
import com.lepu.serial.uitl.ByteUtils;

import java.io.Serializable;

/**
 * 血压参数和模块状态
 * 血压参数和模块状态包，上电主动上传一次，每次测量结束主动上传一次，上位机读取主动上传一次。
 */
public class NibpPramAndStatus implements Serializable {
    /**
     * 测量模式
     */
    NibpMsmEnum msm;
    /**
     * 病人类型
     */
    PatientTypeEnum patientTypeEnum;

    /**
     * 返回结果标志位 （仅手动测量和连续测量模式有效）
     * 0 ：测量开始后第一次返回结果
     * 1 ：非第一次返回结果
     */
    int fg;

    /**
     * 血压一级错误
     */
    Nibp01ErrEnum nibp01ErrEnum;
    /**
     * 血压二级错误
     */
    Nibp02ErrEnum nibp02ErrEnum;
    /**
     * 可靠性指示
     */
    int tip;
    /**
     * 收缩压
     */
    int sys;
    /**
     * 舒张压
     */
    int dia;
    /**
     * 平均压
     */
    int map;
    /**
     * 脉率
     */
    int PR;

    public NibpPramAndStatus() {
    }

    public NibpPramAndStatus(byte[] buf) {
        msm = NibpMsmEnum.getNibpMSM((buf[0] & 0x0f) >> 4);
        patientTypeEnum = PatientTypeEnum.getPatientTypeEnum(buf[0] >> 1 & 4);
        fg = buf[0] >> 0 & 0x1;
        nibp01ErrEnum = Nibp01ErrEnum.getNibpErrEnum(buf[1]);
        nibp02ErrEnum = Nibp02ErrEnum.getNibpErrEnum(nibp01ErrEnum, buf[2]);
        tip = buf[3];
        sys= ByteUtils.bytes2Short(buf[4],buf[5]);
        dia= ByteUtils.bytes2Short(buf[6],buf[7]);
        map= ByteUtils.bytes2Short(buf[8],buf[9]);
        PR= ByteUtils.bytes2Short(buf[10],buf[11]);

    }

    public NibpMsmEnum getMsm() {
        return msm;
    }

    public void setMsm(NibpMsmEnum msm) {
        this.msm = msm;
    }

    public PatientTypeEnum getPatientTypeEnum() {
        return patientTypeEnum;
    }

    public void setPatientTypeEnum(PatientTypeEnum patientTypeEnum) {
        this.patientTypeEnum = patientTypeEnum;
    }

    public int getFg() {
        return fg;
    }

    public void setFg(int fg) {
        this.fg = fg;
    }

    public Nibp01ErrEnum getNibp01ErrEnum() {
        return nibp01ErrEnum;
    }

    public void setNibp01ErrEnum(Nibp01ErrEnum nibp01ErrEnum) {
        this.nibp01ErrEnum = nibp01ErrEnum;
    }

    public Nibp02ErrEnum getNibp02ErrEnum() {
        return nibp02ErrEnum;
    }

    public void setNibp02ErrEnum(Nibp02ErrEnum nibp02ErrEnum) {
        this.nibp02ErrEnum = nibp02ErrEnum;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public int getSys() {
        return sys;
    }

    public void setSys(int sys) {
        this.sys = sys;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public int getPR() {
        return PR;
    }

    public void setPR(int PR) {
        this.PR = PR;
    }

    @NonNull
    @Override
    protected NibpPramAndStatus clone() throws CloneNotSupportedException {
        return (NibpPramAndStatus)super.clone();
    }
}
