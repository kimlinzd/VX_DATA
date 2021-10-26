package com.lepu.algorithm.ecg.entity;

import android.os.Parcel;
import android.os.Parcelable;


import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;

import java.io.Serializable;
import java.util.List;

public class EcgSettingBean implements Parcelable, Serializable {

    /**
     * 当前工作模式
     */
    private EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType;

    /**
     * 工作模式
     */
    private List<EcgWorkModeBean> workModeList;

    /**
     * 当前导联是几导联
     */
    private EcgSettingConfigEnum.LeadType leadType;

    /**
     * 导联展示格式 (自动记录格式)
     */
    private EcgSettingConfigEnum.LeadShowStyleType leadShowStyleType;


    /**
     * 电极名称
     */
    private EcgSettingConfigEnum.LeadElectrodeType leadElectrodeType;
    /**
     * 导联模式 标准
     */
    private EcgSettingConfigEnum.LeadModeType leadModeType;
    /**
     * 节律类型
     */
    private EcgSettingConfigEnum.LeadRhythmType leadRhythmType;
    /**
     * 节律导联1
     */
    private EcgSettingConfigEnum.LeadNameType rhythmLead1;
    /**
     * 节律导联2
     */
    private EcgSettingConfigEnum.LeadNameType rhythmLead2;
    /**
     * 节律导联3
     */
    private EcgSettingConfigEnum.LeadNameType rhythmLead3;
    /**
     * 基线漂移滤波器
     */
    private EcgSettingConfigEnum.LeadFilterType filterBaseline;
    /**
     * 肌电滤波器
     */
    private EcgSettingConfigEnum.LeadFilterType filterEmg;
    /**
     * 低通滤波器
     */
    private EcgSettingConfigEnum.LeadFilterType filterLowpass;
    /**
     * 交流滤波器 开关值
     */
    private EcgSettingConfigEnum.LeadFilterType filterAc;

    /**
     * 走纸速度
     */
    private EcgSettingConfigEnum.LeadSpeedType leadSpeedType;

    /**
     * 灵敏度
     */
    private EcgSettingConfigEnum.LeadGainType leadGainType;

    /**
     * 是否自动上传
     */
    private boolean autoUpload;

    /**
     * 是否自动存储
     */
    private boolean autoSave;

    /**
     * 心动过缓值
     */
    private int bradycardiaValue;

    /**
     * 心动过速
     */
    private int tachycardiaValue;

    /**
     * 预览
     */
    private boolean preview;

    public EcgSettingBean(){

    }

    protected EcgSettingBean(Parcel in) {

        leadWorkModeType = leadWorkModeType.values()[in.readInt()];
        workModeList = in.createTypedArrayList(EcgWorkModeBean.CREATOR);
        leadType = EcgSettingConfigEnum.LeadType.values()[in.readInt()];
        leadShowStyleType = EcgSettingConfigEnum.LeadShowStyleType.values()[in.readInt()];
        leadElectrodeType = EcgSettingConfigEnum.LeadElectrodeType.values()[in.readInt()];
        leadModeType = EcgSettingConfigEnum.LeadModeType.values()[in.readInt()];
        leadRhythmType = EcgSettingConfigEnum.LeadRhythmType.values()[in.readInt()];
        rhythmLead1 = EcgSettingConfigEnum.LeadNameType.values()[in.readInt()];
        rhythmLead2 = EcgSettingConfigEnum.LeadNameType.values()[in.readInt()];
        rhythmLead3 = EcgSettingConfigEnum.LeadNameType.values()[in.readInt()];
        filterBaseline = EcgSettingConfigEnum.LeadFilterType.values()[in.readInt()];
        filterEmg = EcgSettingConfigEnum.LeadFilterType.values()[in.readInt()];
        filterLowpass = EcgSettingConfigEnum.LeadFilterType.values()[in.readInt()];
        filterAc = EcgSettingConfigEnum.LeadFilterType.values()[in.readInt()];
        leadSpeedType = EcgSettingConfigEnum.LeadSpeedType.values()[in.readInt()];
        leadGainType = EcgSettingConfigEnum.LeadGainType.values()[in.readInt()];
        autoUpload = in.readByte() != 0;
        autoSave = in.readByte() != 0;
        bradycardiaValue = in.readInt();
        tachycardiaValue = in.readInt();
        preview = in.readByte() != 0;
    }

