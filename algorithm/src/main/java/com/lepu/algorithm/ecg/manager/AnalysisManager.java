package com.lepu.algorithm.ecg.manager;

import android.content.Context;
import android.text.TextUtils;

import com.lepu.algorithm.ecg.entity.ConfigBean;
import com.lepu.algorithm.ecg.entity.MeasureResultBean;
import com.lepu.algorithm.ecg.entity.PatientInfoBean;
import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;
import com.lepu.algorithm.ecg.entity.dictionary.PatientSettingConfigEnum;
import com.lepu.algorithm.ecg.utils.SerializableUtils;
import com.lepu.algorithm.ecg.utils.WaveEncodeUtil;

import java.util.List;

public class AnalysisManager {

    private AnalysisManager(){}

    /**
     * 分析 后续会有云诊断
     *
     * @param context
     * @param leadWorkModeType
     * @param leadType
     * @param patientInfoBean
     * @param ecgDataArrayPart
     * @param preview
     * @param ecgDataFilePath
     * @param testMode         正常传true；后门批量分析，传false
     * @return
     */
    public MeasureResultBean analysis(Context context, EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType, EcgSettingConfigEnum.LeadType leadType, PatientInfoBean patientInfoBean,
                                      short[][] ecgDataArrayPart, boolean preview, String ecgDataFilePath, boolean testMode) {
        return traditionalAnalysis(context, leadWorkModeType, leadType, patientInfoBean, ecgDataArrayPart, preview, ecgDataFilePath, testMode);
    }

