package com.lepu.algorithm.Carewell.OmniEcg.jni;

import android.graphics.Bitmap;

import com.lepu.algorithm.ecg.entity.NotifyEcgDataBean;
import com.lepu.algorithm.ecg.entity.NotifyFilterBean;
import com.lepu.algorithm.ecg.entity.RRAnalysisResultBean;

/**
 * Created by wxd
 * @author wxd
 */

public class JniSample {

    private static JniSample instance = null;

    //0 B120;1 C120
    private int filterType = 1;

    static {
        System.loadLibrary("ecg_sample");
    }

    private JniSample() {

    }

    public static JniSample getInstance() {
        if (instance == null) {
            instance = new JniSample();
        }
        return instance;
    }

    //==================================================sample

    /**
     *
     * @param outputData 外部输出 0：开启输出；1 关闭输出
     * @param piaoyiValue
     * @param sampleSerialPath
     * @param aiSerialPath
     * @param printSpiPath
     * @param leadNumCount 10 / 16
     * @param detectSpeedRate 心电采集板波特率  c120 : 460800。SmartEcg:921600
     * @param aiSpeedRate ai波特率
     */
    public native void sampleInit(int outputData,byte piaoyiValue,String sampleSerialPath,String aiSerialPath,String printSpiPath,
                                  int leadNumCount,int detectSpeedRate,int aiSpeedRate,int thresh_heartRate);

    /**
     * 反初始化
     */
    public native void sampleDeInit();

    /**
     * 启动串口工作等
     */
    public native void sampleStart();

    /**
     * 停止底层工作
     */
    public native void sampleStop();

    /**
     * 获取采样数据
     * @param notifyEcgDataBean
     * @return
     */
    public native int getSampleData(NotifyEcgDataBean notifyEcgDataBean, int monitorAddLeadNum);


    /*add Gavin
     */
     public native int stopSerialCollection();

    public native int SerialDataOverview(char page);

    public native int SerialSendUpgradeFile(char pagenum, char data[]);

    public native int queryUpgrade();


    /**
     * 起搏检测
     * @param notifyEcgDataBean
     * @return
     */
    public native void pacemakerCheck(NotifyEcgDataBean notifyEcgDataBean);

    /**
     * AI分析数据
     * @param ipAddress
     * @param port
     * @param dataArray
     * @return
     */
    public native byte[] aiAnalysisData(String ipAddress,int port,byte[] dataArray);

    //=====================================================
    /**
     * 更新滤波 漂移
     */
    public native void updateFilterPiaoyi(byte piaoyiValue);

    /**
     * print data
     * @param ecgDataArray
     */
    public native byte[] printData(byte[] ecgDataArray);

    /**
     * 获取rr分析的数据
     * @param rrAnalysisResultBean 返回结果对象
     * @param RR_interval RR间期  这个数据可以趋势图   R的个数=总心波数
     * @param RR_interval RR间期  (r(i+1) - r(i))*samplerate/samplerate
     * @param RR_interval_size RR间期长度
     * @param sample 采样率
     * @return
     */
    public native int getRRAnalysisData(RRAnalysisResultBean rrAnalysisResultBean, double[] RR_interval, int RR_interval_size, int sample);

    /**
     * 获取心率等信息 演示模式
     * @param notifyEcgDataBean
     * @return
     */
    public native int getDataHeartRate(NotifyEcgDataBean notifyEcgDataBean, int rthIndex);

    /**
     *
     * @param utime 睡眠单位微妙 = 毫秒*1000
     */
    public native void usleep(int utime);

    /**
     * 获取图片数据
     * @param bitmap
     * @param readInd
     * @param readNum
     * @param dataArray
     * @return
     */
    public native int processBitmap(Bitmap bitmap, int readInd , int readNum, byte[] dataArray);

    /**
     * 初始化gpio
     * @param pinPrintKey
     * @param pinPowerSupply
     * @return
     */
    public native void initGpio(int pinPrintKey,int pinPowerSupply,int pinDC);