    public static final Creator<EcgSettingBean> CREATOR = new Creator<EcgSettingBean>() {
        @Override
        public EcgSettingBean createFromParcel(Parcel in) {
            return new EcgSettingBean(in);
        }

        @Override
        public EcgSettingBean[] newArray(int size) {
            return new EcgSettingBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(leadWorkModeType.ordinal());
        dest.writeTypedList(workModeList);
        dest.writeInt(leadType.ordinal());
        dest.writeInt(leadShowStyleType.ordinal());
        dest.writeInt(leadElectrodeType.ordinal());
        dest.writeInt(leadModeType.ordinal());
        dest.writeInt(leadRhythmType.ordinal());
        dest.writeInt(rhythmLead1.ordinal());
        dest.writeInt(rhythmLead2.ordinal());
        dest.writeInt(rhythmLead3.ordinal());
        dest.writeInt(filterBaseline.ordinal());
        dest.writeInt(filterEmg.ordinal());
        dest.writeInt(filterLowpass.ordinal());
        dest.writeInt(filterAc.ordinal());
        dest.writeInt(leadSpeedType.ordinal());
        dest.writeInt(leadGainType.ordinal());
        dest.writeByte((byte) (autoUpload ? 1 : 0));
        dest.writeByte((byte) (autoSave ? 1 : 0));
        dest.writeInt(bradycardiaValue);
        dest.writeInt(tachycardiaValue);
        dest.writeByte((byte) (preview ? 1 : 0));
    }

    public EcgSettingConfigEnum.LeadWorkModeType getLeadWorkModeType() {
        return leadWorkModeType;
    }

    public void setLeadWorkModeType(EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType) {
        this.leadWorkModeType = leadWorkModeType;
    }

    public List<EcgWorkModeBean> getWorkModeList() {
        return workModeList;
    }

    public void setWorkModeList(List<EcgWorkModeBean> workModeList) {
        this.workModeList = workModeList;
    }

    public EcgSettingConfigEnum.LeadType getLeadType() {
        return leadType;
    }

    public void setLeadType(EcgSettingConfigEnum.LeadType leadType) {
        this.leadType = leadType;
    }

    public EcgSettingConfigEnum.LeadElectrodeType getLeadElectrodeType() {
        return leadElectrodeType;
    }

    public void setLeadElectrodeType(EcgSettingConfigEnum.LeadElectrodeType leadElectrodeType) {
        this.leadElectrodeType = leadElectrodeType;
    }

    public EcgSettingConfigEnum.LeadModeType getLeadModeType() {
        return leadModeType;
    }

    public void setLeadModeType(EcgSettingConfigEnum.LeadModeType leadModeType) {
        this.leadModeType = leadModeType;
    }

    public EcgSettingConfigEnum.LeadRhythmType getLeadRhythmType() {
        return leadRhythmType;
    }

    public void setLeadRhythmType(EcgSettingConfigEnum.LeadRhythmType leadRhythmType) {
        this.leadRhythmType = leadRhythmType;
    }

    public EcgSettingConfigEnum.LeadNameType getRhythmLead1() {
        return rhythmLead1;
    }

    public void setRhythmLead1(EcgSettingConfigEnum.LeadNameType rhythmLead1) {
        this.rhythmLead1 = rhythmLead1;
    }

    public EcgSettingConfigEnum.LeadNameType getRhythmLead2() {
        return rhythmLead2;
    }

    public void setRhythmLead2(EcgSettingConfigEnum.LeadNameType rhythmLead2) {
        this.rhythmLead2 = rhythmLead2;
    }

    public EcgSettingConfigEnum.LeadNameType getRhythmLead3() {
        return rhythmLead3;
    }

    public void setRhythmLead3(EcgSettingConfigEnum.LeadNameType rhythmLead3) {
        this.rhythmLead3 = rhythmLead3;
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

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public int getBradycardiaValue() {
        return bradycardiaValue;
    }

    public void setBradycardiaValue(int bradycardiaValue) {
        this.bradycardiaValue = bradycardiaValue;
    }

    public int getTachycardiaValue() {
        return tachycardiaValue;
    }

    public void setTachycardiaValue(int tachycardiaValue) {
        this.tachycardiaValue = tachycardiaValue;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public EcgSettingConfigEnum.LeadShowStyleType getLeadShowStyleType() {
        return leadShowStyleType;
    }

    public void setLeadShowStyleType(EcgSettingConfigEnum.LeadShowStyleType leadShowStyleType) {
        this.leadShowStyleType = leadShowStyleType;
    }

    public EcgSettingConfigEnum.LeadFilterType getFilterBaseline() {
        return filterBaseline;
    }

    public void setFilterBaseline(EcgSettingConfigEnum.LeadFilterType filterBaseline) {
        this.filterBaseline = filterBaseline;
    }

    public EcgSettingConfigEnum.LeadFilterType getFilterEmg() {
        return filterEmg;
    }

    public void setFilterEmg(EcgSettingConfigEnum.LeadFilterType filterEmg) {
        this.filterEmg = filterEmg;
    }

    public EcgSettingConfigEnum.LeadFilterType getFilterLowpass() {
        return filterLowpass;
    }

    public void setFilterLowpass(EcgSettingConfigEnum.LeadFilterType filterLowpass) {
        this.filterLowpass = filterLowpass;
    }

    public EcgSettingConfigEnum.LeadFilterType getFilterAc() {
        return filterAc;
    }

    public void setFilterAc(EcgSettingConfigEnum.LeadFilterType filterAc) {
        this.filterAc = filterAc;
    }
}
