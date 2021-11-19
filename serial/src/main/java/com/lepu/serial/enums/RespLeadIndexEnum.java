package com.lepu.serial.enums;

/**
 * 0x00 ：LA导联      0x01 ：LL导联
 */
public enum RespLeadIndexEnum {
    LA((byte)0x00),
    LL((byte)0x01);

    private final byte value;
    RespLeadIndexEnum(byte value) {
        this.value=value;
    }

    public byte getValue() {
        return value;
    }
}
