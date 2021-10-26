package com.lepu.algorithm.ecg.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lepu.algorithm.Carewell.OmniEcg.jni.JniQrsDetect;
import com.lepu.algorithm.Carewell.OmniEcg.jni.JniResample;
import com.lepu.algorithm.Carewell.OmniEcg.jni.JniSample;
import com.lepu.algorithm.Carewell.OmniEcg.jni.JniTraditionalAnalysis;
import com.lepu.algorithm.ecg.entity.AiResultBean;
import com.lepu.algorithm.ecg.entity.AiResultMeasuredValueBean;
import com.lepu.algorithm.ecg.entity.ConfigBean;
import com.lepu.algorithm.ecg.entity.EcgDataInfoBean;
import com.lepu.algorithm.ecg.entity.MeasureResultBean;
import com.lepu.algorithm.ecg.entity.NotifyQrsDetectBean;
import com.lepu.algorithm.ecg.entity.NotifyResampleBean;
import com.lepu.algorithm.ecg.entity.PatientInfoBean;
import com.lepu.algorithm.ecg.entity.RRAnalysisResultBean;
import com.lepu.algorithm.ecg.entity.dictionary.EcgMacureResultEnum;
import com.lepu.algorithm.ecg.entity.dictionary.EcgMeasureResultEnum;
import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;
import com.lepu.algorithm.ecg.entity.dictionary.PatientSettingConfigEnum;
import com.lepu.algorithm.ecg.utils.Const;
import com.lepu.algorithm.ecg.utils.FormatTransfer;
import com.lepu.algorithm.ecg.utils.SdLocal;
import com.lepu.algorithm.ecg.utils.SerializableUtils;
import com.lepu.algorithm.ecg.utils.WaveEncodeUtil;
import com.lepu.algorithm.restingecg.gri.GriAnlysManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static com.lepu.algorithm.BuildConfig.DEBUG;

public class AnalysisManager<macureResultBean> {

    private ConfigBean configBeanTemp;

    public static final int TRADITIONAL_ANALYSIS_RESULT_XML = DEBUG ? 1 : 0;

    private AnalysisManager() {
    }

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

        ConfigBean configBeanMain = configBeanTemp;

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
//            KLog.d(String.format("分析数据的长度:%d", ecgDataArraySrcAll[0].length));

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
//                    KLog.d("hrv检测 心率检测异常");
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
//                KLog.d(String.format("hrv 检测，计算心率:%d", hr));

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
//                KLog.d(String.format("rr pxy:%s", sb.toString()));
            }

            macureResultBean = new MeasureResultBean();
            macureResultBean.setEcgMacureResultEnum(EcgMeasureResultEnum.TYPE_LOCAL_HRV_RESULT);
            macureResultBean.setRrAnalysisResultBean(rrAnalysisResultBean);
        } else {
            //常规算法分析
            short[][] ecgDataArrayPartAll = WaveEncodeUtil.leadDataSwitch(ecgDataArrayPart, false, leadType, false);

            if (true) { //默认使用glassgow算法
                if (leadType == EcgSettingConfigEnum.LeadType.LEAD_12) {
                    macureResultBean = GriAnlysManager.getInstance().processEcg(patientInfoBean, ecgDataArrayPartAll, leadType, DetectManager.getInstance().leadStateList, false, preview);
//                    KLog.d(String.format("分析数据的长度:%d", ecgDataArrayPartAll[0].length));
                } else {
                    //18导联，glassgo不支持分析。类型 算法 无
                    macureResultBean = new MeasureResultBean();
                    macureResultBean.setEcgMacureResultEnum(EcgMeasureResultEnum.TYPE_NONE);
                }
            } else {
                //去掉最后两个通道数据 起搏通道，导联脱落通道
                short[][] ecgDataArrayOri = new short[ecgDataArrayPart.length - Const.LEAD_ADD_EXTRA_COUNT][ecgDataArrayPart[0].length];
                for (int i = 0; i < ecgDataArrayOri.length; i++) {
                    System.arraycopy(ecgDataArrayPart[i], 0, ecgDataArrayOri[i], 0, ecgDataArrayOri[0].length);
                }

                String resultFilePathTest = null;
//                if (testMode) {
//                    resultFilePathTest = SdLocal.getTestResultXmlHl7resultPath(context,
//                            String.format("%s_%d", patientInfoBean.getPatientNumber(), System.currentTimeMillis()));
//                } else {
//                    resultFilePathTest = SdLocal.getDataXmlHl7resultPath(context,
//                            String.format("%s_%d", patientInfoBean.getPatientNumber(), System.currentTimeMillis()));
//                }

                AiResultBean aiResultBean = new AiResultBean();
                JniTraditionalAnalysis.getInstance().traditionalAnalysis(WaveEncodeUtil.switchEcgDataArray(ecgDataArrayOri), ecgDataArrayOri[0].length, aiResultBean, patientInfoBean,
                        TRADITIONAL_ANALYSIS_RESULT_XML, resultFilePathTest,
                        (Integer) leadType.getValue(), Const.ANALYSIS_DIAGNOSIS_MODE);

//                KLog.d(String.format("分析数据的长度:%d", ecgDataArrayOri[0].length));

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

                macureResultBean = new MeasureResultBean();
                macureResultBean.setEcgMacureResultEnum(EcgMeasureResultEnum.TYPE_LOCAL_TRADITIONAL_RESULT);
                macureResultBean.setAiResultBean(aiResultBean);
            }


        }
        //紧急使用，抹掉诊断结果
