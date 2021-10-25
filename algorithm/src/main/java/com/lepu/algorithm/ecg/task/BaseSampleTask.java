package com.lepu.algorithm.ecg.task;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.print.PrintManager;
import android.text.TextUtils;
import android.util.Log;

//import com.casecenter.entity.PatientCaseBean;
//import com.casecenter.entity.PatientInfoBean;
//import com.casecenter.entity.dictionary.DiagnosticStateEnum;
//import com.casecenter.manager.localimpl.AppointmentDaoLocal;
//import com.casecenter.manager.localimpl.PatientCaseDaoLocal;
//import com.common.manager.AppManager;
//import com.ecg.entity.AiResultDiagnosisBean;
//import com.ecg.entity.EcgDataInfoBean;
//import com.ecg.entity.MacureResultBean;
//import com.ecg.entity.MinnesotaCodeItemBean;
//import com.ecg.entity.dictionary.EcgMacureResultEnum;
//import com.ecg.entity.dictionary.ThermalPrintExceptionEnum;
//import com.ecg.manager.DetectManager;
//import com.ecg.manager.EcgDataManager;
//import com.ecg.manager.PrintManager;
//import com.ecg.manager.RemoteDiagnosisManager;
//import com.ecg.manager.RemoteDiagnosisPlatformManager;
//import com.ecg.print.thermal.PrintCallback;
//import com.ecg.print.thermal.Send2PrinterThread;
//import com.ecg.print.thermal.ThermalPrintUtil;
//import com.lib.common.BaseApplication;
//import com.lib.common.manager.ActivityManager;
//import com.lib.common.util.DateUtil;
//import com.lib.common.util.FileUtil;
//import com.lib.common.util.log.KLog;
//import com.lib.common.utils.CallBackCommon;
//import com.lib.common.utils.DataTypeChangeHelper;
//import com.lib.common.utils.ObserverManager;
//import com.lib.common.utils.ThreadManager;
//import com.login.manager.LoginManager;
//import com.main.R;
//import com.main.manager.MainEcgManager;
//import com.main.utils.Const;
//import com.main.utils.SdLocal;
//import com.preview.entity.dictionary.PreviewPageEnum;
//import com.preview.manager.PreviewManager;
//import com.preview.manager.PrintPreviewManager;
//import com.setting.entity.ConfigBean;
//import com.setting.entity.EcgSettingBean;
//import com.setting.entity.SampleDrugTestTimePointBean;
//import com.setting.entity.SampleSettingBean;
//import com.setting.entity.dictionary.EcgSettingConfigEnum;
//import com.setting.entity.dictionary.RecordSettingConfigEnum;
//import com.setting.entity.dictionary.SystemSettingConfigEnum;
//import com.setting.entity.dictionary.UploadStateEnum;
//import com.setting.manager.SettingManager;


/**
 * Liuxm
 * */
import com.lepu.algorithm.ecg.entity.AiResultDiagnosisBean;
import com.lepu.algorithm.ecg.entity.ConfigBean;
import com.lepu.algorithm.ecg.entity.EcgSettingBean;
import com.lepu.algorithm.ecg.entity.MinnesotaCodeItemBean;
import com.lepu.algorithm.ecg.entity.PatientInfoBean;
import com.lepu.algorithm.ecg.entity.EcgDataInfoBean;
import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;
import com.lepu.algorithm.ecg.utils.CallBackCommon;
import com.lepu.algorithm.ecg.utils.Const;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;

public abstract class BaseSampleTask {

    public static final int MSG_UPDATE_PROGRESS = 10;
    public static final int DELAY_TIME_SHOW_TIP = 1000;

    Context context;
    ConfigBean configBean;
    PatientInfoBean patientInfoBean;
    EcgDataInfoBean ecgDataInfoBean;

    CallBackCommon callBackCommon;
    boolean runStateIng = false;//工作任务
    boolean sampleDataStateIng = false;//采集存储数据中
    boolean sampleTaskDoWithIng = false;//分析中

    int sampleTimeProgress = 0;
    int sampleMaxTime = 0;
    int sampleDataLen = 0;
    int sampleDataLenTotal = 0;
    String ecgDataFilePathTemp;
    FileOutputStream fileOutputStream;
    boolean ifCycleTask = false;
    boolean cancelRealtimeSampleTask = true;
    PatientCaseBean patientCaseBean = null;

    public abstract void initSampleTask();

    public abstract void startSampleTask();

    public abstract boolean stopSampleTask(boolean cancelTask);

    public abstract void updateProgress();

    public abstract void sampleDataTaskFinish();

    BaseSampleTask(Context context, ConfigBean configBean, PatientInfoBean patientInfoBean, EcgDataInfoBean ecgDataInfoBean, int sampleMaxTime) {
        this.context = context;
        this.configBean = configBean;
        this.patientInfoBean = patientInfoBean;
        this.ecgDataInfoBean = ecgDataInfoBean;
        this.sampleMaxTime = sampleMaxTime;
        patientCaseBean = new PatientCaseBean();
        patientCaseBean.setArchivesBean(patientInfoBean);

        EcgSettingBean ecgSettingBean = configBean.getEcgSettingBean();
        if (ecgSettingBean.getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_PERIODIC ||
                ecgSettingBean.getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_TRIGGER ||
                ecgSettingBean.getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_DRUG_TEST) {
            ifCycleTask = true;
        }
    }

