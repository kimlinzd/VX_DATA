package com.lepu.algorithm.ecg.manager;

//import com.Carewell.OmniEcg.jni.JniFilterNew;
//import com.Carewell.OmniEcg.jni.JniHeartRateDetect;
//import com.Carewell.OmniEcg.jni.JniNoiseDetect;
//import com.Carewell.OmniEcg.jni.JniSample;
//import com.casecenter.entity.PatientInfoBean;
//import com.ecg.entity.EcgDataInfoBean;
//import com.ecg.entity.NotifyEcgDataBean;
//import com.ecg.entity.dictionary.LeadSignalEnum;
//import com.ecg.task.AutoCycleSampleTask;
//import com.ecg.task.AutoPreSampleTask;
//import com.ecg.task.AutoRealtimeSampleTask;
//import com.ecg.task.AutoTriggerSampleTask;
//import com.ecg.task.BaseSampleTask;
//import com.ecg.task.DrugTestSampleTask;
//import com.ecg.task.EcgEventSampleTask;
//import com.ecg.task.FrankSampleTask;
//import com.ecg.task.HRVSampleTask;
//import com.ecg.task.ManualSampleTask;
//import com.ecg.task.RRSampleTask;
//import com.lib.common.BaseApplication;
//import com.lib.common.util.FileUtil;
//import com.lib.common.util.log.KLog;
//import com.lib.common.utils.CallBackCommon;
//import com.lib.common.utils.DataTypeChangeHelper;
//import com.lib.common.utils.ObserverManager;
//import com.lib.common.utils.RootUtil;
//import com.lib.common.utils.SerializableUtils;
//import com.lib.common.utils.ThreadManager;
//import com.lib.common.utils.Util;
//import com.main.R;
//import com.main.entity.dictionary.MainTipEnum;
//import com.main.manager.ExceptionTipManager;
//import com.main.manager.MainEcgManager;
//import com.main.utils.Const;
//import com.main.utils.WaveEncodeUtil;
//import com.main.utils.XmlUtil;
//import com.setting.entity.ConfigBean;
//import com.setting.entity.dictionary.AppTypeEnum;
//import com.setting.entity.dictionary.EcgSettingConfigEnum;
//import com.setting.entity.dictionary.SystemSettingConfigEnum;
//import com.setting.manager.SettingManager;

import com.lepu.algorithm.ecg.utils.DataTypeChangeHelper;
import com.lepu.algorithm.ecg.utils.Util;

import java.util.LinkedList;

/**
 * 采集数据用
 */
public class DetectManager {
//    public static final String TAG = "DetectManager";
//    private static final int TIMER_GET_ECG_DATA_PERIOD = 100;//ms 定时获取数据的间隔   不能随便改这个时间
//
    private static DetectManager instance = null;
//
//    //全局context
//    private Context context;
//    //采集数据定时任务
//    private ScheduledExecutorService detectEcgDataTimer = null;
//    private ScheduledExecutorService dowithEcgDataTimer = null;
//    //演示模式
//    private short[][] demoWaveArrayAll;//演示模式中所有的数据
//    private short[][] demoWaveArray = null;//演示模式 每次去拷贝的数据
//    private int demoWaveArrayBeginPos = 0;//演示模式 每次拿完数据的位置
//    //采集模式，缓存的数据
//    private short[][] ecgDataArrayCache;//缓存的心电数据 ,计算的肢体导联数据不存储。方便cabrera模式转换，同时节省内存
//    private int ecgDataArrayCacheBeginPos = 0;//缓存的心电数据 记录的 位置
//    private boolean ecgDataArrayCacheRangeMax = false;//是否数据到头，重新开始
//
//    //取数据jni
//    private NotifyEcgDataBean notifyEcgDataSampleBean = new NotifyEcgDataBean();
//    //private NotifyEcgDataBean notifyEcgDataTempBean = new NotifyEcgDataBean();
//    //qrs声音同步
//    private long lastQrsSoundTime = 0L;
//    //当前心率值
//    private int heartRateValue = 0;
//    //心率未检测到
//    private boolean heartRateNotDetected = true;
//    //最新的导联脱落变化状态
//    private boolean lastLeadOffState = false;
    //V5R V4R V3R V9 V8	V7 V6	V5	V4	V3	V2	V1	F/LL L RL 反转位置
    public LinkedList<Boolean> leadStateList = new LinkedList<>();
//    //采集数据总时长
//    private long sampleDataDuration = 0L;
//    //采集任务
//    private BaseSampleTask baseSampleTask;
//    private LinkedBlockingQueue<short[][]> ecgDataQuene = new LinkedBlockingQueue<>();
//    //干扰检测复位
//    private boolean noiseDetectReset = true;
//    private LeadSignalEnum leadSignalEnumCurrent = LeadSignalEnum.NOISE_NORMAL;
//    private int resetSampleCount = 0;

    private DetectManager() {

    }