//        if (LoginManager.getInstance().isEmergencyUseMode() && macureResultBean != null) {
//            macureResultBean.getAiResultBean().setArrDiagnosis("");
//        }

        return macureResultBean;
    }


    /**
     * 解析心电数据文件
     *
     * @param context
     * @param filePath
     * @return
     */
    public static EcgDataInfoBean parseEcgDataFile(Context context, String filePath, boolean ifNeedFilter) {
        return parseEcgDataFile(context, filePath, true, 0, ifNeedFilter);
    }

    /**
     * @param context
     * @param filePath
     * @param readAllData            是否读所有可用数据
     * @param lastReadDataSampleSize 读最后的数据的长度，单导联的长度
     * @return
     */
    public static EcgDataInfoBean parseEcgDataFile(Context context, String filePath, boolean readAllData, int lastReadDataSampleSize, boolean ifNeedFilter) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
//        KLog.d("parseEcgDataFile");

        //几通道数据
        short[][] ecgDataArray = null;
        byte leadTypeEnum = 0;
        float filterAdsValue = 0;
        int filterEmgValue = 0;
        int filterLowpassValue = 0;
        int filterAcValue = 0;
        int filterAcOpenStateValue = 0;

        RandomAccessFile raf = null;
        try {
            byte[] destReadArray;
            long seekIndex = 0;
            int perChannelSampleSize = 0;
            List<short[][]> ecgDataArrayList = new ArrayList<>();
            byte[] valueArray = new byte[2];
            short value;
            int readTimes = 0;
            int readSampleSize = 0;
            byte[] valueArrayTemp;
            int parseIndex = 0;

            raf = new RandomAccessFile(filePath, "r");

            //读头数据
            seekIndex = 0;
            destReadArray = new byte[Const.ECG_DATA_HEADER_LEN];
            int readCount = raf.read(destReadArray, 0, destReadArray.length);
            if (readCount > 0) {
                //解析头数据
                //leadType
                parseIndex = Const.ECG_DATA_HEADER_LEN_CUSTOM_MY;
                leadTypeEnum = destReadArray[parseIndex];
                parseIndex += 1;

                //ads
                valueArrayTemp = new byte[4];
                System.arraycopy(destReadArray, parseIndex, valueArrayTemp, 0, valueArrayTemp.length);
                filterAdsValue = FormatTransfer.lBytesToFloat(valueArrayTemp);
                parseIndex += valueArrayTemp.length;

                //emg
                valueArrayTemp = new byte[4];
                System.arraycopy(destReadArray, parseIndex, valueArrayTemp, 0, valueArrayTemp.length);
                filterEmgValue = FormatTransfer.lBytesToInt(valueArrayTemp);
                parseIndex += valueArrayTemp.length;

                //lowpass
                valueArrayTemp = new byte[4];
                System.arraycopy(destReadArray, parseIndex, valueArrayTemp, 0, valueArrayTemp.length);
                filterLowpassValue = FormatTransfer.lBytesToInt(valueArrayTemp);
                parseIndex += valueArrayTemp.length;

                //ac
                valueArrayTemp = new byte[4];
                System.arraycopy(destReadArray, parseIndex, valueArrayTemp, 0, valueArrayTemp.length);
                filterAcValue = FormatTransfer.lBytesToInt(valueArrayTemp);
                parseIndex += valueArrayTemp.length;

                //ac open state
                valueArrayTemp = new byte[4];
                System.arraycopy(destReadArray, parseIndex, valueArrayTemp, 0, valueArrayTemp.length);
                filterAcOpenStateValue = FormatTransfer.lBytesToInt(valueArrayTemp);
                parseIndex += valueArrayTemp.length;

                // 读取有几通道数据
                byte leadChannelCount = destReadArray[Const.FILE_POS_LEAD_SIZE];
                //读取每个通道有多少采样点
                byte[] perChannelSampleSizeArray = new byte[4];
                System.arraycopy(destReadArray, Const.FILE_POS_SAMPLE_SIZE, perChannelSampleSizeArray, 0, perChannelSampleSizeArray.length);
                perChannelSampleSize = FormatTransfer.lBytesToInt(perChannelSampleSizeArray);

                byte[] simpleDataPackageByteArray = new byte[leadChannelCount * 2];

                //每次读1秒心电数据
                int perReadSecondLen = Const.SAMPLE_RATE * 1;
                destReadArray = new byte[perReadSecondLen * leadChannelCount * 2];
                int perReadPoints = 0;

                if (!readAllData) {
                    //读指定数据长度的数据
                    int perChannelSeekDataLen = perChannelSampleSize - lastReadDataSampleSize;
                    int seekEcgDataLen = perChannelSeekDataLen * leadChannelCount * 2;

                    if (seekEcgDataLen > 0) {
                        raf.seek(seekEcgDataLen);
                        perChannelSampleSize = lastReadDataSampleSize;
                    }
                }

                while (readSampleSize < perChannelSampleSize) {
                    int perReadCount = (readSampleSize + perReadSecondLen) > perChannelSampleSize ? ((perChannelSampleSize - readSampleSize) * leadChannelCount * 2) : destReadArray.length;
                    readCount = raf.read(destReadArray, 0, perReadCount);
                    if (readCount <= 0) {
                        break;
                    }
                    readTimes++;
                    perReadPoints = (readCount / leadChannelCount / 2);
                    readSampleSize += perReadPoints;

                    //KLog.d(String.format("读取心电文件:%d次，个数:%d",readTimes,perReadPoints));
                    //解析心电数据
                    short[][] ecgDataArrayListItem = new short[leadChannelCount][perReadPoints];
                    for (int i = 0; i < perReadPoints; i++) {
                        System.arraycopy(destReadArray, i * simpleDataPackageByteArray.length,
                                simpleDataPackageByteArray, 0,
                                simpleDataPackageByteArray.length);

                        for (int j = 0; j < leadChannelCount; j++) {
                            valueArray[0] = simpleDataPackageByteArray[j * 2];
                            valueArray[1] = simpleDataPackageByteArray[j * 2 + 1];
                            value = FormatTransfer.lBytesToShort(valueArray);
                            ecgDataArrayListItem[j][i] = value;
                        }
                    }
                    ecgDataArrayList.add(ecgDataArrayListItem);
                }
                ecgDataArray = new short[leadChannelCount][perChannelSampleSize];
                for (int k = 0; k < ecgDataArray.length; k++) {
                    for (int i = 0; i < ecgDataArrayList.size(); i++) {
                        System.arraycopy(ecgDataArrayList.get(i)[k], 0,
                                ecgDataArray[k], i * ecgDataArrayList.get(i)[k].length,
                                ecgDataArrayList.get(i)[k].length);
                    }
                }
            } else {
//                KLog.d("读取心电文件头，错误");
            }
        } catch (Exception e) {
//            KLog.e(Log.getStackTraceString(e));
        } finally {
            if (raf != null) {
                try {
                    //raf.seek(0);
                    raf.close();
                } catch (IOException e) {
//                    KLog.e(Log.getStackTraceString(e));
                }
            }
        }

        //取默认值，数据文件，不存储
        EcgSettingConfigEnum.LeadSpeedType leadSpeedType = EcgSettingConfigEnum.LeadSpeedType.FORMFEED_25;
        EcgSettingConfigEnum.LeadGainType leadGainType = EcgSettingConfigEnum.LeadGainType.GAIN_10;
        float[] gainArray = new float[]{1.0F, 1.0F};

        //取数据存储的
        EcgSettingConfigEnum.LeadType leadType = EcgSettingConfigEnum.LeadType.values()[leadTypeEnum];
        EcgSettingConfigEnum.LeadFilterType leadFilterTypeAds = EcgSettingConfigEnum.LeadFilterType.getLeadEnumByValue(filterAdsValue);
        EcgSettingConfigEnum.LeadFilterType leadFilterTypeEmg = EcgSettingConfigEnum.LeadFilterType.getLeadEnumByValue(filterEmgValue);
        EcgSettingConfigEnum.LeadFilterType leadFilterTypeLowpass = EcgSettingConfigEnum.LeadFilterType.getLeadEnumByValue(filterLowpassValue);
        EcgSettingConfigEnum.LeadFilterType leadFilterTypeAc = EcgSettingConfigEnum.LeadFilterType.getLeadEnumByValue(filterAcValue);
        EcgSettingConfigEnum.LeadFilterType leadFilterTypeAcOpenState = EcgSettingConfigEnum.LeadFilterType.getLeadEnumByValue(filterAcOpenStateValue);

        EcgDataInfoBean ecgDataInfoBean = new EcgDataInfoBean();
        //保存到文件中的数据，是原始数据，需要先滤波
