package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.enums.Nibp01ErrEnum;
import com.lepu.serial.enums.Nibp02ErrEnum;
import com.lepu.serial.enums.NibpMsmEnum;
import com.lepu.serial.enums.PatientTypeEnum;

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
     * 收缩压 h
     */
    int sys_h;

    /**
     * 收缩压 l
     */
    int sys_l;

    /**
     * 舒张压 h
     */
    int dia_h;
    /**
     * 舒张压 l
     */
    int dia_l;
    /**
     * 平均压 h
     */
    int map_h;
    /**
     * 平均压 l
     */
    int map_l;
    /**
     * 脉率 h
     */
    int PR_h;
    /**
     * 脉率 l
     */
    int PR_l;

    public NibpPramAndStatus() {
    }

    public NibpPramAndStatus(byte[] buf) {
        msm = NibpMsmEnum.getNibpMSM((buf[0] & 0x0f) >> 4);
        patientTypeEnum = PatientTypeEnum.getPatientTypeEnum(buf[0] >> 1 & 4);
        fg = buf[0] >> 0 & 0x1;
        nibp01ErrEnum = Nibp01ErrEnum.getNibpErrEnum(buf[1]);
        nibp02ErrEnum = Nibp02ErrEnum.getNibpErrEnum(nibp01ErrEnum, buf[2]);
        tip = buf[3];
        sys_h = buf[4];
        sys_l = buf[5];
        dia_h = buf[6];
        dia_l = buf[7];
        map_h = buf[8];
        map_l = buf[9];
        PR_h = buf[10];
        PR_l = buf[11];
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

    public int getSys_h() {
        return sys_h;
    }

    public void setSys_h(int sys_h) {
        this.sys_h = sys_h;
    }

    public int getSys_l() {
        return sys_l;
    }

    public void setSys_l(int sys_l) {
        this.sys_l = sys_l;
    }

    public int getDia_h() {
        return dia_h;
    }

    public void setDia_h(int dia_h) {
        this.dia_h = dia_h;
    }

    public int getDia_l() {
        return dia_l;
    }

    public void setDia_l(int dia_l) {
        this.dia_l = dia_l;
    }

    public int getMap_h() {
        return map_h;
    }

    public void setMap_h(int map_h) {
        this.map_h = map_h;
    }

    public int getMap_l() {
        return map_l;
    }

    public void setMap_l(int map_l) {
        this.map_l = map_l;
    }

    public int getPR_h() {
        return PR_h;
    }

    public void setPR_h(int PR_h) {
        this.PR_h = PR_h;
    }

    public int getPR_l() {
        return PR_l;
    }

    public void setPR_l(int PR_l) {
        this.PR_l = PR_l;
    }

    @NonNull
    @Override
    protected NibpPramAndStatus clone() throws CloneNotSupportedException {
        return (NibpPramAndStatus)super.clone();
    }
}
