package com.lepu.serial.enums;

/**
 * 当前通道数为1时有效,当前导联名称： I导联=0,II导联=1,III导联=2
 */
public enum EcgChn0IndexEnum {
    LEAD_I((byte) 0x00),
    LEAD_II((byte) 0x01),
    LEAD_III((byte) 0x02);
    private final byte value;

    EcgChn0IndexEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