    public CallBackCommon getCallBackCommon() {
        return callBackCommon;
    }

    public void setCallBackCommon(CallBackCommon callBackCommon) {
        this.callBackCommon = callBackCommon;
    }

    public boolean isRunStateIng() {
        return runStateIng;
    }

    public void setRunStateIng(boolean runStateIng) {
        this.runStateIng = runStateIng;
    }

    public boolean isSampleDataStateIng() {
        return sampleDataStateIng;
    }

    public void setSampleDataStateIng(boolean sampleDataStateIng) {
        this.sampleDataStateIng = sampleDataStateIng;
    }

    /**
     * 启动任务
     */
    public void start() {
        initEcgDataFilePath();
        //先传0 ，实时采集写数据完了，会修改这个采样点的个数值
        writeHeaderData(ecgDataFilePathTemp, 0);
        openEcgDataFileOutputStream(ecgDataFilePathTemp);

        sampleDataStateIng = true;
        runStateIng = true;
    }

    /**
     * 停止任务
     */
    public void stop() {
        runStateIng = false;
        sampleDataStateIng = false;
        sampleTaskDoWithIng = false;

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        closeEcgDataFileOutputStream();
    }

    /**
     * 添加采样数据
     * 保存文件 为原始数据
     *
     * @param ecgDataArrayOri 原始数据，没有计算后的导联
     */
    public void addSampleTaskData(short[][] ecgDataArrayOri, int heartRateValue) {
        if (sampleDataStateIng) {
            //滤波 2个线程都调用，可能会有问题，先传原始数据
            //short[][] ecgDataArrayDowith = FilterManager.getInstance().filter(configBean,ecgDataArrayOri,ecgDataArrayOri.length);
            short[][] ecgDataArrayDowith = ecgDataArrayOri;

            int dataLen = ecgDataArrayDowith[0].length;
            EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType = configBean.getEcgSettingBean().getLeadWorkModeType();
            boolean isEmergencyUseMode = LoginManager.getInstance().isEmergencyUseMode();
            boolean printPreview = configBean.getRecordSettingBean().isPrintPreview();
            boolean printOutput = configBean.getRecordSettingBean().isPrintOutput();

            if (leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_MANUAL) {
                if (Const.MANUAL_MODE_WRITE_DATA) {
                    //手动模式，没有最大采集时间，一直采集保存数据.方便调试更多的数据
                    boolean flag = EcgDataManager.saveEcgDataFileByfos(fileOutputStream, ecgDataArrayDowith, dataLen);
                    if (flag) {
                        sampleDataLen += dataLen;
                        sampleDataLenTotal += dataLen;
                    }
                }

                //KLog.d(String.format("采样保存数据的长度：%d",sampleDataLen));
                //热敏打印 手动模式
                if ((configBean.getRecordSettingBean().getPrintDeviceType() == RecordSettingConfigEnum.PrintDeviceType.THERMAL) && printOutput) {
                    if (ThermalPrintUtil.paperState) {
                        //缺纸
                        if (cancelRealtimeSampleTask) {
                            cancelRealtimeSampleTask = false;
                            ObserverManager.getInstance().notifyAsync(Const.Observer.NOTIFY_MAIN_CANCEL_REALTIME_SAMPLE_TASK, null, null);
                        }
                    } else {
                        //纸张正常
                        PrintManager.getInstance().printByThermalManualMode(thermalPrintCallback, configBean, ecgDataArrayDowith, heartRateValue,
                                MainEcgManager.getInstance().getGainArray(), (Float) configBean.getEcgSettingBean().getLeadSpeedType().getValue());
                    }
                }
            } else {
                //自动工作模式 支持热敏快速打印
                boolean autoRealMode = leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_REALTIME;
                boolean isEmergencyUseModePrint = isEmergencyUseMode && SettingManager.getInstance().isThermalPrintMode();

                int maxDataLen = sampleMaxTime * Const.SAMPLE_RATE;
                if ((sampleDataLen + dataLen) >= maxDataLen) {
                    int needDataLen = maxDataLen - sampleDataLen;
                    boolean flag = EcgDataManager.saveEcgDataFileByfos(fileOutputStream, ecgDataArrayOri, needDataLen);
                    if (flag) {
                        sampleDataLen += needDataLen;
                        sampleDataLenTotal += needDataLen;
                    }
                    KLog.d(String.format("采样保存数据的总长度：%d。本次采样任务结束,needDataLen=%d", sampleDataLenTotal, needDataLen));
                    sampleDataStateIng = false;
                    closeEcgDataFileOutputStream();

                    //热敏快速打印
                    if (!printPreview && autoRealMode && printOutput) {//!isEmergencyUseMode &&
                        if (SettingManager.getInstance().isThermalPrintQuickMode() || isEmergencyUseModePrint) {
                            short[][] ecgDataArrayTemp = new short[ecgDataArrayDowith.length][needDataLen];
                            for (int i = 0; i < ecgDataArrayTemp.length; i++) {
                                System.arraycopy(ecgDataArrayDowith[i], 0, ecgDataArrayTemp[i], 0, needDataLen);
                            }
                            PrintManager.getInstance().printByThermalAutoModeEcgData(thermalPrintCallback, configBean, patientCaseBean, null, ecgDataArrayTemp, heartRateValue, true,
                                    MainEcgManager.getInstance().getGainArray(), (Float) configBean.getEcgSettingBean().getLeadSpeedType().getValue(), maxDataLen, false);
                        }
                    }
                } else {
                    boolean flag = EcgDataManager.saveEcgDataFileByfos(fileOutputStream, ecgDataArrayOri, dataLen);
                    if (flag) {
                        sampleDataLen += dataLen;
                        sampleDataLenTotal += dataLen;
                        //KLog.d(String.format("采样保存数据的长度：%d",sampleDataLenTotal));

                        //热敏快速打印
                        if (!printPreview && autoRealMode && printOutput) {//!isEmergencyUseMode &&
                            if (SettingManager.getInstance().isThermalPrintQuickMode() || isEmergencyUseModePrint) {
                                PrintManager.getInstance().printByThermalAutoModeEcgData(thermalPrintCallback, configBean, patientCaseBean, null, ecgDataArrayDowith, heartRateValue, false,
                                        MainEcgManager.getInstance().getGainArray(), (Float) configBean.getEcgSettingBean().getLeadSpeedType().getValue(), maxDataLen, false);
                            }
                        }
                    }
                    //
                }
            }
        }
    }