    public static DetectManager getInstance() {
        if (instance == null) {
            instance = new DetectManager();
        }
        return instance;
    }
//
//    /**
//     * 采集数据初始化
//     *
//     * @param context
//     */
//    public void init(Context context) {
//        this.context = context;
//
//        initEcgDataArrayCache();
//
//        //初始化演示数据
//        SystemSettingConfigEnum.DemoMode demoMode = MainEcgManager.getInstance().getConfigBeanTemp().getSystemSettingBean().getDemoMode();
//        updateDemoWaveData(demoMode);
//    }
//
//    /**
//     * 启动采集任务
//     */
//    public void startSample() {
//        JniSample.getInstance().sampleStart();
//        startGetEcgData();
//    }
//
//    /**
//     * 停止采集任务
//     */
//    public void stopSample() {
//        stopGetEcgData();
//        JniSample.getInstance().sampleStop();
//    }
//
//    /**
//     * 采集销毁
//     */
//    public void destroy() {
//        Const.Cjbupgrade_flag = true;
//        stopSample();
//        //初始化在appstart中，主页面关闭，下面不销毁，只停止采集
//        clearEcgDataArrayCache();
//        clearDemoWaveData();
//        JniHeartRateDetect.getInstance().closeHeartRateDetect();
//
//        instance = null;
//    }
//
//    //======================================
//    /**
//     * 开始定时获取心电图数据
//     */
//    private void startGetEcgData() {
//        if (detectEcgDataTimer == null) {
//            detectEcgDataTimer = new ScheduledThreadPoolExecutor(1);
//            detectEcgDataTimer.scheduleAtFixedRate(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        detectEcgDataTask();
//                        sampleDataDuration += TIMER_GET_ECG_DATA_PERIOD;
//                    }catch (Exception e){
//                        //KLog.e(Log.getStackTraceString(e));
//                    }
//                }
//            }, 10, TIMER_GET_ECG_DATA_PERIOD, TimeUnit.MILLISECONDS);//150
//        }
//
//        if (dowithEcgDataTimer == null) {
//            dowithEcgDataTimer = new ScheduledThreadPoolExecutor(1);
//            dowithEcgDataTimer.scheduleAtFixedRate(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        dowithEcgDataTask();
//                    } catch (Exception e) {
//                       // KLog.e(Log.getStackTraceString(e));
//                    }
//                }
//            }, 10, TIMER_GET_ECG_DATA_PERIOD, TimeUnit.MILLISECONDS);//150
//        }
//    }
//
//    /**
//     * 停止获取心电图数据
//     */
//    private void stopGetEcgData() {
//        if (detectEcgDataTimer != null) {
//            try {
//                // shutdown只是起到通知的作用
//                // 只调用shutdown方法结束线程池是不够的
//                detectEcgDataTimer.shutdown();
//                // (所有的任务都结束的时候，返回TRUE)
//                if (!detectEcgDataTimer.awaitTermination(0, TimeUnit.MILLISECONDS)) {
//                    // 超时的时候向线程池中所有的线程发出中断(interrupted)。
//                    detectEcgDataTimer.shutdownNow();
//                }
//            } catch (InterruptedException e) {
//                // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
//                //KLog.e(Log.getStackTraceString(e));
//            } finally {
//                detectEcgDataTimer.shutdownNow();
//                detectEcgDataTimer = null;
//            }
//        }
//
//        if (dowithEcgDataTimer != null) {
//            try {
//                // shutdown只是起到通知的作用
//                // 只调用shutdown方法结束线程池是不够的
//                dowithEcgDataTimer.shutdown();
//                // (所有的任务都结束的时候，返回TRUE)
//                if (!dowithEcgDataTimer.awaitTermination(0, TimeUnit.MILLISECONDS)) {
//                    // 超时的时候向线程池中所有的线程发出中断(interrupted)。
//                    dowithEcgDataTimer.shutdownNow();
//                }
//            } catch (InterruptedException e) {
//                // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
//               // KLog.e(Log.getStackTraceString(e));
//            } finally {
//                dowithEcgDataTimer.shutdownNow();
//                dowithEcgDataTimer = null;
//            }
//        }
//    }
//
//    /**
//     * 处理定时任务
//     */
//    private void detectEcgDataTask() {
//        short[][] ecgDataArrayOri;
//        ConfigBean configBeanTemp = MainEcgManager.getInstance().getConfigBeanTemp();
//        //任何模式都要去采集数据，否则会积累数据
//        int monitorAddLeadNum = 0;
//        if(Const.SUPPORT_ALL_LEAD && Const.appTypeEnum == AppTypeEnum.SUPPORT_LEAD_18_MONITOR){
//            //支持所有导联，并且是12导联设备。需要模拟几个导联出来
//            monitorAddLeadNum = 6;
//        }
//
//        int retCode = JniSample.getInstance().getSampleData(notifyEcgDataSampleBean,monitorAddLeadNum);
//        //检查导联脱落
//        short currentLeadOffValue = notifyEcgDataSampleBean.getLeadOffFlag();
//        lastLeadOffState = checkLeadOffState(configBeanTemp,currentLeadOffValue);
//
//        boolean isDemoMode = SettingManager.getInstance().isDemoMode();
//        SystemSettingConfigEnum.DemoMode  demomode = configBeanTemp.getSystemSettingBean().getDemoMode();
//        if (isDemoMode) {
//            //演示模式
//            ecgDataArrayOri = getDemoWaveData();
//        } else {
//            //采集模式
//            if(retCode < 0){
//                if(resetSampleCount > 15){
//                    if(!Const.Cjbupgrade_flag) {
//                        JniSample.getInstance().sampleStart();
//                    }
////                    KLog.d(TAG, String.format("采集数据收到的长度异常，重置次数:%d", resetSampleCount-15));
//                }  else {
//                    resetSampleCount++;
//                }
//                return;
//            }
//            resetSampleCount = 0;
//            ecgDataArrayOri = notifyEcgDataSampleBean.getLeadEcgBuf();
//            short[][] finalEcgDataArrayOri = ecgDataArrayOri;
////            ThreadManager.execute(new Runnable() {
////                @Override
////                public void run() {
////                    for (int i = 0; i < finalEcgDataArrayOri.length; i++) {
////                        short[] dataByte = finalEcgDataArrayOri[1];
////                        String dataString=   Arrays.toString(dataByte);
////                        Date d = new Date();
////                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                        String path = Environment.getExternalStoragePublicDirectory("") + "/lepu/demoData1.txt";
////                        FileUtil.saveTextFile(path,dataString,true);
////                        FileUtil.saveTextFile(path,sdf.format(d)+"当前导联是第"+i+"导联"+dataString+"\r\n",true);
////                    }
////                }
////            });
//
//            if (ecgDataArrayOri == null) {
//                return ;
//            }
//            //数据预处理；L/F导联脱落，肢体4个导联不计算，改为0；胸导联也都改为0
//            ecgDataArrayOri = preDowithData(ecgDataArrayOri);
//            //KLog.d(TAG,String.format("采集点数:%d",ecgDataArrayOri[0].length));
//        }
//
//        if (ecgDataArrayOri == null) {
//            return ;
//        }
//
////        sampleDataDuration += TIMER_GET_ECG_DATA_PERIOD;
//
//        boolean addPacemaker = SettingManager.getInstance().checkIfAddPaceFlagCurrentPatient();
//        if(addPacemaker){
//            //是否抹掉起搏脉冲
//            ecgDataArrayOri = WaveEncodeUtil.modifyPacePluse(ecgDataArrayOri,ecgDataArrayOri.length-Const.LEAD_ADD_EXTRA_COUNT,
//                    ecgDataArrayOri[ecgDataArrayOri.length-1], ecgDataArrayOri[ecgDataArrayOri.length-2], (short) (0),false);
//        }
//
//        //在这里滤波.导联脱落通道，起搏通道，不滤波，所以-2
//        short[][] ecgDataArrayOriFilter;
//        if(Const.FILTER_ON){
//            //起搏通道也走滤波，导联脱落通道不滤波
//            ecgDataArrayOriFilter = FilterManager.getInstance().filter(configBeanTemp,ecgDataArrayOri,
//                    ecgDataArrayOri.length-2,leadStateList);
////            ThreadManager.execute(new Runnable() {
////                @Override
////                public void run() {
////                    for (int i = 0; i <ecgDataArrayOriFilter.length; i++) {
////                        short[] dataByte = ecgDataArrayOriFilter[1];
////                        String dataString=   Arrays.toString(dataByte);
////                        Date d = new Date();
////                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                        String path = Environment.getExternalStoragePublicDirectory("") + "/lepu/demoData.txt";
////                        FileUtil.saveTextFile(path,dataString,true);
////                        FileUtil.saveTextFile(path,sdf.format(d)+"当前导联是第"+i+"导联"+dataString+"\r\n",true);
////                    }
////                }
////            });
//
//        }else{
//            ecgDataArrayOriFilter = ecgDataArrayOri;
//        }
//        //往内存中拷贝数据
//        copyEcgDataArrayCache(ecgDataArrayOriFilter);
//
//        //添加采集的数据，另外一个线程，实时保存数据使用
//        if(Const.SAMPLE_SAVE_SRC_DATA){
//            //实时采集保存数据中,保存原始数据
//            if((baseSampleTask != null && baseSampleTask.isSampleDataStateIng())){
//                ecgDataQuene.add(ecgDataArrayOri);
//            }
//        }else{
//            //保存滤波后的数据
//            if((baseSampleTask != null && baseSampleTask.isSampleDataStateIng())){
//                ecgDataQuene.add(ecgDataArrayOriFilter);
//            }
//        }
//
//        //冻结波形了，不绘图等
//        if (MainEcgManager.getInstance().isFreezeWave()) {
//            return;
//        }
//
//        //实时计算自动增益,
////        if(configBeanTemp.getEcgSettingBean().getLeadGainType() == EcgSettingConfigEnum.LeadGainType.GAIN_AUTO){
////            MainEcgManager.getInstance().realCalculateAutoSensitivity();
////        }
//
//        //画图 添加实时绘图数据
//        //不同的导联模式，转成了不同导联的数据了
//        if(configBeanTemp.getEcgSettingBean().getLeadWorkModeType() == EcgSettingConfigEnum.LeadWorkModeType.WORK_MODE_FRANK){
//            short[][] ecgDataArrayTemp = WaveEncodeUtil.leadDataSwitchXYZ(ecgDataArrayOriFilter);
//            MainEcgManager.getInstance().addEcgData(ecgDataArrayTemp);
//        }else{
//            boolean cabreraMode = SettingManager.getInstance().checkLeadSortCabrera();
//            if(cabreraMode){
//                short[][] ecgDataArrayTemp = WaveEncodeUtil.leadDataSwitch(ecgDataArrayOriFilter,cabreraMode, configBeanTemp.getEcgSettingBean().getLeadType(),addPacemaker);
//                MainEcgManager.getInstance().addEcgData(ecgDataArrayTemp);
//            }else{
//                //转换后的正常所有数据,添加计算导联数据
//                short[][] ecgDataArrayTemp = WaveEncodeUtil.leadDataSwitch(ecgDataArrayOriFilter,false, configBeanTemp.getEcgSettingBean().getLeadType(),addPacemaker);
//                MainEcgManager.getInstance().addEcgData(ecgDataArrayTemp);
//            }
//        }
//
//        //计算心率和干扰检测，需要自动把起搏脉冲信号抹平，否则计算心率不准确.. 这么改了，有效果，但是还是会跳动。先注释
//        //short[][] ecgDataArrayTemp = WaveEncodeUtil.modifyPacePluse(ecgDataArrayOriFilter,ecgDataArrayOriFilter.length-Const.LEAD_ADD_EXTRA_COUNT,ecgDataArrayOriFilter[ecgDataArrayOriFilter.length-2],ecgDataArrayOriFilter[ecgDataArrayOriFilter.length-1], (short) (0),false);
//        //short[][] ecgDataArrayFilterAll = WaveEncodeUtil.leadDataSwitch(ecgDataArrayTemp,false, configBeanTemp.getEcgSettingBean().getLeadType(),false);
//
//        short[][] ecgDataArrayFilterAll = WaveEncodeUtil.leadDataSwitch(ecgDataArrayOriFilter,false, configBeanTemp.getEcgSettingBean().getLeadType(),false);
//
//        //检查干扰信号,信号检测算法
//        if(lastLeadOffState){
//            //导联脱落，不检查信号检测
//            leadSignalEnumCurrent = LeadSignalEnum.NOISE_LEADOFF;
//            noiseDetectReset = true;
//        }else{
//            //干扰检测
//            leadSignalEnumCurrent = checkNoiseDetect(ecgDataArrayFilterAll);
//        }
//
//        switch (leadSignalEnumCurrent){
//            case NOISE_NORMAL:
//            case NOISE_LEADOFF:
//                ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_SIGNAL_AC_BAD);
//                ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_SIGNAL_EMG_BAD);
//                ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_SIGNAL_BASELINE_BAD);
//                break;
//            case NOISE_50HZ60HZ:
//                ExceptionTipManager.getInstance().addTip(MainTipEnum.LEAD_SIGNAL_AC_BAD);
//                ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_SIGNAL_EMG_BAD);
//                ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_SIGNAL_BASELINE_BAD);
//                break;
//            case NOISE_EMG:
//                ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_SIGNAL_AC_BAD);
//                ExceptionTipManager.getInstance().addTip(MainTipEnum.LEAD_SIGNAL_EMG_BAD);
//                ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_SIGNAL_BASELINE_BAD);
//                break;
//            case NOISE_BASELINE:
//                ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_SIGNAL_AC_BAD);
//                ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_SIGNAL_EMG_BAD);
//                ExceptionTipManager.getInstance().addTip(MainTipEnum.LEAD_SIGNAL_BASELINE_BAD);
//                break;
//            default:
//                break;
//        }
//
//        //计算心率
//        int rthIndex1 = MainEcgManager.getInstance().getRthDataIndex(configBeanTemp.getEcgSettingBean().getRhythmLead1());
//        int[] heartArray = JniHeartRateDetect.getInstance().getDataHeartRate(ecgDataArrayFilterAll[rthIndex1]);
//        heartRateValue = heartArray[0];
//        int qrsPosLen = heartArray[1];
//        //KLog.d(String.format("采集计算实时心率值：%d,qrsposlen =%d",heartRateValue,qrsPosLen));
//
//        //异常心率检查
//        if (lastLeadOffState && heartRateValue == 0) { // 导联脱落
//            heartRateValue = -2;
//            leadSignalEnumCurrent = LeadSignalEnum.NOISE_LEADOFF;
//        } else {
//            if (heartRateValue != 0 && (heartRateValue < 30 || heartRateValue > 300)) { //异常心率，超过范围
//                heartRateValue = -1;
//            }else if(heartRateValue == 0){
//                //停博为 0
//                //停博，暂时不需要需要声音响
////                long currentTime = System.currentTimeMillis();
////                long time = currentTime - lastQrsSoundTime;
////                if (time > 500) {
////                    SettingManager.getInstance().playEcgHeartBeep();
////                    lastQrsSoundTime = System.currentTimeMillis();
////                }
//                ObserverManager.getInstance().notifyAsync(Const.Observer.NOTIFY_MAIN_HEART_RATE_UPDATE, null, heartRateValue);
//            }else{
//                //正常心率
//                //qrs 监测到，声音
//                //int maxRate = configBeanTemp.getEcgSettingBean().getTachycardiaValue();
//                //int minRate = configBeanTemp.getEcgSettingBean().getBradycardiaValue();
//
//                //heartRateValue >= minRate && heartRateValue <= maxRate &&
//                //演示模式，qrs同步音，定时响
//                if (isDemoMode) {
//                    //判断是什么演示模式
//                    if(demomode == SystemSettingConfigEnum.DemoMode.DEMOMODE_NORMALE)
//                    {
//                        long currentTime = System.currentTimeMillis();
//                        long time = currentTime - lastQrsSoundTime;
//                        if (time > 1000) {
//                            SettingManager.getInstance().playEcgHeartBeep();
//                            lastQrsSoundTime = System.currentTimeMillis();
//                            ObserverManager.getInstance().notifyAsync(Const.Observer.NOTIFY_MAIN_HEART_RATE_UPDATE, null, heartRateValue);
//                        }
//                    }else if(demomode == SystemSettingConfigEnum.DemoMode.DEMOMODE_EXCEPTION)
//                    {
//                        long currentTime = System.currentTimeMillis();
//                        long time = currentTime - lastQrsSoundTime;
//                        if (time > 500) {
//                            SettingManager.getInstance().playEcgHeartBeep();
//                            lastQrsSoundTime = System.currentTimeMillis();
//                            ObserverManager.getInstance().notifyAsync(Const.Observer.NOTIFY_MAIN_HEART_RATE_UPDATE, null, heartRateValue);
//                        }
//                    }
//                }else {
//                    if (notifyEcgDataSampleBean != null && qrsPosLen > 0) {
//                        long currentTime = System.currentTimeMillis();
//                        long time = currentTime - lastQrsSoundTime;
//                        int extraValue = 0;
//                        if(heartRateValue > 120){
//                            extraValue = 50;
//                        }
//
//                        if (time > extraValue) {
//                            SettingManager.getInstance().playEcgHeartBeep();
//                            lastQrsSoundTime = System.currentTimeMillis();
//                            // 正常心率：更新心率，播放同频率的心跳音和动画
//                            ObserverManager.getInstance().notifyAsync(Const.Observer.NOTIFY_MAIN_HEART_RATE_UPDATE, null, heartRateValue);
//                            //KLog.d("qrs sound");
//                        }
//                    }
//                }
//            }
//        }
//
//
//        //导联异常心率，导联信号状态，导联脱落状态 通知 每隔1秒通知一次
//        if (sampleDataDuration % 1000 == 0) {
//            List<Boolean> leadStateListTemp = new ArrayList<>(leadStateList);
//            if (0 >  heartRateValue) { // 异常心率：更新心率值
//                ObserverManager.getInstance().notifyAsync(Const.Observer.NOTIFY_MAIN_HEART_RATE_UPDATE, null, heartRateValue);
//            }
//            ObserverManager.getInstance().notifyAsync(Const.Observer.NOTIFY_MAIN_LEAD_SIGNAL_STATE_UPDATE, null, leadSignalEnumCurrent.ordinal());
//            ObserverManager.getInstance().notifyAsync(Const.Observer.NOTIFY_MAIN_LEAD_OFF_STATE_UPDATE, null, leadStateListTemp);
//        }
//
////        Utils.startCalculateTime();
////        long endTimeAll = SystemClock.elapsedRealtimeNanos();
////        float timeAll = (endTimeAll - startTime) / 1000000F;
////        KLog.d(TAG,String.format("detect get all cost time %.1f ms",timeAll));
//    }
//
//
//
//    private void  getDatatoSdCard() {
//        String destDirPath = Environment.getExternalStorageDirectory().getPath() + "//文件夹//";
//
//        File file = new File(destDirPath);
//        if (!file.exists()) {
//            try {
//                file.mkdirs();
//            } catch (Exception e) {
//            }
//
//        }
//    }
//
//    /**
//     * 处理心电数据
//     */
//    public void dowithEcgDataTask() throws InterruptedException {
//        if(ecgDataQuene.size() == 0){
//            return;
//        }
//
//        if(ecgDataQuene.size() > 1){
//            //KLog.d(String.format("心电数据队列长度超过1个，有了延迟:%d",ecgDataQuene.size()));
//        }
//
//        //long startTime = SystemClock.elapsedRealtimeNanos();
//
//        short[][] ecgDataArray = ecgDataQuene.take();
//        //向采集任务添加数据
//        addSampleTaskData(ecgDataArray,heartRateValue);
//
////        long endTime = SystemClock.elapsedRealtimeNanos();
////        float time = (endTime - startTime) / 1000000F;
////        KLog.d(String.format("dowith task cost time %.1f s",time));
//    }
//
//    /**
//     * 初始化演示数据
//     * 演示模式，只存1份18导联的数据，按需要获取
//     */
//    public void initDemoWaveData(SystemSettingConfigEnum.DemoMode demoMode) {
//        //需要的演示数据
//        int leadNum = SettingManager.getInstance().getCurrentNeedLeadNum();
//        demoWaveArray = new short[leadNum][TIMER_GET_ECG_DATA_PERIOD];
//
//        //加载演示文件数据，所有导联的数据
//        InputStream inputStream = null;
//        try {
//            String value = (String) demoMode.getValue();
//            inputStream = context.getAssets().open(String.format("demo_ecg_data/wave_%s.xml", value));
//            EcgDataInfoBean ecgDataInfoBean = XmlUtil.getEcgDataFromXml(context, inputStream, false,leadNum);
//            if (ecgDataInfoBean != null) {
//                demoWaveArrayAll = ecgDataInfoBean.getEcgDataArray();
//            }
//        } catch (Exception e) {
//            KLog.e(Log.getStackTraceString(e));
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    KLog.e(Log.getStackTraceString(e));
//                }
//            }
//        }
//    }
//
//    /**l
//     * 清理演示数据
//     */
//    public void clearDemoWaveData() {
//        demoWaveArrayAll = null;
//        demoWaveArray = null;
//        demoWaveArrayBeginPos = 0;
//    }
//
//    /**
//     * 更新演示模式数据
//     *
//     * @param demoMode
//     */
//    public void updateDemoWaveData(SystemSettingConfigEnum.DemoMode demoMode) {
//        if(demoMode.ordinal() > SystemSettingConfigEnum.DemoMode.DEMOMODE_CLOSE.ordinal()){
//            clearDemoWaveData();
//            initDemoWaveData(demoMode);
//        }
//    }
//
//    /**
//     * 获取当前演示模式的数据
//     *
//     * @return
//     */
//    public short[][] getDemoWaveData() {
//        try {
//            if (demoWaveArrayAll != null) {
//                int perDataLen = 0;
//                //拿数据到头了，从开始位置再拿
//                if (demoWaveArrayBeginPos >= demoWaveArrayAll[0].length - 1) {
//                    demoWaveArrayBeginPos = 0;
//                }
//
//                perDataLen = demoWaveArray[0].length;
//                for (int i = 0; i < demoWaveArray.length; i++) {
//                    System.arraycopy(demoWaveArrayAll[i], demoWaveArrayBeginPos, demoWaveArray[i], 0, perDataLen);
//                }
//                demoWaveArrayBeginPos += perDataLen;
//            }
//        } catch (Exception e) {
//            KLog.e(Log.getStackTraceString(e));
//            demoWaveArrayBeginPos = 0;
//        }
//
//        return demoWaveArray;
//    }
//
//    /**
//     * 初始化内存中的数据
//     */
//    public void initEcgDataArrayCache() {
//        if (ecgDataArrayCache == null) {
//            int leadNum = SettingManager.getInstance().getCurrentNeedLeadNum();
//
//            ecgDataArrayCache = new short[leadNum][Const.SAMPLE_RATE * Const.CACHE_DATA_SECOND_DATA];
//            ecgDataArrayCacheRangeMax = false;
//        }
//    }
//
//    /**
//     * 清理内存中的数据
//     * 自动 预采样模式 周期采样模式
//     */
//    public void clearEcgDataArrayCache() {
//        ecgDataArrayCache = null;
//        ecgDataArrayCacheBeginPos = 0;
//    }
//
//    /**
//     * 130秒数据拷贝
//     * 拷贝数据
//     *
//     * @param dataArray
//     */
//    private void copyEcgDataArrayCache(short[][] dataArray) {
//        if (ecgDataArrayCache != null) {
//            int perDataLen = 0;
//            boolean rangeMax = false;
//            int rangeMaxIndex = 0;
//
//            boolean paceAdd = false;
//
//            for (int i = 0; i < dataArray.length; i++) {
//                perDataLen = dataArray[i].length;//每次读取数据的长度
//                if (ecgDataArrayCacheBeginPos + perDataLen > ecgDataArrayCache[0].length) {
//                    int perDataLastCount = ecgDataArrayCacheBeginPos + perDataLen - ecgDataArrayCache[0].length;//拷贝数据到头部
//                    int perDataPreCount = perDataLen - perDataLastCount;//拷贝数据到尾部
//
//                    System.arraycopy(dataArray[i], perDataPreCount, ecgDataArrayCache[i], 0,
//                            perDataLastCount);
//                    System.arraycopy(dataArray[i], 0, ecgDataArrayCache[i], ecgDataArrayCacheBeginPos,
//                            perDataPreCount);
//
//                    rangeMax = true;
//                    rangeMaxIndex = perDataLastCount;
//                } else {
//                    System.arraycopy(dataArray[i], 0, ecgDataArrayCache[i], ecgDataArrayCacheBeginPos, perDataLen);
//                }
//                paceAdd = true;
//            }
//            if (rangeMax) {
//                ecgDataArrayCacheBeginPos = 0;
//                ecgDataArrayCacheBeginPos += rangeMaxIndex;
//                ecgDataArrayCacheRangeMax = true;
//            } else {
//                ecgDataArrayCacheBeginPos += perDataLen;
//            }
//        }
//    }
//
//    /**
//     * 检查当前采集的数据是否超过10s
//     *
//     * @return
//     */
//    public boolean checkDetectDataMax10Second() {
//        if (sampleDataDuration < Const.PER_SCREEN_DATA_SECOND * Const.SAMPLE_RATE) {
//            //采集的数据小于10秒，不返回
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 返回当前循环缓冲的数据
//     *
//     * @return
//     */
//    public short[][] getEcgDataArrayCache() {
//        int simpleDataLen = ecgDataArrayCache[0].length;
//        short[][] ecgDestDataArray;
//        short[][] ecgDataArrayTemp = ecgDataArrayCache;
//        int beginPos = ecgDataArrayCacheBeginPos;
//
//        if (!ecgDataArrayCacheRangeMax) {
//            ecgDestDataArray = new short[ecgDataArrayCache.length][beginPos];
//
//            for (int i = 0; i < ecgDestDataArray.length; i++) {
//                System.arraycopy(ecgDataArrayTemp[i], 0, ecgDestDataArray[i], 0, beginPos);
//            }
//        } else {
//            ecgDestDataArray = new short[ecgDataArrayCache.length][simpleDataLen];
//
//            int lastDataLen = 0;
//            int preDataLen = 0;
//
//            for (int i = 0; i < ecgDataArrayTemp.length; i++) {
//                //把后面的数据，放到新数组的最前面，因为数据是老的
//                lastDataLen = ecgDataArrayTemp[i].length - beginPos;
//                if (lastDataLen > 0) {
//                    System.arraycopy(ecgDataArrayTemp[i], beginPos, ecgDestDataArray[i], 0, lastDataLen);
//                }
//
//                //把前面的数据，放到新数组的最后面，因为数据是最新的
//                preDataLen = beginPos;
//                if (preDataLen > 0) {
//                    System.arraycopy(ecgDataArrayTemp[i], 0, ecgDestDataArray[i], lastDataLen, preDataLen);
//                }
//            }
//        }
//
//        return ecgDestDataArray;
//    }
//
//    /**
//     * 获取最后几秒数据
//     * @param needDataLen
//     * @return
//     */
//    public short[][] getEcgDataArrayCacheLastData(int needDataLen) {
//        short[][] allDataArray = getEcgDataArrayCache();
//
//        if(allDataArray[0].length < needDataLen){
//            return null;
//        }
//
//        short[][] ecgDataArray = new short[allDataArray.length][needDataLen];
//        for(int i=0;i<ecgDataArray.length;i++){
//            System.arraycopy(allDataArray[i],allDataArray[0].length-needDataLen,ecgDataArray[i],0,needDataLen);
//        }
//        return ecgDataArray;
//    }
//
//    public void sampleInit() {
//        //初始化sample so
//        //采集串口
//        String sampleSerialPath = "";
//        //spi 打印
//        String printSpiSerialPath = "";
//
//        try {
//            if(Const.SAMPLE_SERIAL_SAMPLE){
//                sampleSerialPath = "/dev/ttyS0";
//            }else{
//                if(Const.BIG_SCRREN){
//                    sampleSerialPath = "/dev/ttyS1";
//                }else{
//                    sampleSerialPath = "/dev/ttyS0";
//                }
//            }
//
//
//            printSpiSerialPath = "/dev/spidev1.0";
//
//            //系统也给了666权限.老版本系统，没有给权限。
//            RootUtil.requestPermission(sampleSerialPath);
//            RootUtil.requestPermission(printSpiSerialPath);
//        } catch (Exception e) {
////            KLog.e(Log.getStackTraceString(e));
//        }
//
//        //经讨论，不使用采集板，基线漂移
//        //byte filterPiaoyiValue = getFilterPiaoyiValue(EcgSettingConfigEnum.LeadFilterType.FILTER_BASELINE_067);
//        //这里初始化，只采集8,14通道数据。getSampleData返回上来的数据包括起搏数据
//        int leadNum = (int) Const.appTypeEnum.getValue();
//        JniSample.getInstance().sampleInit(0, (byte) 0, sampleSerialPath, "", printSpiSerialPath,
//                leadNum,460800,115200,Const.HEART_RATE_DETECT_VALUE);
//
//        if(Const.SAMPLE_SERIAL_SAMPLE){
//            if(Const.SUPPORT_THERMAL_PRINT && Const.THERMAL_PRINT_DEVICE_SMARTECG && Const.BIG_SCRREN){
//                //使用smart 热敏打印机，初始化串口
//                JniSample.getInstance().samplePrinterInit("/dev/ttyS1",921600);
//            }else{
//                //test c120 走spi
//            }
//        }else{
//            if(Const.SUPPORT_THERMAL_PRINT && Const.THERMAL_PRINT_DEVICE_SMARTECG && Const.BIG_SCRREN){
//                //使用smart 热敏打印机，初始化串口
//                JniSample.getInstance().samplePrinterInit("/dev/ttyS0",921600);
//            }else{
//                //test c120 走spi
//            }
//        }
//
//        JniNoiseDetect.getInstance().initNoiseDetection();
//        JniHeartRateDetect.getInstance().initHeartRateDetect(Const.SAMPLE_RATE,Const.HEART_RATE_DETECT_VALUE);
//        //初始化滤波
//        JniFilterNew.getInstance().InitDCRecover(0);
//    }
//
//    /**
//     * 基线漂移滤波
//     *
//     */
//    public void updateFilterPiaoyi(EcgSettingConfigEnum.LeadFilterType leadFilterTypeBaseline) {
//        byte value = getFilterPiaoyiValue(leadFilterTypeBaseline);
//        JniSample.getInstance().updateFilterPiaoyi(value);
//    }
//
//    /**
//     * 获取基线漂移滤波值
//     *
//     * @return
//     */
//    public byte getFilterPiaoyiValue(EcgSettingConfigEnum.LeadFilterType leadFilterTypeBaseline) {
//
//        byte value;
//        switch (leadFilterTypeBaseline) {
//            case FILTER_BASELINE_001:
//                //0.01hz
//                value = (byte) 0xD2;
//                break;
//            case FILTER_BASELINE_005:
//                //0.05hz
//                value = (byte) 0xF0;
//                break;
//            case FILTER_BASELINE_032:
//                //0.32hz
//                value = (byte) 0xE1;
//                break;
//            case FILTER_BASELINE_067:
//                //0.67hz
//                value = (byte) 0xC3;
//                break;
//            default:
//                //默认0.01hz
//                value = (byte) 0xD2;
//                break;
//        }
//        return value;
//    }
//
//    public int getEcgDataArrayCacheBeginPos() {
//        return ecgDataArrayCacheBeginPos;
//    }
//
//    public void setEcgDataArrayCacheBeginPos(int ecgDataArrayCacheBeginPos) {
//        this.ecgDataArrayCacheBeginPos = ecgDataArrayCacheBeginPos;
//    }
//
    /**
     * 解析导联脱落
     * @param leadOffValue
     * @return  顺序 L F V1-V5R
     */
    public static char[] getLeadOffArray(short leadOffValue){
        //自己增加了RL RA
        //V5R V4R V3R V9 V8	V7 V6	V5	V4	V3	V2	V1	F/LL L 反转位置
        //1为脱落，0为不脱落，当全为1时，说明所有导联都脱落，包括R在内
        byte[] tempArray = DataTypeChangeHelper.shortToByteArray(leadOffValue, true);
        String flag0 = Util.toBinaryString(tempArray[0]);
        String flag1 = Util.toBinaryString(tempArray[1]);
        StringBuilder sb = new StringBuilder();
        sb.append(flag0).reverse();
        sb.append(new StringBuilder(flag1).reverse());
        char[] leadoffArray = sb.toString().toCharArray();

        return leadoffArray;
    }
//
//    /**
//     * 检查导联脱落状态
//     *
//     * @return
//     */
//    public boolean checkLeadOffState(ConfigBean configBeanTemp,short leadOffValue) {
//        leadStateList.clear();
//
//        char[] leadoffArray = getLeadOffArray(leadOffValue);
//
//        //KLog.d(String.format("leadoff==>%s",sb.toString()));
//
//        boolean lead18Device = Const.appTypeEnum == AppTypeEnum.SUPPORT_LEAD_18;
//        int leadStateNumV = lead18Device ? 12 : 6;
//
//        boolean leadoffTemp = false;
//        char item = '1';
//        StringBuilder sbLeadOff = new StringBuilder();
//        String space = " ";
//        boolean isAHA = SettingManager.getInstance().checkLeadNameAHA();
//        String leadName = "C%d";
//        if (isAHA) {
//            leadName = "V%d";
//        }
//
//        //肢体导联
//        for (int i = 0; i < 4; i++) {
//            if (i == 0) {
//                //LA
//                item = leadoffArray[0];
//
//                if (item == '1'){
//                    sbLeadOff.append(isAHA ? "LA":"L").append(space);
//                }
//            } else if (i == 1) {
//                //LL
//                item = leadoffArray[1];
//
//                if (item == '1'){
//                    sbLeadOff.append(isAHA ? "LL":"F").append(space);
//                }
//            } else {
//                //RA RL
//                if (lead18Device) {
//                    if (leadoffArray[0] == '1' && leadoffArray[1] == '1' && leadoffArray[2] == '1' && leadoffArray[3] == '1' &&
//                            leadoffArray[4] == '1' && leadoffArray[5] == '1' && leadoffArray[6] == '1' && leadoffArray[7] == '1' &&
//                            leadoffArray[8] == '1' && leadoffArray[9] == '1' && leadoffArray[10] == '1' && leadoffArray[11] == '1' &&
//                            leadoffArray[12] == '1' && leadoffArray[13] == '1') {
//                        item = '1';
//                    } else {
//                        item = '0';
//                    }
//                } else {
//                    if (leadoffArray[0] == '1' && leadoffArray[1] == '1' && leadoffArray[2] == '1' && leadoffArray[3] == '1' &&
//                            leadoffArray[4] == '1' && leadoffArray[5] == '1' && leadoffArray[6] == '1' && leadoffArray[7] == '1') {
//                        item = '1';
//                    } else {
//                        item = '0';
//                    }
//                }
//
//                if (item == '1'){
//                    if(i == 2){
//                        sbLeadOff.append(isAHA ? "RA":"R").append(space);
//                    }else{
//                        sbLeadOff.append(isAHA ? "RL":"N").append(space);
//                    }
//                }
//            }
//
//            if (item == '0') {
//                //正常
//                leadoffTemp = false;
//            } else {
//                //脱落
//                leadoffTemp = true;
//            }
//            leadStateList.add(leadoffTemp);
//        }
//
//        //v1-v5R   v1-v6
//        for (int i = 0; i < leadStateNumV; i++) {
//            item = leadoffArray[i + 2];
//
//            if (item == '0') {
//                //正常
//                leadoffTemp = false;
//            } else {
//                //脱落
//                leadoffTemp = true;
//            }
//
//            switch (configBeanTemp.getEcgSettingBean().getLeadType()){
//                case LEAD_9:
//                    //v1 v3 v5
//                    if(i == 0 || i== 2 || i == 4){
//                        leadStateList.add(leadoffTemp);
//
//                        if(leadoffTemp){
//                            sbLeadOff.append(String.format(leadName,i+1)).append(space);
//                        }
//                    }
//                    break;
//                case LEAD_12:
//                    //v1-v6
//                    if(i < 6){
//                        leadStateList.add(leadoffTemp);
//
//                        if(leadoffTemp){
//                            sbLeadOff.append(String.format(leadName,i+1)).append(space);
//                        }
//                    }
//                    break;
//                case LEAD_15_STANDARD_RIGHT:
//                    //v1-v6 (v7-v9 不要)V3R V4R V5R
//                    if(i < 6 || i > 8){//old:7 wuxd modify
//                        leadStateList.add(leadoffTemp);
//
//                        if(leadoffTemp){
//                            if(i < 6){
//                                sbLeadOff.append(String.format(leadName,i+1)).append(space);
//                            }else{
//                                sbLeadOff.append(String.format(leadName+"R",i-6)).append(space);
//                            }
//                        }
//                    }
//                    break;
//                case LEAD_15_STANDARD_AFTER:
//                    //V7 V8 V9
//                    if(i < 9){
//                        leadStateList.add(leadoffTemp);
//
//                        if(leadoffTemp){
//                            sbLeadOff.append(String.format(leadName,i+1)).append(space);
//                        }
//                    }
//                    break;
//                case LEAD_15_CHILD:
//                    //V3R V4R V7
//                    if(i < 7 || (i > 8 && i < 11)){
//                        leadStateList.add(leadoffTemp);
//
//                        if(leadoffTemp){
//                            if(i < 6){
//                                //V7 在下面处理.这里到v6
//                                sbLeadOff.append(String.format(leadName,i+1)).append(space);
//                            }else if(i > 8 && i < 11){
//                                sbLeadOff.append(String.format(leadName+"R",i-6)).append(space);
//                            }
//                        }
//                    }
//                    break;
//                case LEAD_18:
//                    if(i < 12){
//                        leadStateList.add(leadoffTemp);
//
//                        if(leadoffTemp){
//                            if(i < 9){
//                                sbLeadOff.append(String.format(leadName,i+1)).append(space);
//                            }else{
//                                sbLeadOff.append(String.format(leadName+"R",i-6)).append(space);
//                            }
//                        }
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        //变换顺序
//        if(configBeanTemp.getEcgSettingBean().getLeadType() == EcgSettingConfigEnum.LeadType.LEAD_15_CHILD){
//            boolean v7Flag = leadStateList.get(10);
//            leadStateList.remove(10);
//            leadStateList.add(10,v7Flag);
//
//            if(v7Flag){
//                sbLeadOff.append(isAHA? "V7" : "C7").append(space);
//            }
//        }
//
//        boolean leadOffSave = leadStateList.contains(true);
//
//        if (leadOffSave) {
//            if(leadStateList.contains(false)){
//                //个别导联脱落
//                String msg = BaseApplication.getInstance().getString(R.string.main_tip_leadoff_custom);
//                String message = String.format(msg,sbLeadOff.toString());
//                ExceptionTipManager.getInstance().addTip(MainTipEnum.LEAD_OFF,message);
//            }else{
//                //全部导联脱落
//                ExceptionTipManager.getInstance().addTip(MainTipEnum.LEAD_OFF);
//            }
//
//            if (lastLeadOffState != leadOffSave) {
//                //没有冻结波形了，才响
//                if (!MainEcgManager.getInstance().isFreezeWave()) {
//                    SettingManager.getInstance().playLeadoff();
//                }
//            }
//        } else {
//            ExceptionTipManager.getInstance().removeTip(MainTipEnum.LEAD_OFF);
//        }
//        return leadOffSave;
//    }
//
//    public boolean isHeartRateNotDetected() {
//        return heartRateNotDetected;
//    }
//
//    public void setHeartRateNotDetected(boolean heartRateNotDetected) {
//        this.heartRateNotDetected = heartRateNotDetected;
//    }
//
//    /**
//     * 干扰信号检测
//     * @param ecgDataArray 转换后的所有导联数据，9，12，15，18导联数据
//     * @return
//     */
//    public LeadSignalEnum checkNoiseDetect(short[][] ecgDataArray) {
//        int noiseStateNormal = (int) LeadSignalEnum.NOISE_NORMAL.getValue();
//        int noiseStateValue = 0;
//        short value;
//        char noiseResetFlag = '0';
//
//        for(int i=0;i<ecgDataArray.length;i++){
//            for (int j = 0; j < ecgDataArray[i].length; j++) {
//                value = (ecgDataArray[i][j]);
//                noiseStateValue = JniNoiseDetect.getInstance().noiseDetection(value, (short) i, noiseResetFlag);
//
//                if (noiseStateValue != noiseStateNormal) {
//                    //KLog.d(String.format("干扰信号 %s", LeadSignalEnum.getLeadSignalEnumByValue(noiseStateValue).getName()));
//                    break;
//                }
//            }
//        }
//        noiseDetectReset = false;
//        return LeadSignalEnum.getLeadSignalEnumByValue(noiseStateValue);
//    }
//
//    /**
//     * 启动采集任务
//     */
//    public void startSampleTask(Context context, ConfigBean configBeanTemp, PatientInfoBean patientInfoBean,EcgDataInfoBean ecgDataInfoBean, CallBackCommon callBackCommon){
//        if(baseSampleTask == null){
//            ConfigBean configBean = (ConfigBean) SerializableUtils.copy(configBeanTemp);
//            EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType = configBean.getEcgSettingBean().getLeadWorkModeType();
//            switch (leadWorkModeType){
//                case WORK_MODE_MANUAL:
//                    baseSampleTask = new ManualSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//                case WORK_MODE_SAMPLE_PRE:
//                    baseSampleTask = new AutoPreSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//                case WORK_MODE_SAMPLE_PERIODIC:
//                    baseSampleTask = new AutoCycleSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//                case WORK_MODE_SAMPLE_TRIGGER:
//                    baseSampleTask = new AutoTriggerSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//                case WORK_MODE_RR:
//                    baseSampleTask = new RRSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//                case WORK_MODE_HRV:
//                    baseSampleTask = new HRVSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//                case WORK_MODE_ECG_EVENT:
//                    baseSampleTask = new EcgEventSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//                case WORK_MODE_DRUG_TEST:
//                    baseSampleTask = new DrugTestSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//                case WORK_MODE_FRANK:
//                    baseSampleTask = new FrankSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//                case WORK_MODE_SAMPLE_REALTIME:
//                default:
//                    baseSampleTask = new AutoRealtimeSampleTask(context,configBean,patientInfoBean,ecgDataInfoBean);
//                    break;
//            }
//            baseSampleTask.setCallBackCommon(callBackCommon);
//            baseSampleTask.initSampleTask();
//            baseSampleTask.startSampleTask();
//        }
//    }
//
//    /**
//     * 停止采集任务
//     */
//    public boolean stopSampleTask(boolean cancelTask){
//        if(baseSampleTask != null){
//            boolean flag = baseSampleTask.stopSampleTask(cancelTask);
//            if(flag){
//                baseSampleTask = null;
//                if(ecgDataQuene != null && ecgDataArrayCache.length > 0){
//                    ecgDataQuene.clear();
//                }
//            }
//            return flag;
//        }else{
//            return true;
//        }
//    }
//
//    /**
//     * 任务中添加数据
//     * @param ecgDataArray
//     */
//    public void addSampleTaskData(short[][] ecgDataArray,int heartRateValue){
//        //实时采集保存数据中
//        if(baseSampleTask != null && baseSampleTask.isSampleDataStateIng()){
//            baseSampleTask.addSampleTaskData(ecgDataArray,heartRateValue);
//        }
//    }
//
//    /**
//     * 采集任务中
//     * @return
//     */
//    public boolean isSampleTaskIng(){
//        if(baseSampleTask != null && baseSampleTask.isRunStateIng()){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    /**
//     * 分析打印中
//     * @return
//     */
//    public boolean isSampleTaskDoWithIng(){
//        if(baseSampleTask != null && baseSampleTask.isSampleTaskDoWithIng()){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    public long getSampleDataDuration() {
//        return sampleDataDuration;
//    }
//
//    public void setSampleDataDuration(long sampleDataDuration) {
//        this.sampleDataDuration = sampleDataDuration;
//    }
//
//    /**
//     * 检查是否全部导联脱落
//     * @return
//     */
//    public boolean checkIfAllLeadOff(){
//        boolean flag = true;
//
//        try{
//            if(leadStateList != null && leadStateList.size() > 0){
//                for(Boolean item : leadStateList){
//                    if(!item){
//                        flag = false;
//                        break;
//                    }
//                }
//            }
//        }catch (Exception e){
//            KLog.e(Log.getStackTraceString(e));
//        }
//
//        return flag;
//    }
//
//    public LeadSignalEnum getLeadSignalEnumCurrent() {
//        return leadSignalEnumCurrent;
//    }
//
//    public void setLeadSignalEnumCurrent(LeadSignalEnum leadSignalEnumCurrent) {
//        this.leadSignalEnumCurrent = leadSignalEnumCurrent;
//    }
//
//    public int getHeartRateValue() {
//        return heartRateValue;
//    }
//
//    public void setHeartRateValue(int heartRateValue) {
//        this.heartRateValue = heartRateValue;
//    }
//
//    /**
//     * 预处理数据
//     * @param ecgDataArray
//     * @return
//     */
//    private short[][] preDowithData(short[][] ecgDataArray){
//        boolean leadoffL = leadStateList.get(0);//true 脱落
//        boolean leadoffF = leadStateList.get(1);
//        boolean leadoffR = leadStateList.get(2);
//        boolean leadoffRL = leadStateList.get(3);
//
//        int dataCount = ecgDataArray[0].length;
//        short[] emptyDataArray = new short[dataCount];
//        if(leadoffR || (leadoffL && leadoffF)){//|| leadoffRL
//            //全波导联，数据都改为0
//            for(int i=0;i<ecgDataArray.length-Const.LEAD_ADD_EXTRA_COUNT;i++){
//                System.arraycopy(emptyDataArray,0,ecgDataArray[i],0,emptyDataArray.length);
//            }
//        }else if(leadoffL){
//            //L脱落 I
//            //除了导联II，数据都改为0
//            for(int i=0;i<ecgDataArray.length-Const.LEAD_ADD_EXTRA_COUNT;i++){
//                if(i == 1){
//                    continue;
//                }
//                System.arraycopy(emptyDataArray,0,ecgDataArray[i],0,emptyDataArray.length);
//            }
//        }else if(leadoffF){
//            //F脱落 II
//            //除了导联I，数据都改为0
//            for(int i=0;i<ecgDataArray.length-Const.LEAD_ADD_EXTRA_COUNT;i++){
//                if(i == 0){
//                    continue;
//                }
//                System.arraycopy(emptyDataArray,0,ecgDataArray[i],0,emptyDataArray.length);
//            }
//        }else{
//            //
//        }
//
//        return ecgDataArray;
//    }
}

