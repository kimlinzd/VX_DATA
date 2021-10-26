package com.lepu.algorithm.Carewell.OmniEcg.jni;


import com.lepu.algorithm.ecg.entity.NotifyQrsDetectBean;

/**
 * Created by wxd
 * @author wxd
 */

public class JniQrsDetect {

    private static JniQrsDetect instance = null;

    static {
        System.loadLibrary("ecg_qrs_detect");
    }

    private JniQrsDetect() {

    }

    public static JniQrsDetect getInstance() {
        if (instance == null) {
            instance = new JniQrsDetect();
        }
        return instance;
    }

    //==================================================sample

    /**
     * 返回qrs的位置 rr分析使用传入
     * @param ecgDataArray 单导联数据，毫伏值
     * @param dataLen 数组长度
     * @param sample 采样率
     * @param notifyQrsDetectBean 输出数据
     */
    public native void qrsDetect(float[] ecgDataArray, int dataLen, int sample, NotifyQrsDetectBean notifyQrsDetectBean);

}
