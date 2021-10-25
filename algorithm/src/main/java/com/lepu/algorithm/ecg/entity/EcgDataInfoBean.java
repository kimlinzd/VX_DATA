package com.lepu.algorithm.ecg.entity;

import android.util.Log;



/**
 * Liuxm
 * **/
import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;

/**
 * @author wxd
 */
public class EcgDataInfoBean {

    //这些不会保存到文件中,使用时，需要重新赋值当前的值
    private EcgSettingConfigEnum.LeadSpeedType leadSpeedType;
    private EcgSettingConfigEnum.LeadGainType leadGainType;
    float[] gainArray;

    float[] auto_gainArray;

    //这两个参数暂时用处不大。测试批量诊断数据时，可能使用
    private float ecgDataGain;//数据增益
    private float sampleRate;//采样频率 0.001 :1000  0.002 : 500

    //这些数据会保存到数据文件中
    private short[][] ecgDataArray;
    private EcgSettingConfigEnum.LeadType leadType;
    private EcgSettingConfigEnum.LeadFilterType filterAds;
    private EcgSettingConfigEnum.LeadFilterType filterEmg;
    private EcgSettingConfigEnum.LeadFilterType filterLowpass;
    private EcgSettingConfigEnum.LeadFilterType filterAc;//存的是50hz，60hz 值
    private EcgSettingConfigEnum.LeadFilterType filterAcOpenState;//存的是0,1 开关值

    private String patientId;
    private String patientSex;
    private String patientAge;
    private String patientName;

    public EcgDataInfoBean(){

    }

    /**
     *
     * @param ecgDataArray
     * @param gainArray
     * @param configBean
     * @return
     */
    public static EcgDataInfoBean makeEcgDataInfoBean(short[][] ecgDataArray, float[] gainArray, ConfigBean configBean){
        EcgSettingBean ecgSettingBean = configBean.getEcgSettingBean();
        SystemSettingBean systemSettingBean = configBean.getSystemSettingBean();

        EcgDataInfoBean ecgDataInfoBean = new EcgDataInfoBean();

        ecgDataInfoBean.setLeadSpeedType(ecgSettingBean.getLeadSpeedType());
        ecgDataInfoBean.setLeadGainType(ecgSettingBean.getLeadGainType());

        int leadLines = ecgSettingBean.getLeadShowStyleType().getOriginalLeadLines();
//        float[] auto_gainArray = MainEcgManager.getInstance().calculateAutoSensitivity(MainEcgManager.getInstance().getCalculateAutoSensitivityDataReal(), leadLines);
//
//        ecgDataInfoBean.setAuto_gainArray(auto_gainArray);

        ecgDataInfoBean.setGainArray(gainArray);

        ecgDataInfoBean.setEcgDataArray(ecgDataArray);
        ecgDataInfoBean.setLeadType(ecgSettingBean.getLeadType());
        ecgDataInfoBean.setFilterLowpass(ecgSettingBean.getFilterLowpass());
        ecgDataInfoBean.setFilterEmg(ecgSettingBean.getFilterEmg());
        ecgDataInfoBean.setFilterAds(ecgSettingBean.getFilterBaseline());
        ecgDataInfoBean.setFilterAc(systemSettingBean.getLeadFilterTypeAc());
        ecgDataInfoBean.setFilterAcOpenState(ecgSettingBean.getFilterAc());

        return ecgDataInfoBean;
    }

    public short[][] getEcgDataArray() {
        return ecgDataArray;
    }

    public void setEcgDataArray(short[][] ecgDataArray) {
        this.ecgDataArray = ecgDataArray;
    }

    public EcgSettingConfigEnum.LeadType getLeadType() {
        return leadType;
    }

    public void setLeadType(EcgSettingConfigEnum.LeadType leadType) {
        this.leadType = leadType;
    }

    public float[] getGainArray() {
        return gainArray;
    }

    public void setGainArray(float[] gainArray) {
        this.gainArray = gainArray;
    }

    public float[] getAuto_gainArray() {
        return auto_gainArray;
    }

    public void setAuto_gainArray(float[] auto_gainArray) {
        this.auto_gainArray = auto_gainArray;
    }

    public EcgSettingConfigEnum.LeadSpeedType getLeadSpeedType() {
        return leadSpeedType;
    }

    public void setLeadSpeedType(EcgSettingConfigEnum.LeadSpeedType leadSpeedType) {
        this.leadSpeedType = leadSpeedType;
    }

    public EcgSettingConfigEnum.LeadGainType getLeadGainType() {
        return leadGainType;
    }

    public void setLeadGainType(EcgSettingConfigEnum.LeadGainType leadGainType) {
        this.leadGainType = leadGainType;
    }

    public EcgSettingConfigEnum.LeadFilterType getFilterLowpass() {
        return filterLowpass;
    }

    public void setFilterLowpass(EcgSettingConfigEnum.LeadFilterType filterLowpass) {
        this.filterLowpass = filterLowpass;
    }

    public EcgSettingConfigEnum.LeadFilterType getFilterEmg() {
        return filterEmg;
    }

    public void setFilterEmg(EcgSettingConfigEnum.LeadFilterType filterEmg) {
        this.filterEmg = filterEmg;
    }

    public EcgSettingConfigEnum.LeadFilterType getFilterAds() {
        return filterAds;
    }

    public void setFilterAds(EcgSettingConfigEnum.LeadFilterType filterAds) {
        this.filterAds = filterAds;
    }

    public EcgSettingConfigEnum.LeadFilterType getFilterAc() {
        return filterAc;
    }

    public void setFilterAc(EcgSettingConfigEnum.LeadFilterType filterAc) {
        this.filterAc = filterAc;
    }

    public EcgSettingConfigEnum.LeadFilterType getFilterAcOpenState() {
        return filterAcOpenState;
    }

    public void setFilterAcOpenState(EcgSettingConfigEnum.LeadFilterType filterAcOpenState) {
        this.filterAcOpenState = filterAcOpenState;
    }

    public float getEcgDataGain() {
        return ecgDataGain;
    }

    public void setEcgDataGain(float ecgDataGain) {
        this.ecgDataGain = ecgDataGain;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
