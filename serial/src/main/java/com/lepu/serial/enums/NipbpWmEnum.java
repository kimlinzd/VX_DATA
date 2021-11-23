package com.lepu.serial.enums;

/**
 * 波形传输模式
 */
public enum NipbpWmEnum {
    /**
     * 禁止波形传输
     */
    PROHIBIT_WAVE_TRANSMISSION(1),
    /**
     * 5HZ
     */
    HZ_5(2),
    /**
     * 200HZ
     */
    HZ_200(3),
    /**
     * 5HZ AND 200HZ
     */
    HZ_5_AND_HZ_200(4),
    /**
     * 未知
     */
    UN_KNOW(110);

    private final int value;


    NipbpWmEnum(int value) {
        this.value = value;

    }

    public int getValue() {
        return value;
    }

    public static NipbpWmEnum getNipbpWmEnum(int value) {
        switch (value) {
            case 1:
                return PROHIBIT_WAVE_TRANSMISSION;
            case 2:
                return HZ_5;
            case 3:
                return HZ_200;
            case 4:
                return HZ_5_AND_HZ_200;
            default:
                return UN_KNOW;

        }
    }

}