    /**
     * 激光打印回调
     */
    public PrintCallback usbLaserPrintCallback = new PrintCallback() {
        @Override
        public void printBegin(Object object) {
            String message = context.getString(R.string.preview_text_send_print_task_ing);
            KLog.d(message);
            callBackCommon.callBackProgress(message);
        }

        @Override
        public void printFinish(Object object) {
            String message = context.getString(R.string.preview_text_send_print_task_success);
            KLog.d(message);
            callBackCommon.callBackProgress(message);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!ifCycleTask) {
                callBackCommon.callBackEnd(null);
            }else {
                callBackCommon.callBackProgress(context.getString(R.string.main_text_cycle_sample_ing));
            }
        }

        @Override
        public void printException(Object object) {
            String message = context.getString(R.string.preview_text_send_print_task_fail);
            KLog.d(message);
            callBackCommon.callBackProgress(message);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!ifCycleTask) {
                callBackCommon.callBackEnd(null);
            }else {
                callBackCommon.callBackProgress(context.getString(R.string.main_text_cycle_sample_ing));
            }
        }

        @Override
        public void printCancel(Object object) {
            String message = context.getString(R.string.preview_text_send_print_task_cancel);
            KLog.d(message);
            callBackCommon.callBackProgress(message);
        }
    };

    /**
     * 热敏打印回调
     */
    public PrintCallback thermalPrintCallback = new PrintCallback() {
        @Override
        public void printBegin(Object object) {
            String message = context.getString(R.string.main_text_print_ing);
            KLog.d(message);
            callBackCommon.callBackProgress(message);
        }

        @Override
        public void printFinish(Object object) {
            KLog.d("baseSampleTask callback printFinish");

            PrintManager.getInstance().printFinish(configBean);



            if (!ifCycleTask) {
                String message = context.getString(R.string.main_text_print_report_finish);
                KLog.d(message);
                callBackCommon.callBackProgress(message);

                callBackCommon.callBackEnd(null);
            }else {
                callBackCommon.callBackProgress(context.getString(R.string.main_text_cycle_sample_ing));
            }
        }

        @Override
        public void printException(Object object) {
            KLog.d("baseSampleTask callback printException");
            String message = context.getString(R.string.main_text_print_fail);
            if (object != null) {
                message = String.format(Locale.getDefault(), "%s %s", context.getString(R.string.main_text_print_fail), object.toString());
            }

            KLog.d(message);
            callBackCommon.callBackProgress(message);

            PrintManager.getInstance().printFinish(configBean);

            new Handler(Looper.getMainLooper()).postDelayed(()->{
                if (!ifCycleTask) {
                    callBackCommon.callBackEnd(null);
                }else {
                    callBackCommon.callBackProgress(context.getString(R.string.main_text_cycle_sample_ing));
                }
            },2000);

        }

        @Override
        public void printCancel(Object object) {
            KLog.d("baseSampleTask callback printCancel");

            PrintManager.getInstance().printFinish(configBean);

            if (!ifCycleTask) {
                String message = context.getString(R.string.main_text_print_cancel);
                KLog.d(message);
                callBackCommon.callBackProgress(message);
                callBackCommon.callBackEnd(null);
            }else {
                callBackCommon.callBackProgress(context.getString(R.string.main_text_cycle_sample_ing));
            }
        }
    };

    /**
     * 初始化心电数据文件名字
     */
    public void initEcgDataFilePath() {
        String patientId = "";
        if (patientInfoBean != null && !TextUtils.isEmpty(patientInfoBean.getPatientNumber())) {
            patientId = patientInfoBean.getPatientNumber();
        }
        String dateString = DateUtil.stringFromDate(new Date(System.currentTimeMillis()), "yyyyMMddHHmmss");
        String ecgDataFileName;
        if (configBean.getEcgSettingBean().getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_MANUAL) {
            ecgDataFileName = String.format("%s_manual_%s", patientId, dateString);
        } else {
            ecgDataFileName = String.format("%s_%s", patientId, dateString);
        }
        ecgDataFilePathTemp = SdLocal.getDataDatPath(context, ecgDataFileName);
    }

    /**
     * 打开心电数据流
     */
    public void openEcgDataFileOutputStream(String ecgDataFilePath) {
        try {
            fileOutputStream = new FileOutputStream(ecgDataFilePath, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭心电数据流
     */
    public void closeEcgDataFileOutputStream() {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
                fileOutputStream = null;
            } catch (IOException e) {
                KLog.e(Log.getStackTraceString(e));
            }
        }
    }

    /**
     * 删除保存的数据文件
     */
    public void deleteEcgDataFile(String ecgDataFilePath) {
        if (!TextUtils.isEmpty(ecgDataFilePath)) {
            File file = new File(ecgDataFilePath);
            FileUtil.deleteFile(context, file);
        }
    }

    /**
     * @param sec
     * @return
     */
    public String secondToString(int sec) {
        int minute = sec / 60;
        int second = sec % 60;

        String minuteString = minute > 0 ? String.format("%d min", minute) : "";
        String secondString = second > 0 ? String.format("%d s", second) : "";
        if (minute > 0 && second == 0) {
            secondString = "";
        } else if (minute == 0 && second == 0) {
            secondString = String.format("%d s", second);
        }
        String value = String.format("%s %s", minuteString, secondString);

        return value;
    }

    /**
     * 写数据头
     *
     * @param ecgDataFilePath
     * @param sampleSize
     */
    public void writeHeaderData(String ecgDataFilePath, int sampleSize) {
        byte leadSize = (byte) SettingManager.getInstance().getCurrentNeedLeadNum();
        EcgDataManager.saveEcgDataHeaderFile(context, ecgDataFilePath, ecgDataInfoBean, patientInfoBean, sampleSize, leadSize);
    }

    /**
     * 修改sample size
     *
     * @param ecgDataFilePath
     * @param sampleSize
     */
    public void modifyHeaderDataSampleSize(String ecgDataFilePath, int sampleSize) {
        byte[] dataArray = DataTypeChangeHelper.intToByteArray(sampleSize, true);
        FileUtil.modifyFileByte(ecgDataFilePath, dataArray, Const.FILE_POS_SAMPLE_SIZE);
        KLog.d(String.format("修改文件，数据长度为：%d", sampleSize));
    }

    /**
     * 修改 lead size
     *
     * @param ecgDataFilePath
     * @param leadSize
     */
    public void modifyHeaderDataLeadSize(String ecgDataFilePath, byte leadSize) {
        byte[] dataArray = new byte[]{leadSize};
        FileUtil.modifyFileByte(ecgDataFilePath, dataArray, Const.FILE_POS_LEAD_SIZE);
    }

    /**
     * 修改crc
     *
     * @param ecgDataFilePath
     * @param crc
     */
    public void modifyHeaderDataCrc(String ecgDataFilePath, short crc) {
        byte[] dataArray = DataTypeChangeHelper.shortToByteArray(crc, true);
        FileUtil.modifyFileByte(ecgDataFilePath, dataArray, Const.FILE_POS_CRC);
    }

    public boolean isSampleTaskDoWithIng() {
        return sampleTaskDoWithIng;
    }

    public void setSampleTaskDoWithIng(boolean sampleTaskDoWithIng) {
        this.sampleTaskDoWithIng = sampleTaskDoWithIng;
    }

    /**
     * 采集完成
     *
     * @param ecgDataFilePath
     */
    public void sampleFinish(String ecgDataFilePath) {
        sampleTaskDoWithIng = true;

        int needDataLen = Const.PER_SCREEN_DATA_SECOND * Const.SAMPLE_RATE;
        EcgDataInfoBean ecgDataInfoBeanTemp = EcgDataManager.parseEcgDataFile(context, ecgDataFilePath, false, needDataLen, true);
        sampleFinish(ecgDataInfoBeanTemp.getEcgDataArray(), ecgDataFilePath, null, null);
    }

    /**
     * 采集完成 药物试验主要调用
     *
     * @param ecgDataArrayPart 采集的当次数据
     * @param ecgDataFilePath  药物试验，手动模式等，全程采集的数据在filePath里
     */
    public void sampleFinish(short[][] ecgDataArrayPart, String ecgDataFilePath, String drugTestTimePointAll, String drugTestTimePointCurrent) {
        //KLog.d("sampleFinish");
        sampleTaskDoWithIng = true;
        String message;
        ecgDataInfoBean.setEcgDataArray(ecgDataArrayPart);

        EcgSettingBean ecgSettingBean = configBean.getEcgSettingBean();
        EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType = ecgSettingBean.getLeadWorkModeType();

        //设置当前药物试验时间点
        if (!TextUtils.isEmpty(drugTestTimePointCurrent)) {
            configBean.getSampleSettingBean().setDrugTestPointList(
                    SampleSettingBean.parseDrugTestPointList(drugTestTimePointCurrent));
        }
        //是否是触发采样
        boolean trigerSampleFlag = leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_TRIGGER;
        //是否是frank工作
        boolean frankMode = leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_FRANK;

        //本地传统分析 frank 模式暂时不分析
        MacureResultBean macureResultBean = null;
        boolean localDiagnosisMode = SettingManager.getInstance().isLocalDiagnosisMode();

        if (!frankMode && (localDiagnosisMode || leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_RR ||
                leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_HRV)) {
            if (!trigerSampleFlag) {
                message = context.getString(R.string.main_text_analysis_ing);
                callBackCommon.callBackProgress(message);
            }

            //test
            int analysisCount = 1;
            for (int i = 0; i < analysisCount; i++) {
                //KLog.d(String.format("分析第%d次",i+1));
                macureResultBean = EcgDataManager.getInstance().analysis(context, ecgSettingBean.getLeadWorkModeType(),
                        ecgSettingBean.getLeadType(), patientInfoBean, ecgDataArrayPart, false, ecgDataFilePath, false);
                //KLog.d(String.format("分析第%d次完成",i+1));
            }

            boolean isException = false;
            if (trigerSampleFlag) {
                String codeException = "";
                StringBuilder resultCodeSb = new StringBuilder();
                if (macureResultBean != null && macureResultBean.getAiResultBean() != null) {
                    AiResultDiagnosisBean aiResultDiagnosisBean = macureResultBean.getAiResultBean().getAiResultDiagnosisBean();
                    if (aiResultDiagnosisBean != null && aiResultDiagnosisBean.getDiagnosis() != null
                            && aiResultDiagnosisBean.getDiagnosis().size() > 0) {
                        for (MinnesotaCodeItemBean item : aiResultDiagnosisBean.getDiagnosis()) {
                            resultCodeSb.append(String.format("(%s:%s)", item.getCode(), EcgDataManager.getInstance().getDiagnosisTitle(item))).append(",");
                            if (EcgDataManager.getInstance().checkCodeExists(item)) {
                                isException = true;
                                codeException = item.getCode();
                                break;
                            }
                        }
                    }
                }

                KLog.d(String.format("触发采样 异常常态:%s，异常code:%s，诊断结果:%s", String.valueOf(isException), codeException, resultCodeSb.toString()));
            } else {
                if (macureResultBean == null) {
                    message = context.getString(R.string.main_text_analysis_fail);
                } else {
                    if (macureResultBean.getEcgMacureResultEnum() == EcgMacureResultEnum.TYPE_NONE) {
                        message = context.getString(R.string.main_tip_analysis_calc_not_support);
                    } else {
                        message = context.getString(R.string.main_text_analysis_success);
                    }
                }
                callBackCommon.callBackProgress(message);
            }

            try {
                Thread.sleep(DELAY_TIME_SHOW_TIP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //触发采样，并且没有异常
            if (trigerSampleFlag && !isException) {
                //删除没用的文件
                sampleTaskDoWithIng = false;
                deleteEcgDataFile(ecgDataFilePath);
                return;
            }
        }

        //保存 记录是否保存到数据库
        String resultFilePath = "";
        boolean saveFlag = configBean.getEcgSettingBean().isAutoSave();
        long checkTime = System.currentTimeMillis();

        if (saveFlag) {
            message = context.getString(R.string.main_text_save_ing);
            callBackCommon.callBackProgress(message);

            //保存测量结果
            if (macureResultBean != null) {
                String extraName = "";
                if (!TextUtils.isEmpty(drugTestTimePointCurrent)) {
                    extraName = drugTestTimePointCurrent;
                }
                File file = new File(ecgDataFilePath);
                if (file.exists()) {
                    String ecgDataFileName = FileUtil.getFileNameNoEx(file.getName());
                    resultFilePath = SdLocal.getDataDatPath(context, String.format("%s_%s_result", ecgDataFileName, extraName));
                    EcgDataManager.getInstance().writeConfigMacureResultBean(macureResultBean, resultFilePath);
                }
            }

            if (configBean.getEcgSettingBean().getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_DRUG_TEST) {
                if (!TextUtils.isEmpty(drugTestTimePointAll)) {
                    StringBuilder points = new StringBuilder(drugTestTimePointAll);
                    points = points.deleteCharAt(points.length() - 1);
                    drugTestTimePointAll = points.toString();
                    KLog.d(String.format("药物试验时间点类型保存，%s", drugTestTimePointAll));
                }
            }

            boolean flagSave;
            PatientCaseBean item = PatientCaseDaoLocal.getItemByEcgDataPath(ecgDataFilePath);
            KLog.i("MyTag","patientInfoId:"+patientInfoBean.getId());
            if (item != null) {
                //更新数据
                StringBuilder sbResultPath = new StringBuilder();
                String itemResultPath = item.getEcgDataResultPath();
                //先取老的结果路径
                sbResultPath.append(itemResultPath).append(",");
                //存新的结果路径
                if (!TextUtils.isEmpty(resultFilePath)) {
                    sbResultPath.append(resultFilePath).append(",");
                }
                sbResultPath = sbResultPath.deleteCharAt(sbResultPath.length() - 1);
                resultFilePath = sbResultPath.toString();
                KLog.d(String.format("多个结果路径:%s", resultFilePath));

                item.setDrugTestPoints(drugTestTimePointAll);
                item.setEcgDataResultPath(resultFilePath);
                flagSave = PatientCaseDaoLocal.updateEcgDataItem(item.getId(), resultFilePath, drugTestTimePointAll);
            } else {
                //保存数据库 新增数据
                flagSave = EcgDataManager.getInstance().saveEcgDataRecord(patientInfoBean, checkTime, macureResultBean, resultFilePath, ecgDataFilePath, configBean, drugTestTimePointAll, false, ecgDataInfoBean);
            }

            if(patientInfoBean.getId()>=0){
                AppointmentDaoLocal.deleteItemById(patientInfoBean.getId());
            }

            if (flagSave) {
                message = context.getString(R.string.main_text_save_success);
            } else {
                message = context.getString(R.string.main_text_save_fail);
            }
            callBackCommon.callBackProgress(message);
            try {
                Thread.sleep(DELAY_TIME_SHOW_TIP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean autoWorkMode = leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_REALTIME ||
                leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_PRE ||
                leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_TRIGGER ||
                leadWorkModeType == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_PERIODIC;
        boolean autoUploadFlag = configBean.getEcgSettingBean().isAutoUpload();


        //云模式 并且 允许保存，才上传。上传完，不打印
        if (!localDiagnosisMode && saveFlag && autoWorkMode && autoUploadFlag) {//&& autoUploadFlag
            message = context.getString(R.string.main_text_upload_ing);
            callBackCommon.callBackProgress(message);
            //upload
            final   PatientCaseBean patientCaseBean = PatientCaseDaoLocal.getItemByEcgDataPath(ecgDataFilePath);
            Objects.requireNonNull(patientCaseBean).setArchivesBean(patientInfoBean);
            String finalResultFilePath = resultFilePath;
            MacureResultBean finalMacureResultBean = macureResultBean;
            String finalDrugTestTimePointAll = drugTestTimePointAll;
            RemoteDiagnosisManager.getInstance().uploadDataSimple(context, patientCaseBean, false, new CallBackCommon() {
                @Override
                public void callBackSuccess(Object obj) {
                    super.callBackSuccess(obj);

                    String message = context.getString(R.string.main_text_upload_success);
                    KLog.d(message);
                    callBackCommon.callBackProgress(message);

                    ThreadManager.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(DELAY_TIME_SHOW_TIP);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            PatientCaseBean newpatientCaseBean = PatientCaseDaoLocal.getItemByEcgDataPath(ecgDataFilePath);
                            if (newpatientCaseBean!=null){
                                printStep(finalMacureResultBean, ecgDataFilePath, finalDrugTestTimePointAll, finalResultFilePath, checkTime);
                            }else {
                                ecgDataFilePathTemp=patientCaseBean.getEcgDataPath();
                                printStep(finalMacureResultBean, patientCaseBean.getEcgDataPath(), finalDrugTestTimePointAll, finalResultFilePath, checkTime);
                            }

                        }
                    });
                }

                @Override
                public void callBackFail(Object obj) {
                    super.callBackFail(obj);

                    String message = context.getString(R.string.main_text_upload_fail);
                    KLog.d(message);
                    callBackCommon.callBackProgress(message);
                    //检查设备号是否填写 如果没有填写 上传会报500错误
                    String deviceId = AppManager.getDeviceUniqueId(context);
                    if (TextUtils.isEmpty(deviceId)) {
                        message = context.getString(R.string.setting_text_system_set_device_sn);
                        callBackCommon.callBackProgress(message);
                    }
                    ThreadManager.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(DELAY_TIME_SHOW_TIP);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            printStep(finalMacureResultBean, ecgDataFilePath, finalDrugTestTimePointAll, finalResultFilePath, checkTime);
                        }
                    });
                }
            });
        } else {
            //去打印
            printStep(macureResultBean, ecgDataFilePath, drugTestTimePointAll, resultFilePath, checkTime);
        }
    }

    /**
     * @param macureResultBean
     * @param ecgDataFilePath
     * @param drugTestTimePointAll
     */
    private void printStep(MacureResultBean macureResultBean, String ecgDataFilePath, String drugTestTimePointAll, String analysisResultPath, long checkTime) {
        KLog.d("================", "printStep");
        if (configBean.getRecordSettingBean().getPrintDeviceType() == RecordSettingConfigEnum.PrintDeviceType.THERMAL) {

            KLog.d("use ThermalPrint...");

            ThermalPrintUtil.queryState().subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String msg) {

                    KLog.d("ThermalPrint onNext");
                    //检查 热敏打印机
                    boolean enable = PrintManager.getInstance().checkThermalPrintState(thermalPrintCallback);


                    KLog.d("ThermalPrint enable", enable);
                    if (enable) {


                        doPrint(macureResultBean, ecgDataFilePath, drugTestTimePointAll, analysisResultPath, checkTime);


                    }

                }

                @Override
                public void onError(Throwable e) {
                    KLog.e("ThermalPrint onError", e.toString());
                    thermalPrintCallback.printException(null);

                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            doPrint(macureResultBean, ecgDataFilePath, drugTestTimePointAll, analysisResultPath, checkTime);
        }

    }


    private void doPrint(MacureResultBean macureResultBean, String ecgDataFilePath, String drugTestTimePointAll, String analysisResultPath, long checkTime) {
        EcgSettingBean ecgSettingBean = configBean.getEcgSettingBean();
        //采集分析后预览
        boolean flagPreview = ecgSettingBean.isPreview();
        boolean notSupportWorkMode = ecgSettingBean.getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_PERIODIC ||
                ecgSettingBean.getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_TRIGGER ||
                ecgSettingBean.getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_DRUG_TEST;

        if (flagPreview && !notSupportWorkMode) {
            PreviewManager.IntentBuilder builder = new PreviewManager.IntentBuilder();
            builder.previewPageEnum(PreviewPageEnum.PAGE_PREVIEW)
                    .setCollectFinishJump(true)
                    .patientInfo(patientInfoBean)
                    .ecgDataFilePath(ecgDataFilePathTemp)
                    .macureResult(macureResultBean)
                    .readConfigFromEcgSetting(ecgSettingBean)
                    .previewDataDbId(-1)
                    .checkTimeStamp(System.currentTimeMillis())
                    .useGainArray(MainEcgManager.getInstance().getGainArray());
            callBackCommon.callBackEnd(null);
            builder.startActivity(ActivityManager.getInstance().currentActivity());
            return;
        }

        boolean printOutput = configBean.getRecordSettingBean().isPrintOutput();

        //打印可以操作取消，所有放在了打印的前面
        //是否打印
        //只有实时模式，可以打印预览。周期采样等，分析后，还要接着采样，所以不能打印预览
        boolean printPreview = configBean.getRecordSettingBean().isPrintPreview();
        boolean realMode = configBean.getEcgSettingBean().getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_REALTIME;
        boolean autoMode = configBean.getEcgSettingBean().getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_REALTIME ||
                configBean.getEcgSettingBean().getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_PRE ||
                configBean.getEcgSettingBean().getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_PERIODIC ||
                configBean.getEcgSettingBean().getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_SAMPLE_TRIGGER;

        if (printPreview && realMode) {
            //打印前先预览,实时模式起作用
            float[] gainAutoList = null;
            if (ecgSettingBean.getLeadGainType() == EcgSettingConfigEnum.LeadGainType.GAIN_AUTO &&
                    MainEcgManager.getInstance().getConfigBeanTemp().getEcgSettingBean().getLeadGainType() == EcgSettingConfigEnum.LeadGainType.GAIN_AUTO) {
                float[] gainBF = MainEcgManager.getInstance().getGainArray();
                gainAutoList = new float[gainBF.length];
                System.arraycopy(gainBF, 0, gainAutoList, 0, gainBF.length);
            }
            callBackCommon.callBackEnd(null);
            Activity currentActivity = ActivityManager.getInstance().currentActivity();
            PrintPreviewManager.getInstance().jumpPrintPreview(currentActivity, PreviewPageEnum.PAGE_PREVIEW, patientInfoBean, macureResultBean,
                    ecgSettingBean.getLeadWorkModeType(),
                    ecgSettingBean.getLeadType(),
                    ecgSettingBean.getLeadShowStyleType(),
                    null,
                    ecgDataFilePath, drugTestTimePointAll,
                    ecgSettingBean.getRhythmLead1(), ecgSettingBean.getRhythmLead2(), ecgSettingBean.getRhythmLead3(),
                    System.currentTimeMillis(), analysisResultPath,
                    ecgSettingBean.getLeadSpeedType(), ecgSettingBean.getLeadGainType(),
                    ecgSettingBean.getFilterEmg(), ecgSettingBean.getFilterLowpass(), ecgSettingBean.getFilterBaseline(), ecgSettingBean.getFilterAc(),
                    gainAutoList, configBean.getSystemSettingBean().getLeadFilterTypeAc());
        } else {
            if (printOutput) {
                //打印
                if (configBean.getRecordSettingBean().getPrintDeviceType() == RecordSettingConfigEnum.PrintDeviceType.THERMAL) {
                    //热敏打印
                    if (autoMode) {
                        //自动模式
                        if (realMode) {
                            //实时采样，分，省纸模式，快速模式
                            if (configBean.getRecordSettingBean().getRecordMode() == RecordSettingConfigEnum.RecordMode.TYPE_FAST) {
                                //快速打印 这里打印结果。心电数据已经提前实时打印完了
                                PatientCaseBean patientCaseBean = new PatientCaseBean();
                                patientCaseBean.setArchivesBean(patientInfoBean);
                                patientCaseBean.setCheckTime(checkTime);

                                PrintManager.getInstance().printByThermalModeResult(thermalPrintCallback, configBean, macureResultBean, patientCaseBean, false, null);
                            } else {
                                //省纸打印
                                PatientCaseBean patientCaseBean = new PatientCaseBean();
                                patientCaseBean.setArchivesBean(patientInfoBean);
                                patientCaseBean.setCheckTime(checkTime);
                                int heartRateValue = DetectManager.getInstance().getHeartRateValue();

                                //只打10s的话，取ecgDataInfoBean.getEcgDataArray()。打所有数据，就取path里的数据
                                //可能超过10s数据
                                short[][] ecgDataArrayAll = EcgDataManager.parseEcgDataFile(context, ecgDataFilePath, false).getEcgDataArray();
                                KLog.d(String.format("省纸模式，打印数据长度:%d", ecgDataArrayAll[0].length));
                                PrintManager.getInstance().printByThermalAutoModeEcgDataAndResultP(thermalPrintCallback, configBean, ecgDataArrayAll, heartRateValue,
                                        macureResultBean, patientCaseBean, MainEcgManager.getInstance().getGainArray(), (float) configBean.getEcgSettingBean().getLeadSpeedType().getValue(), false);
                            }
                        } else {
                            //预采样，周期采样，触发采样，多页心电图打印
                            PatientCaseBean patientCaseBean = new PatientCaseBean();
                            patientCaseBean.setArchivesBean(patientInfoBean);
                            patientCaseBean.setCheckTime(checkTime);
                            int heartRateValue = DetectManager.getInstance().getHeartRateValue();

                            PrintManager.getInstance().printByThermalAutoModeEcgDataAndResultOther(thermalPrintCallback, configBean, ecgDataInfoBean.getEcgDataArray(), heartRateValue,
                                    macureResultBean, patientCaseBean, MainEcgManager.getInstance().getGainArray(), (float) configBean.getEcgSettingBean().getLeadSpeedType().getValue(), false);
                        }
                    } else {
                        //其它工作模式
                        PatientCaseBean patientCaseBean = new PatientCaseBean();
                        patientCaseBean.setArchivesBean(patientInfoBean);
                        patientCaseBean.setCheckTime(checkTime);
                        boolean isDrugTestEnd = false;
                        //药物试验每10S数据打印
                        if (!TextUtils.isEmpty(drugTestTimePointAll)) {
                            String[] points = drugTestTimePointAll.split(",");
                            String lastPoint = points[points.length - 1];
                            patientCaseBean.setDrugTestPoints(drugTestTimePointAll);
                            List<SampleDrugTestTimePointBean> drugTestPointList = SettingManager.getInstance().getConfigBeanTemp().getSampleSettingBean().getDrugTestPointList();
                            int settingLastTimeIndex = 0;
                            for (SampleDrugTestTimePointBean bean : drugTestPointList) {
                                if (bean.isValue()) {
                                    settingLastTimeIndex = bean.getDrugTestSamplePointsMode().ordinal();
                                }
                            }
                            isDrugTestEnd = (Integer.parseInt(lastPoint) == settingLastTimeIndex);
                        }
                        int heartRateValue = DetectManager.getInstance().getHeartRateValue();
                        short[][] ecgDataArrayAll = EcgDataManager.parseEcgDataFile(BaseApplication.getInstance(), ecgDataFilePath, false).getEcgDataArray();
                        KLog.d("采集药物试验时间节点" + drugTestTimePointAll + "数据长度" + ecgDataArrayAll[0].length + "");

                        PrintManager.getInstance().printByThermalHighModeEcgDataAndResult(thermalPrintCallback, configBean, ecgDataArrayAll, heartRateValue,
                                macureResultBean, patientCaseBean, MainEcgManager.getInstance().getGainArray(), (float) configBean.getEcgSettingBean().getLeadSpeedType().getValue(), false, isDrugTestEnd);
                    }
                } else {
                    //激光打印
                    EcgDataInfoBean ecgDataInfoBean = EcgDataManager.parseEcgDataFile(BaseApplication.getInstance(), ecgDataFilePath, false);
                    short[][] ecgDataArrayAll = new short[0][];
                    //上传成功后 心电数据文件的本地路径会修改 所以发现EcgDataInfoBean是空的话 重新获取本地心电数据路径
                    if (ecgDataInfoBean != null) {
                        ecgDataArrayAll = ecgDataInfoBean.getEcgDataArray();
                    } else {
                        //重新获取本地心电数据路径
                        patientCaseBean = PatientCaseDaoLocal.getItemByPatientIdAndSerialNumber(patientInfoBean.getPatientId(),patientInfoBean.getPatientNumber());
                        ecgDataInfoBean = EcgDataManager.parseEcgDataFile(BaseApplication.getInstance(), patientCaseBean.getEcgDataPath(), false);
                        ecgDataArrayAll = ecgDataInfoBean.getEcgDataArray();
                    }
                    //ecgDataInfoBean和ecgDataArrayAll都不为空 可以打印
                    if (ecgDataInfoBean!=null&&ecgDataArrayAll!=null){
                        ecgDataInfoBean.setEcgDataArray(ecgDataArrayAll);
                        PrintManager.getInstance().printLaser(context, configBean, ecgDataInfoBean, null, usbLaserPrintCallback,
                                patientInfoBean, macureResultBean, checkTime,
                                true, true, 0, analysisResultPath);
                    }

                }
            } else {
                if (!ifCycleTask) {
                    callBackCommon.callBackEnd(null);
                }else {
                    callBackCommon.callBackProgress(context.getString(R.string.main_text_cycle_sample_ing));
                }
            }
        }

        boolean isEmergencyUseMode = LoginManager.getInstance().isEmergencyUseMode();

        if (isEmergencyUseMode) {
            //紧急使用模式
            //跳到预览页面
            MacureResultBean tempMacureResultBean = macureResultBean;
            BaseApplication.commonHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new PreviewManager.IntentBuilder()
                            .previewPageEnum(PreviewPageEnum.PAGE_PREVIEW)
                            .setCollectFinishJump(true)
                            .patientInfo(patientInfoBean)
                            .macureResult(tempMacureResultBean)
                            .ecgDataFilePath(ecgDataFilePathTemp)
                            .readConfigFromEcgSetting(configBean.getEcgSettingBean())
                            .previewDataDbId(-1)
                            .checkTimeStamp(System.currentTimeMillis())
                            .useGainArray(MainEcgManager.getInstance().getGainArray())
                            .startActivity(ActivityManager.getInstance().currentActivity());
                }
            }, 1000);
        }

        sampleTaskDoWithIng = false;


    }
}

