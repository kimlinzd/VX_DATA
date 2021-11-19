package com.lepu.serial.enums;

/**
 * CAL输出
 * 0x00 ：关闭CAL输出       0x01 ：打开CAL输出
 */
public enum Ecg1mVEnum {
    /**
     * 关闭CAL输出
     */
    CALOPEN((byte)0x00),
    /**
     * 打开CAL输出
     */
    CALCLOSE((byte)0x01);


    private final byte value;

    Ecg1mVEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