    /**
     * 传统分析
     *
     * @param context
     * @param leadWorkModeType
     * @param leadType
     * @param patientInfoBeanSrc
     * @param ecgDataArrayPart
     * @param preview
     * @param ecgDataFilePath
     * @param testMode           正常传true；后门批量分析，传false
     * @return
     */
    public MeasureResultBean traditionalAnalysis(Context context, EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType, EcgSettingConfigEnum.LeadType leadType, PatientInfoBean patientInfoBeanSrc,
                                                short[][] ecgDataArrayPart, boolean preview, String ecgDataFilePath, boolean testMode) {

        PatientInfoBean patientInfoBean = (PatientInfoBean) SerializableUtils.copy(patientInfoBeanSrc);

        //这三个字段不能为null,否则分析失败
        if (TextUtils.isEmpty(patientInfoBean.getArchivesName())) {
            patientInfoBean.setArchivesName("w");
        }
        if (TextUtils.isEmpty(patientInfoBean.getSex())) {
            patientInfoBean.setSex("0");
        }
        if (TextUtils.isEmpty(patientInfoBean.getAge())) {
            patientInfoBean.setAge("20");
            patientInfoBean.setAgeUnit(PatientSettingConfigEnum.AgeUnit.YEAR);
        }
        if (TextUtils.isEmpty(patientInfoBean.getBirthdate())) {
            patientInfoBean.setBirthdate("1990-09-08");
        }

        ConfigBean configBeanMain = MainEcgManager.getInstance().getConfigBeanTemp();

        MeasureResultBean macureResultBean = null;
        if (leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_RR ||
                leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_HRV) {

            short[][] ecgDataArraySrcAll = null;
            if (!TextUtils.isEmpty(ecgDataFilePath)) {
                //实时采集的数据，传进来的是部分数据，需要，重新解析一下所有的数据
                short[][] ecgDataArrayTemp = parseEcgDataFile(context, ecgDataFilePath, false).getEcgDataArray();
                ecgDataArraySrcAll = WaveEncodeUtil.leadDataSwitch(ecgDataArrayTemp, false, leadType, false);
            } else {
                ecgDataArraySrcAll = WaveEncodeUtil.leadDataSwitch(ecgDataArrayPart, false, leadType, false);
            }

            //rr分析算法分析。一个算法分析，只是画的图不一样
            RRAnalysisResultBean rrAnalysisResultBean = null;
            int rth1 = configBeanMain.getEcgSettingBean().getRhythmLead1().ordinal();

            int dataLenTemp = ecgDataArraySrcAll[rth1].length;
            int timeLen = dataLenTemp / Const.SAMPLE_RATE;//s
            float[] mvDataArrayTemp = new float[dataLenTemp];
            for (int i = 0; i < dataLenTemp; i++) {
                mvDataArrayTemp[i] = ecgDataArraySrcAll[rth1][i] * Const.SHORT_MV_GAIN;
            }
            KLog.d(String.format("分析数据的长度:%d", ecgDataArraySrcAll[0].length));

            //降采样
            float[] mvDataArray = new float[dataLenTemp / 4];//4=1000/250 1000降采样到250
            NotifyResampleBean notifyResample = new NotifyResampleBean();
            notifyResample.setResampleDataArray(mvDataArray);
            notifyResample.setDataLen(mvDataArray.length);
            JniResample.getInstance().resample(mvDataArrayTemp, dataLenTemp, Const.SAMPLE_RATE, Const.SAMPLE_RATE / 4, notifyResample);

            int dataLen = notifyResample.getDataLen();
            if (dataLen > 0) {
                NotifyQrsDetectBean notifyQrsDetect = new NotifyQrsDetectBean();
                int[] qrsPosArray = new int[dataLen];
                notifyQrsDetect.setQrsPosArray(qrsPosArray);
                notifyQrsDetect.setQrsArrayLength(qrsPosArray.length);
                JniQrsDetect.getInstance().qrsDetect(mvDataArray, dataLen, Const.SAMPLE_RATE / 4, notifyQrsDetect);

                boolean exception = false;
                //校正qrs 根据幅值
                for (int i = 0; i < qrsPosArray.length; i++) {
                    if (qrsPosArray[i] >= mvDataArray.length) {
                        exception = true;
                        break;
                    }
                    if (mvDataArray[qrsPosArray[i]] < 0.1) {
                        qrsPosArray[i] = -1;
                    }
                }

                if (exception) {
                    KLog.d("hrv检测 心率检测异常");
                    qrsPosArray = new int[2];
                    notifyQrsDetect.setQrsArrayLength(2);
                }

                int qrsLen = notifyQrsDetect.getQrsArrayLength();
                double[] RR_interval;
                if (qrsLen > 1) {
                    RR_interval = new double[qrsLen - 1];
                } else {
                    //导联脱落，或qrslen < 1
                    RR_interval = new double[2];
                }

                double value = 0;
                int true_RR_interval = 0;
                for (int i = 0; i < RR_interval.length; i++) {
                    if (qrsPosArray[i] > 0 && qrsPosArray[i + 1] > 0) {
                        RR_interval[i] = (qrsPosArray[i + 1] - qrsPosArray[i]);
                        value += RR_interval[i];
                        true_RR_interval++;
                    }
                }
                int hr = 0;
                if (true_RR_interval > 0) {
                    double avgValue = value / true_RR_interval;
                    if (avgValue > 0) {
                        hr = (int) (60 / (avgValue / Const.SAMPLE_RATE) / 4);
                    }
                }
                KLog.d(String.format("hrv 检测，计算心率:%d", hr));

                rrAnalysisResultBean = new RRAnalysisResultBean();
                JniSample.getInstance().getRRAnalysisData(rrAnalysisResultBean, RR_interval, RR_interval.length, Const.SAMPLE_RATE / 4);// / 4
                rrAnalysisResultBean.setRR_interval(RR_interval);

                if (qrsLen < 2) {
                    rrAnalysisResultBean.setMEAN_RR_interval(0D);
                }
                if (leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_RR)
                    rrAnalysisResultBean.setMacureTime(timeLen);
                else
                    rrAnalysisResultBean.setMacureTime(timeLen);
                //值过大，异常了
                if (rrAnalysisResultBean.getRMSSD() > 100000) {
                    rrAnalysisResultBean.setRMSSD(0D);
                }

                rrAnalysisResultBean.setMAX_RR_interval(rrAnalysisResultBean.getMAX_RR_interval() / 250F * 1000);
                rrAnalysisResultBean.setMIN_RR_interval(rrAnalysisResultBean.getMIN_RR_interval() / 250F * 1000);
                rrAnalysisResultBean.setMEAN_RR_interval(rrAnalysisResultBean.getMEAN_RR_interval() / 250F * 1000);

                double[] RR_interval_array = rrAnalysisResultBean.getRR_interval();
                if (RR_interval_array != null && RR_interval_array.length > 0) {
                    for (int i = 0; i < RR_interval_array.length; i++) {
                        RR_interval_array[i] = RR_interval_array[i] / 250F * 1000;
                    }
                }
                rrAnalysisResultBean.setRR_interval(RR_interval_array);

                double[] RR_interval_diff_array = rrAnalysisResultBean.getRR_interval_Diff();
                if (RR_interval_diff_array != null && RR_interval_diff_array.length > 0) {
                    for (int i = 0; i < RR_interval_diff_array.length; i++) {
                        RR_interval_diff_array[i] = RR_interval_diff_array[i] / 250F * 1000;
                    }
                }
                rrAnalysisResultBean.setRR_interval_Diff(RR_interval_diff_array);

                float[] psdXy = rrAnalysisResultBean.getPSD_specXY();
                StringBuilder sb = new StringBuilder();
                if (psdXy != null && psdXy.length > 0) {
                    for (int i = 0; i < psdXy.length; i++) {
                        sb.append(String.format("%.5f", psdXy[i])).append(",");
                    }
                }
                KLog.d(String.format("rr pxy:%s", sb.toString()));
            }

            macureResultBean = new MacureResultBean();
            macureResultBean.setEcgMacureResultEnum(EcgMacureResultEnum.TYPE_LOCAL_HRV_RESULT);
            macureResultBean.setRrAnalysisResultBean(rrAnalysisResultBean);
        } else {
            //常规算法分析
            short[][] ecgDataArrayPartAll = WaveEncodeUtil.leadDataSwitch(ecgDataArrayPart, false, leadType, false);

            if (configBeanMain.getSystemSettingBean().isUseGlasgow()) {
                if (leadType == EcgSettingConfigEnum.LeadType.LEAD_12) {
                    macureResultBean = GriAnlysManager.getInstance().processEcg(patientInfoBean, ecgDataArrayPartAll, leadType, DetectManager.getInstance().leadStateList, false, preview);
                    KLog.d(String.format("分析数据的长度:%d", ecgDataArrayPartAll[0].length));
                } else {
                    //18导联，glassgo不支持分析。类型 算法 无
                    macureResultBean = new MacureResultBean();
                    macureResultBean.setEcgMacureResultEnum(EcgMacureResultEnum.TYPE_NONE);
                }
            } else {
                //去掉最后两个通道数据 起搏通道，导联脱落通道
                short[][] ecgDataArrayOri = new short[ecgDataArrayPart.length - Const.LEAD_ADD_EXTRA_COUNT][ecgDataArrayPart[0].length];
                for (int i = 0; i < ecgDataArrayOri.length; i++) {
                    System.arraycopy(ecgDataArrayPart[i], 0, ecgDataArrayOri[i], 0, ecgDataArrayOri[0].length);
                }

                String resultFilePathTest;
                if (testMode) {
                    resultFilePathTest = SdLocal.getTestResultXmlHl7resultPath(context,
                            String.format("%s_%d", patientInfoBean.getPatientNumber(), System.currentTimeMillis()));
                } else {
                    resultFilePathTest = SdLocal.getDataXmlHl7resultPath(context,
                            String.format("%s_%d", patientInfoBean.getPatientNumber(), System.currentTimeMillis()));
                }

                AiResultBean aiResultBean = new AiResultBean();
                JniTraditionalAnalysis.getInstance().traditionalAnalysis(WaveEncodeUtil.switchEcgDataArray(ecgDataArrayOri), ecgDataArrayOri[0].length, aiResultBean, patientInfoBean,
                        Setting.TRADITIONAL_ANALYSIS_RESULT_XML, resultFilePathTest,
                        (Integer) leadType.getValue(), Const.ANALYSIS_DIAGNOSIS_MODE);

                KLog.d(String.format("分析数据的长度:%d", ecgDataArrayOri[0].length));

                StringBuilder sb = new StringBuilder();
                sb.append("JSON:");
                sb.append(aiResultBean.getArrDiagnosis());
                aiResultBean.setArrDiagnosis(sb.toString());

                aiResultBean = AiResultBean.manualSetValue(aiResultBean);
                if (aiResultBean.getWaveForm12() != null) {
                    //赋值，序列化使用
                    short value;
                    for (int i = 0; i < aiResultBean.getWaveForm12().length; i++) {
                        for (int j = 0; j < aiResultBean.getWaveForm12()[0].length; j++) {
                            value = (short) (aiResultBean.getWaveForm12()[i][j] / 1000F * Const.MV_1_SHORT * Const.SMART_ECG_TRADITION_ANALYSIS_TEMPLATE);
                            aiResultBean.getWaveForm12()[i][j] = value;
                        }
                    }
//                short[] avR = aiResultBean.getWaveForm12()[3];
//                short[] avL = aiResultBean.getWaveForm12()[4];
//                //传统算法分析结果，平均模板：R L F;Ai分析结果是:L R F;平均模板显示，数据转换成了 R L F 显示
//                aiResultBean.getWaveForm12()[3] = avL;
//                aiResultBean.getWaveForm12()[4] = avR;
                    aiResultBean.setWaveForm12Array(WaveEncodeUtil.getEcgDataArrayAiAnalysisWaveForm(aiResultBean.getWaveForm12()));
                }

                //添加其它测量值 幅值，单独计算 求平均.目前不用了
                List<AiResultMeasuredValueBean> aiResultBeanMeasuredValueList = aiResultBean.getMeasuredValueList();
                if (aiResultBeanMeasuredValueList != null && aiResultBeanMeasuredValueList.size() > 0) {
                    short PA_avg = 0;
                    short QA_avg = 0;
                    short RA_avg = 0;
                    short SA_avg = 0;
                    short TA_avg = 0;
                    short ST20A_avg = 0;
                    short ST40A_avg = 0;
                    short ST60A_avg = 0;
                    short ST80A_avg = 0;

                    short leadCount = 0;
                    for (int i = 0; i < aiResultBeanMeasuredValueList.size(); i++) {
                        if (i < 2 || i > 5) {
                            //I II V1-V6
                            AiResultMeasuredValueBean item = aiResultBeanMeasuredValueList.get(i);
                            PA_avg += item.getPa1();
                            QA_avg += item.getQa();
                            RA_avg += item.getRa1();
                            SA_avg += item.getSa1();
                            TA_avg += item.getTa1();
                            ST20A_avg += item.getST20();
                            ST40A_avg += item.getST40();
                            ST60A_avg += item.getST60();
                            ST80A_avg += item.getST80();

                            leadCount++;
                        }
                    }
                    PA_avg = (short) (PA_avg / leadCount);
                    QA_avg = (short) (QA_avg / leadCount);
                    RA_avg = (short) (RA_avg / leadCount);
                    SA_avg = (short) (SA_avg / leadCount);
                    TA_avg = (short) (TA_avg / leadCount);
                    ST20A_avg = (short) (ST20A_avg / leadCount);
                    ST40A_avg = (short) (ST40A_avg / leadCount);
                    ST60A_avg = (short) (ST60A_avg / leadCount);
                    ST80A_avg = (short) (ST80A_avg / leadCount);

                    aiResultBean.setPA(PA_avg);
                    aiResultBean.setQA(QA_avg);
                    aiResultBean.setRA(RA_avg);
                    aiResultBean.setSA(SA_avg);
                    aiResultBean.setTA(TA_avg);
                    aiResultBean.setST20A(ST20A_avg);
                    aiResultBean.setST40A(ST40A_avg);
                    aiResultBean.setST60A(ST60A_avg);
                    aiResultBean.setST80A(ST80A_avg);
                }

                macureResultBean = new MacureResultBean();
                macureResultBean.setEcgMacureResultEnum(EcgMacureResultEnum.TYPE_LOCAL_TRADITIONAL_RESULT);
                macureResultBean.setAiResultBean(aiResultBean);
            }
        }
        //紧急使用，抹掉诊断结果
        if (LoginManager.getInstance().isEmergencyUseMode() && macureResultBean != null) {
            macureResultBean.getAiResultBean().setArrDiagnosis("");
        }
        return macureResultBean;
    }
}