    /**
     *  0-->LOW, 1-->HIGH
     * @return
     */
    public native int getRunSampleKeyGpio(int pinPrintKey);

    /**
     *
     * @param pinPowerSupply
     * @return
     */
    public native int getPowerSupplyGpio(int pinPowerSupply);

    /**
     * 释放gpio pin
     * @param pinPrintKey
     * @param pinPowerSupply
     * @return
     */
    public native void releaseGpio(int pinPrintKey,int pinPowerSupply,int pinDC);

    /**
     *
     * @param powerStateCmd
     *  0: wake
     *  1: sleep
     * @return
     */
    public native int sendDataToAiBySerial(int powerStateCmd);

    /**
     * 播放蜂鸣器
     * @return
     */
    public native void playBuzzer();

    /**
     * 获取电量值
     * @return
     */
    public native int getBatteryValue(int powerState);

    /**
     * spi 初始化
     */
    public native void sampleDeinitSPI(String printSpiPath);

    /**
     *
     * @param pinDC
     * @return
     */
    public native int getPowerDcState(int pinDC);

    //==================================================交流 工频
    public native void powerFrequency(short[][] ecgDataArray, int dataLen, NotifyFilterBean notifyFilterBean, double freq, int sampleRate);

    //===========================肌电 低通  这个滤波开了，就不调用低通了。肌电和低通是一种滤波   传的数据不能小于400.前后各200个点，中间取多少传多少
    public native void lowPass(short[][] ecgDataArray, int dataLen, NotifyFilterBean notifyFilterBean, double freq, int sampleRate);

    //==================高通
    public native void highPass(short[][] ecgDataArray, int dataLen, NotifyFilterBean notifyFilterBean, double freq, int sampleRate);

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    /**
     * 0 success
     * other fail
     * @param ipAddress
     * @param port
     * @return
     */
    public native int checkRndisNet(String ipAddress,int port);

//    /**
//     * 生成scp文件
//     * 官方浏览工具，只支持12导联，15导联的2种模式。
//     * 目前scp只支持导出12导联
//     * @param filePath
//     * @param scpBean
//     */
//    public native void writeEcgScp(String filePath, ScpBean scpBean);

    /**
     * 初始化干扰检测
     */
    public native void initNoiseDetection();

    /**
     * 噪声类型signed int
     * #define NOISE_NORMAL    0   //无噪声
     * #define NOISE_50HZ60HZ  1   //工频噪声
     * #define NOISE_EMG       2   //肌电噪声
     * #define NOISE_BASELINE  4   //基线漂移
     * 干扰检测
     * @param data
     * @param channel
     * @param reset 传0
     * @return
     */
    public native int noiseDetection(short data,short channel,char reset);

    /**
     * 发送采样数据，正常频率
     */
    public native void sendSampleFrequencyNormal();

    /**
     * 发送采样数据，高频率
     */
    public native void sendSampleFrequencyHigh();

    /**
     * 开始保存串口读到的数据
     */
    public native void startSaveSerialData();

    /**
     * 停止保存串口读到的数据
     */
    public native void stopSaveSerialData();

    /**
     * 查询采集板是几导联的 暂时还不工作
     *
     * @return <= 0 无效; 10 : 8导联采集板；16：18导联采集板
     */
    public native int getQueryResult();

    /**
     * smart 热敏打印初始化
     * @param PrinterSerialPath
     * @param PrinterSpeedRate
     */
    public native void samplePrinterInit(String PrinterSerialPath,int PrinterSpeedRate);

    /**
     * smart 热敏打印close
     */
    public native void samplePrinterDeInit();

    /**
     * smart 热敏打印 压缩数据
     * @param dataArray
     * @param column
     * @return
     */
    public native byte[] PrinterEcode(byte[] dataArray, int column);

    /**
     * smart 热敏打印 发送数据
     * @param cmdStr
     * @param cmdLen
     * @return
     */
    public native byte[] PrinterSerialCMD(byte[] cmdStr, int cmdLen);

    /**
     * test
     * @return
     */
    public native byte[] testPrinterSerialCMD();




}
