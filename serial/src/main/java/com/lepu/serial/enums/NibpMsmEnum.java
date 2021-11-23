package com.lepu.serial.enums;

/**
 * 血压测量模式 测量状态
 */
public enum NibpMsmEnum {
    /**
     * 空闲
     */
    IDLE(0),
    /**
     * 自检
     */
    SELF_CHECK(1),
    /**
     * 手动
     */
    MANUAL(2),
    /**
     * 连续
     */
    CONTINUOUS(3),
    /**
     * 自动周期
     */
    AUTOMATIC_CYCLE(4),
    /**
     * 辅助静脉穿刺
     */
    AUXILIARY_VENIPUNCTURE(5),
    /**
     * 压力检验模式1
     */
    PRESSURE_TEST_MODE_1(6),
    /**
     * 压力检验模式2
     */
    PRESSURE_TEST_MODE_2(7),
    /**
     * 漏气检测
     */
    AIR_LEAK_DETECTION(8),
    /**
     * 校准
     */
    CALIBRATION(9),
    /**
     * 未知
     */
    UN_KNOW(110);

    private final int value;


    NibpMsmEnum(int value) {
        this.value = value;
    }

    public static NibpMsmEnum getNibpMSM(int value) {
        switch (value) {
            case 0:
                return IDLE;
            case 1:
                return SELF_CHECK;
            case 2:
                return MANUAL;
            case 3:
                return CONTINUOUS;
            case 4:
                return AUTOMATIC_CYCLE;
            case 5:
                return AUXILIARY_VENIPUNCTURE;
            case 6:
                return PRESSURE_TEST_MODE_1;
            case 7:
                return PRESSURE_TEST_MODE_2;
            case 8:
                return AIR_LEAK_DETECTION;
            case 9:
                return CALIBRATION;
            default:
                return UN_KNOW;
        }

    }
}
