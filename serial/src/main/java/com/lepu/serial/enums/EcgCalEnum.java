package com.lepu.serial.enums;

/**
 * CAL输出
 * 0x00 ：关闭CAL输出       0x01 ：打开CAL输出
 */
public enum EcgCalEnum {
    /**
     * 关闭CAL输出
     */
    CALOPEN((byte)0x00),
    /**
     * 打开CAL输出
     */
    CALCLOSE((byte)0x01);


    private final byte value;

    EcgCalEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static void main(String[] args) {
        EcgCalEnum ecgCalEnum=EcgCalEnum.valueOf("CALOPEN");
        System.out.println("D");
    }
}