//        if(ifNeedFilter){
//            FilterManager.getInstance().resetFilter();
//            short[][] ecgDataArrayFilter = FilterManager.getInstance().filter(SettingManager.getInstance().getConfigBeanTemp(),ecgDataArray,ecgDataArray.length-2);
//            ecgDataInfoBean.setEcgDataArray(ecgDataArrayFilter);
//        }else{
//            ecgDataInfoBean.setEcgDataArray(ecgDataArray);
//        }

        ecgDataInfoBean.setEcgDataArray(ecgDataArray);
        ecgDataInfoBean.setLeadSpeedType(leadSpeedType);
        ecgDataInfoBean.setLeadGainType(leadGainType);

        ecgDataInfoBean.setGainArray(gainArray);


        //float[] auto_gainArray = ecgDataInfoBean.getAuto_gainArray();
        // ecgDataInfoBean.setAuto_gainArray(auto_gainArray);

        ecgDataInfoBean.setLeadType(leadType);
        ecgDataInfoBean.setFilterAds(leadFilterTypeAds);
        ecgDataInfoBean.setFilterEmg(leadFilterTypeEmg);
        ecgDataInfoBean.setFilterLowpass(leadFilterTypeLowpass);
        ecgDataInfoBean.setFilterAc(leadFilterTypeAc);
        ecgDataInfoBean.setFilterAcOpenState(leadFilterTypeAcOpenState);

        if (ecgDataInfoBean.getFilterAds() == null) {
            ecgDataInfoBean.setFilterAds(EcgSettingConfigEnum.LeadFilterType.FILTER_BASELINE_067);
        }

        if (ecgDataInfoBean.getFilterEmg() == null) {
            ecgDataInfoBean.setFilterEmg(EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_CLOSE);
        }

        if (ecgDataInfoBean.getFilterLowpass() == null) {
            ecgDataInfoBean.setFilterLowpass(EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_75);
        }

        if (ecgDataInfoBean.getFilterAc() == null) {
            ecgDataInfoBean.setFilterAc(EcgSettingConfigEnum.LeadFilterType.FILTER_AC_50_HZ);
        }

        if (ecgDataInfoBean.getFilterAcOpenState() == null) {
            ecgDataInfoBean.setFilterAcOpenState(EcgSettingConfigEnum.LeadFilterType.FILTER_AC_OPEN);
        }

        return ecgDataInfoBean;
    }

}
