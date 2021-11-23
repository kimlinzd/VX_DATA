package com.lepu.serial.enums;

/**
 * 患者类型
 * 0x00 ：成人  0x01 ：儿童  0x02 ：新生儿    0x03 ：大动物   0x04 ：小动物
 */
public enum PatientTypeEnum {
    /**
     * 成人
     */
    ADULT(0),
    /**
     * 儿童
     */
    CHILD(1),
    /**
     * 新生儿
     */
    NEWBORN(2),
    /**
     * 大动物
     */
    BIG_ANIMAL(3),
    /**
     * 小动物
     */
    SMALL_ANIMAL(4),
    /**
     * 未知
     */
    UN_KNOW(110);

    private final int value;

    PatientTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;

    }


    public static PatientTypeEnum getPatientTypeEnum(int value) {
        switch (value) {
            case 0:
                return ADULT;
            case 1:
                return CHILD;
            case 2:
                return NEWBORN;
            case 3:
                return BIG_ANIMAL;
            case 4:
                return SMALL_ANIMAL;
            default:
                return UN_KNOW;

        }

    }
}
