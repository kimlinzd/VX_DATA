package com.lepu.algorithm.Carewell.OmniEcg.jni;


import com.lepu.algorithm.ecg.entity.NotifyResampleBean;

/**
 * Created by wxd
 * @author wxd
 */

public class JniResample {

    private static JniResample instance = null;

    static {
        System.loadLibrary("ecg_resample");
    }

    private JniResample() {

    }

    public static JniResample getInstance() {
        if (instance == null) {
            instance = new JniResample();
        }
        return instance;
    }

    //==================================================sample
    /**
     *
     * @param ecgDataArray 毫伏数据
     * @param dataLen 数据长度
     * @param sample_src 原始采样率
     * @param sample_dest 目标采样率
     * @param notifyResampleBean 输出数据
     */
    public native void resample(float[] ecgDataArray, int dataLen, int sample_src, int sample_dest, NotifyResampleBean notifyResampleBean);

}
