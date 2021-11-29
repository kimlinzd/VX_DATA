package com.lepu.serial.enums;

/**
 * 校准指令类型
 * 0x01 ：开始校准和校零
 * 0x02 ：校准
 * 0x03 ：检验主压力传感器
 * 0x04 ：检验福压力传感器
 * 0x05 ：停止校准
 */
public enum NibpCalibrationMode {
    /**
     * 开始校准和校零
     */
    START_CALIBRATION_AND_ZERO_CALIBRATION((byte) 0X01),
    /**
     * 校准
     */
    CALIBRATION((byte) 0X02),
    /**
     * 检验主压力传感器
     */
    CHECK_THE_MAIN_PRESSURE_SENSOR((byte) 0X03),
    /**
     * 检验福压力传感器
     */
    CHECK_THE_AUXILIARY_PRESSURE_SENSOR((byte) 0X04),
    /**
     * 停止校准
     */
    STOP_CALIBRATION((byte) 0X05);


    private final byte value;

    NibpCalibrationMode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
