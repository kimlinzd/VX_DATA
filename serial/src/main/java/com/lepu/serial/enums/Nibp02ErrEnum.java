package com.lepu.serial.enums;

/**
 * 血压二级错误
 */
public enum Nibp02ErrEnum {
    /**
     * 传感器或者A\D采样错误
     */
    SENSOR_OR_AD_SAMPLING_ERROR(Nibp01ErrEnum.MODULE_FAILURE, 1),
    /**
     * EEPROM错误
     */
    EEPROM_ERROR(Nibp01ErrEnum.MODULE_FAILURE, 2),
    /**
     * 未校准
     */
    NOT_CALIBRATED(Nibp01ErrEnum.MODULE_FAILURE, 3),
    /**
     * 自动校零失败
     */
    AUTO_ZERO_CALIBRATION_FAILED(Nibp01ErrEnum.MODULE_FAILURE, 4),

    /**
     * 气路系统漏气（漏气检测时报出）
     */
    AIR_SYSTEM_LEAKS(Nibp01ErrEnum.GAS_CIRCUIT_LEAK, 1),
    /**
     * 袖带或充气管漏气
     */
    LEAK_IN_CUFF_OR_INFLATABLE_TUBE(Nibp01ErrEnum.GAS_CIRCUIT_LEAK, 2),

    /**
     * 收缩压超上限
     */
    SYSTOLIC_BLOOD_PRESSURE_EXCEEDS_THE_UPPER_LIMIT(Nibp01ErrEnum.PRESSURE_OUT_OF_RANGE, 1),
    /**
     * 收缩压超下限
     */
    SYSTOLIC_BLOOD_PRESSURE_EXCEEDS_THE_LOWER_LIMIT(Nibp01ErrEnum.PRESSURE_OUT_OF_RANGE, 2),
    /**
     * 舒张压超上限
     */
    DIASTOLIC_BLOOD_PRESSURE_EXCEEDS_THE_UPPER_LIMIT(Nibp01ErrEnum.PRESSURE_OUT_OF_RANGE, 3),
    /**
     * 舒张压超下限
     */
    DIASTOLIC_BLOOD_PRESSURE_EXCEEDS_THE_LOWER_LIMIT(Nibp01ErrEnum.PRESSURE_OUT_OF_RANGE, 4),
    /**
     * 平均压超上限
     */
    THE_AVERAGE_PRESSURE_EXCEEDS_THE_UPPER_LIMIT(Nibp01ErrEnum.PRESSURE_OUT_OF_RANGE, 5),
    /**
     * 平均压超下限
     */
    THE_AVERAGE_PRESSURE_EXCEEDS_THE_LOWER_LIMIT(Nibp01ErrEnum.PRESSURE_OUT_OF_RANGE, 6),
    /**
     * 多参数超限
     */
    MULTI_PARAMETER_LIMIT_EXCEEDED(Nibp01ErrEnum.PRESSURE_OUT_OF_RANGE, 7),

    /**
     * 主MCU检测到过压
     */
    OVER_VOLTAGE_DETECTED_BY_THE_MAIN_MCU(Nibp01ErrEnum.OVER_VOLTAGE_PROTECTION, 1),

    /**
     * 副MCU检测到过压
     */
    OVER_VOLTAGE_DETECTED_BY_THE_SECONDARY_MCU(Nibp01ErrEnum.OVER_VOLTAGE_PROTECTION, 2),


    /**
     * 测量时间超限
     * （成人\儿童：不超过180秒
     * 新生儿：不超过90秒）
     */
    THE_MEASUREMENT_TIME_EXCEEDS_THE_LIMIT(Nibp01ErrEnum.TIMEOUT, 1),
    /**
     * 充气时间超限
     * （成人：50秒；儿童：35秒；
     * 新生儿：15秒）
     */
    INFLATION_TIME_EXCEEDED(Nibp01ErrEnum.TIMEOUT, 2),
    /**
     * 主MCU安全时间超限
     * （成人/儿童： 压力超过15mmHg的累积时间不超过180S
     * 新生儿：压力超过5mmHg的累积时间不超过90S）
     */
    THE_MAIN_MCU_SAFETY_TIME_EXCEEDS_THE_LIMIT(Nibp01ErrEnum.TIMEOUT, 3),
    /**
     * 副MCU安全时间超限
     * （成人/儿童： 压力超过15mmHg的累积时间不超过180S
     * 新生儿：压力超过5mmHg的累积时间不超过90S）
     */
    SECONDARY_MCU_SAFETY_TIME_LIMIT_EXCEEDED(Nibp01ErrEnum.TIMEOUT, 4),


