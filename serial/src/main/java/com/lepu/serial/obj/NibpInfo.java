package com.lepu.serial.obj;

import com.lepu.serial.enums.NibpMsmEnum;

/**
 * NIBP信息
 */
public class NibpInfo {
    /**
     * NIBP状态
     */
    NibpMsmEnum nibpMsmEnum=NibpMsmEnum.IDLE;

    /**
     * Nibp 上一次测量时间
     */
    Long lastMeasureTime=0L;

    public NibpMsmEnum getNibpMsmEnum() {
        return nibpMsmEnum;
    }

    public void setNibpMsmEnum(NibpMsmEnum nibpMsmEnum) {
        this.nibpMsmEnum = nibpMsmEnum;
    }

    public Long getLastMeasureTime() {
        return lastMeasureTime;
    }

    public void setLastMeasureTime(Long lastMeasureTime) {
        this.lastMeasureTime = lastMeasureTime;
    }
}
