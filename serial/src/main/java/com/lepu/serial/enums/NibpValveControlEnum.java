package com.lepu.serial.enums;

/**
 * 血压模块 控制阀 快阀 慢阀开关
 * 0x01：打开
 * 0x02：关闭
 */
public enum NibpValveControlEnum {
    VALVE_OPEN((byte) 1),
    VALVE_CLOSE((byte) 2);

    private final byte value;

    NibpValveControlEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
