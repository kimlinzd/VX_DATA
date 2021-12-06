package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.enums.NibpMsmEnum;
import com.lepu.serial.enums.NipbpWmEnum;
import com.lepu.serial.enums.PatientTypeEnum;
import com.lepu.serial.uitl.ByteUtils;

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
        short db12 = (short) ByteUtils.bytes2Short(buf[1], buf[0]);
        PatientTypeEnum patientTypeEnum = PatientTypeEnum.getPatientTypeEnum(db12 >> 13);

        int sp = (int) (db12 >> 4 & (0xFF << 4));
        nibpMsmEnum = NibpMsmEnum.getNibpMSM(db12 >> 0 & (0xFF >> 12));
        short db32 = (short) ByteUtils.bytes2Short(buf[3], buf[2]);
        nipbpWmEnum = NipbpWmEnum.getNipbpWmEnum((db32 >> 13));


       /* patientTypeEnum = PatientTypeEnum.getPatientTypeEnum(buf[0] >> 5);
        sp = ((buf[0]) >> 0 & (0xFF >> 3)) + ((buf[1] & 0xf0) >> 4);
        nibpMsmEnum = NibpMsmEnum.getNibpMSM(buf[1] & 0x0f);
        nipbpWmEnum = NipbpWmEnum.getNipbpWmEnum(((buf[2]) >> 5 & (0xFF >> 3)));*/

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
        return (NibpWorkingStatus) super.clone();
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[]{(byte) 10, (byte) 0, (byte) 32, (byte) 0};
        short db12 = (short) ByteUtils.bytes2Short(bytes[1], bytes[0]);
        PatientTypeEnum patientTypeEnum = PatientTypeEnum.getPatientTypeEnum(db12 >> 13);

        int sp = (int) (db12 >> 4 & (0xFF << 4));
        NibpMsmEnum nibpMsmEnum = NibpMsmEnum.getNibpMSM(db12 >> 0 & (0xFF >> 12));
        short db32 = (short) ByteUtils.bytes2Short(bytes[3], bytes[2]);
        NipbpWmEnum nipbpWmEnum = NipbpWmEnum.getNipbpWmEnum((db32 >> 13));

        System.out.println("sp==" + sp);

    }

    //获取多个连续的bit值： b为传入的字节，start是起始位，length是长度，如要获取bit0-bit4的值，则start为0，length为5
    public static int getBits(byte b, int start, int length) {
        int bit = (int) ((b >> start) & (0xFF >> (8 - length)));
        return bit;
    }
}
