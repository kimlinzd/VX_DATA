package com.lepu.serial.enums;

/**
 * 患者类型
 * 0x00 ：成人  0x01 ：儿童  0x02 ：新生儿    0x03 ：大动物   0x04 ：小动物
 */
public enum PatientTypeEnum {
    /**
     * 成人
     */
    ALDULT((byte) 0x00),
    /**
     * 儿童
     */
    CHILD((byte) 0x01),
    /**
     * 新生儿
     */
    NEWBORN((byte) 0x02),
    /**
     * 大动物
     */
    BIGANIMAL((byte) 0x03),
    /**
     * 小动物
     */
    SMALLANIMAL((byte) 0x04);

    private final byte value;

    PatientTypeEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;

    }
}
