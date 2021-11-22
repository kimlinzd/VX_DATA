package com.lepu.serial.enums;

/**
 * 导联模式
 * 0x00 ：5线模式，0x01 ：3线模式
 */
public enum EcgLeadModeEnum {
    /**
     * 5线模式
     */
    LINE5((byte) 0x00),
    /**
     * 3线模式
     */
    LINE3((byte) 0x01);
    private final byte value;

    EcgLeadModeEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
