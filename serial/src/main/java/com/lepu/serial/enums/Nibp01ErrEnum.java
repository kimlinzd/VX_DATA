package com.lepu.serial.enums;

/**
 * 血压一级错误
 */
public enum Nibp01ErrEnum {
    /**
     * NIBP正常
     */
    NORMAL(0),
    /**
     * NIBP模块故障
     */
    MODULE_FAILURE(1),
    /**
     * 袖带太松或未接
     */
    CUFF_IS_TOO_LOOSE_OR_DISCONNECTED(2),
    /**
     * 气路漏气
     */
    GAS_CIRCUIT_LEAK(3),
    /**
     * 气路堵塞
     */
    GAS_PATH_IS_BLOCKED(4),
    /**
     * 信号弱
     */
    SIGNAL_IS_WEAK(5),
    /**
     * 压力超范围
     */
    PRESSURE_OUT_OF_RANGE(6),
    /**
     * NIBP手臂运动
     * （兽用：NIBP肢体运动）
     */
    ARM_MOVEMENT(7),

    /**
     * NIBP过压保护
     */
    OVER_VOLTAGE_PROTECTION(8),

    /**
     * NIBP超时
     */
    TIMEOUT(9),

    /**
     * NIBP袖带与病人类型不匹配
     */
    CUFF_DOES_NOT_MATCH_THE_PATIENT_TYPE(10),

    /**
     * NIBP测量失败
     */
    MEASUREMENT_FAILED(11),

    /**
     * 未知
     */
    UN_KNOW(110),
    ;


    private final int value;


    Nibp01ErrEnum(int value) {
        this.value = value;

    }

    public int getValue() {
        return value;
    }

    public static Nibp01ErrEnum getNibpErrEnum(int value) {
        switch (value) {
            case 0: {
                return NORMAL;
            }
            case 1: {
                return MODULE_FAILURE;
            }
            case 2: {
                return CUFF_IS_TOO_LOOSE_OR_DISCONNECTED;
            }
            case 3: {
                return GAS_CIRCUIT_LEAK;
            }
            case 4: {
                return GAS_PATH_IS_BLOCKED;
            }
            case 5: {
                return SIGNAL_IS_WEAK;
            }
            case 6: {
                return PRESSURE_OUT_OF_RANGE;
            }
            case 7: {
                return ARM_MOVEMENT;
            }
            case 8: {
                return OVER_VOLTAGE_PROTECTION;
            }
            case 9: {
                return TIMEOUT;
            }
            case 10: {
                return CUFF_DOES_NOT_MATCH_THE_PATIENT_TYPE;
            }
            case 11: {
                return MEASUREMENT_FAILED;
            }

            default:
                return UN_KNOW;

        }
    }


}