    /**
     * 测量开始时，袖带压大于15mmHg，且5秒内没有降至15mmHg以下
     */
    CUFF_PRESSURE_ERROR(Nibp01ErrEnum.MEASUREMENT_FAILED, 1),
    /**
     * 提取血压参数失败或者不全等
     */
    FAILED_OR_INCOMPLETE_EXTRACTION_OF_BLOOD_PRESSURE_PARAMETERS(Nibp01ErrEnum.MEASUREMENT_FAILED, 2),
    /**
     * 其他
     */
    OTHER(Nibp01ErrEnum.MEASUREMENT_FAILED, 3),

    /**
     * 未知
     */
    UN_KNOW(Nibp01ErrEnum.UN_KNOW, 110),
    ;


    private final int value;
    private final Nibp01ErrEnum nibp01ErrEnum;

    Nibp02ErrEnum(Nibp01ErrEnum nibp01ErrEnum, int value) {
        this.value = value;
        this.nibp01ErrEnum = nibp01ErrEnum;
    }

    public static Nibp02ErrEnum getNibpErrEnum(Nibp01ErrEnum nibp01ErrEnum, int value) {
        switch (nibp01ErrEnum) {
            case MODULE_FAILURE: {
                switch (value) {
                    case 1: {
                        return SENSOR_OR_AD_SAMPLING_ERROR;
                    }
                    case 2: {
                        return EEPROM_ERROR;
                    }
                    case 3: {
                        return NOT_CALIBRATED;
                    }
                    case 4: {
                        return AUTO_ZERO_CALIBRATION_FAILED;
                    }
                }

            }
            case CUFF_IS_TOO_LOOSE_OR_DISCONNECTED: {
                return null;
            }
            case GAS_CIRCUIT_LEAK: {
                switch (value) {
                    case 1: {
                        return AIR_SYSTEM_LEAKS;
                    }
                    case 2: {
                        return LEAK_IN_CUFF_OR_INFLATABLE_TUBE;
                    }

                }

            }
            case GAS_PATH_IS_BLOCKED: {
                return null;
            }
            case SIGNAL_IS_WEAK: {
                return null;
            }
            case PRESSURE_OUT_OF_RANGE: {
                switch (value) {
                    case 1: {
                        return SYSTOLIC_BLOOD_PRESSURE_EXCEEDS_THE_UPPER_LIMIT;
                    }
                    case 2: {
                        return SYSTOLIC_BLOOD_PRESSURE_EXCEEDS_THE_LOWER_LIMIT;
                    }
                    case 3: {
                        return DIASTOLIC_BLOOD_PRESSURE_EXCEEDS_THE_UPPER_LIMIT;
                    }
                    case 4: {
                        return DIASTOLIC_BLOOD_PRESSURE_EXCEEDS_THE_LOWER_LIMIT;
                    }
                    case 5: {
                        return THE_AVERAGE_PRESSURE_EXCEEDS_THE_UPPER_LIMIT;
                    }
                    case 6: {
                        return THE_AVERAGE_PRESSURE_EXCEEDS_THE_LOWER_LIMIT;
                    }
                    case 7: {
                        return MULTI_PARAMETER_LIMIT_EXCEEDED;
                    }


                }


            }
            case ARM_MOVEMENT: {
                return null;
            }

            case OVER_VOLTAGE_PROTECTION: {
                switch (value) {
                    case 1: {
                        return OVER_VOLTAGE_DETECTED_BY_THE_MAIN_MCU;
                    }
                    case 2: {
                        return OVER_VOLTAGE_DETECTED_BY_THE_SECONDARY_MCU;
                    }

                }

            }
            case TIMEOUT: {
                switch (value) {
                    case 1: {
                        return THE_MEASUREMENT_TIME_EXCEEDS_THE_LIMIT;
                    }
                    case 2: {
                        return INFLATION_TIME_EXCEEDED;
                    }
                    case 3: {
                        return THE_MAIN_MCU_SAFETY_TIME_EXCEEDS_THE_LIMIT;
                    }
                    case 4: {
                        return SECONDARY_MCU_SAFETY_TIME_LIMIT_EXCEEDED;
                    }
                }

            }
            case CUFF_DOES_NOT_MATCH_THE_PATIENT_TYPE: {
                return null;
            }
            case MEASUREMENT_FAILED: {
                switch (value) {
                    case 1: {
                        return CUFF_PRESSURE_ERROR;
                    }
                    case 2: {
                        return FAILED_OR_INCOMPLETE_EXTRACTION_OF_BLOOD_PRESSURE_PARAMETERS;
                    }
                    case 3: {
                        return OTHER;
                    }

                }

            }
            default:
                return UN_KNOW;

        }
    }


}
