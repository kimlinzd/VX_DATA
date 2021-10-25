package com.lepu.algorithm.Carewell.OmniEcg.jni;


import com.lepu.algorithm.ecg.entity.AiResultBean;
import com.lepu.algorithm.ecg.entity.PatientInfoBean;

/**
 * Created by wxd
 * @author wxd
 */

public class JniTraditionalAnalysis {

    private static JniTraditionalAnalysis instance = null;

    static {
        System.loadLibrary("ecg_traditional_analysis");
    }

    private JniTraditionalAnalysis() {

    }

    public static JniTraditionalAnalysis getInstance() {
        if (instance == null) {
            instance = new JniTraditionalAnalysis();
        }
        return instance;
    }

    /**
     * 读取config
     * @param configContent
     */
    public native void readEcgConfig(String configContent);

    /**
     * 传统算法分析
     * @param ecgMvDataArray
     * @param dataLen
     * @param aiResultBean
     * @param archivesBean
     * @param ifSaveAnalysisResult 1保存xml 结果
     * @param analysisResultPath 保存结果的path
     * @param leadMode 数据导联模式
     * @param diagnosisMode 诊断模式
     */
    public native void traditionalAnalysis(float[][] ecgMvDataArray, int dataLen, AiResultBean aiResultBean, PatientInfoBean archivesBean,
                                           int ifSaveAnalysisResult, String analysisResultPath,
                                           int leadMode, int diagnosisMode);

    /**
     * 实时预警采样初始化
     * @param sampleRate 1000
     * @param seedDataTime  1s
     */
    public native void realtimeWarnInit(short sampleRate,short seedDataTime);

    /**
     * 实时分析
     * @param dataArray
     */
    public native void realtimeAnalysis(short[] dataArray);

    /**
     * 获取预警code
     * 20:正在学习；22：正常心电；非20,22：异常心电
     * @return
     */
    public native short realtimeGetWarnCode();

}
