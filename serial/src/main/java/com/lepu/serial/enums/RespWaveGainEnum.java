package com.lepu.serial.enums;

/**
 * 呼吸波形显示增益
 *0x00: Gain=0.125;
 * 0x01:Gain=0.25;
 * 0x02: Gain=0.5;
 * 0x03:Gain=1;
 * 0x04: Gain=2;5:
 * Gain=4;
 */
public enum RespWaveGainEnum {
    Gain_0d125((byte)0x00),
    Gain_0d25((byte)0x01),
    Gain_0d5((byte)0x002),
    Gain_1((byte)0x03),
    Gain_2d5((byte)0x04),
    Gain_4((byte)0x05);

    private final byte value;
    RespWaveGainEnum(byte value) {
        this.value=value;
    }

    public byte getValue() {
        return value;
    }
}
