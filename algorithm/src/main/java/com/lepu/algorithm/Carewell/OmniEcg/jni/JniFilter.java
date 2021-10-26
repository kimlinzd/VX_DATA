package com.lepu.algorithm.Carewell.OmniEcg.jni;

//import com.ecg.entity.NotifyFilterBean;

import com.lepu.algorithm.ecg.entity.NotifyFilterBean;

/**
 * Created by wxd
 * @author wxd
 */

public class JniFilter {

    private static JniFilter instance = null;

    //1 :工频可以，震铃不可以;  0:工频不可以，震铃可以
    private int powerFrequency50attenuation = 1;
    private int powerFrequency60attenuation = 1;

    static {
        System.loadLibrary("ecg_filter");
    }

    private JniFilter() {

    }

    public static JniFilter getInstance() {
        if (instance == null) {
            instance = new JniFilter();
        }
        return instance;
    }

    //==================================================交流 工频

    /**
     * 工频不可以，震铃可以
     * @param ecgDataArray
     * @param dataLen
     * @param gainValue
     * @param clearStart
     * @param notifyFilterBean
     */
    public native void powerFrequency50(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    /**
     * 工频可以，震铃不可以
     * @param ecgDataArray
     * @param dataLen
     * @param gainValue
     * @param clearStart
     * @param notifyFilterBean
     */
    public native void powerFrequency50attenuation(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    public native void powerFrequency60(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    public native void powerFrequency60attenuation(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    //===========================肌电  这个滤波开了，就不调用低通了。肌电和低通是一种滤波   传的数据不能小于400.前后各200个点，中间取多少传多少
    public native void electromyography25(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    public native void electromyography35(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    public native void electromyography45(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    //===========================低通
    public native void lowPass75(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    public native void lowPass90(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    public native void lowPass100(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    public native void lowPass165(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    /*工频滤波 有点干扰问题。调用工频之前，先调用这个函数*/
    public native void LP295(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    //===========================高通
    public native void HP0p01(float[][] ecgDataArray, int dataLen, float gainValue, boolean clearStart, NotifyFilterBean notifyFilterBean);

    //起搏抹平数据
    public native void setPaceToZero(short[] ecgDataArray, int dataLen, int[] pacePosArray, int pacePosLen, NotifyFilterBean notifyFilterBean);

    public int getPowerFrequency50attenuation() {
        return powerFrequency50attenuation;
    }

    public void setPowerFrequency50attenuation(int powerFrequency50attenuation) {
        this.powerFrequency50attenuation = powerFrequency50attenuation;
    }

    public int getPowerFrequency60attenuation() {
        return powerFrequency60attenuation;
    }

    public void setPowerFrequency60attenuation(int powerFrequency60attenuation) {
        this.powerFrequency60attenuation = powerFrequency60attenuation;
    }
}
