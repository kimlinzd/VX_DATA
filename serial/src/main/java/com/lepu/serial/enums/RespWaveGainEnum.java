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
    Gain_0d125((byte)0x00,0.125f),
    Gain_0d25((byte)0x01,  0.25f),
    Gain_0d5((byte)0x002,  0.5f),
    Gain_1((byte)0x03,  1f),
    Gain_2((byte)0x04,  2f),
    Gain_4((byte)0x05,  4f);

    private final byte value;
    private final float waveGain;
    RespWaveGainEnum(byte value, float waveGain) {
        this.value=value;
        this.waveGain = waveGain;
    }

    public byte getValue() {
        return value;
    }

    public static RespWaveGainEnum getRespWaveGainEnum(float waveGain){
        for (RespWaveGainEnum value : RespWaveGainEnum.values()) {
            if (value.waveGain==waveGain){
                return value;
            }
        }
        return RespWaveGainEnum.Gain_2;
    }

}
