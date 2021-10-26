package com.lepu.algorithm.ecg.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by wxd on 2019-02-21.
 */
public class ConfigBean implements Parcelable, Serializable {

    private EcgSettingBean ecgSettingBean;
//    private PatientInfoSettingBean patientInfoSettingBean;
//    private SampleSettingBean sampleSettingBean;
//    private RecordSettingBean recordSettingBean;
//    private CommunSettingBean communSettingBean;
    private SystemSettingBean systemSettingBean;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ecgSettingBean, flags);
//        dest.writeParcelable(this.patientInfoSettingBean, flags);
//        dest.writeParcelable(this.sampleSettingBean, flags);
//        dest.writeParcelable(this.recordSettingBean, flags);
//        dest.writeParcelable(this.communSettingBean, flags);
        dest.writeParcelable((Parcelable) this.systemSettingBean, flags);
    }

    public ConfigBean() {
    }

    protected ConfigBean(Parcel in) {
        this.ecgSettingBean = in.readParcelable(EcgSettingBean.class.getClassLoader());
//        this.patientInfoSettingBean = in.readParcelable(PatientInfoSettingBean.class.getClassLoader());
//        this.sampleSettingBean = in.readParcelable(SampleSettingBean.class.getClassLoader());
//        this.recordSettingBean = in.readParcelable(RecordSettingBean.class.getClassLoader());
//        this.communSettingBean = in.readParcelable(CommunSettingBean.class.getClassLoader());
        this.systemSettingBean = in.readParcelable(SystemSettingBean.class.getClassLoader());
    }

    public static final Creator<ConfigBean> CREATOR = new Creator<ConfigBean>() {
        @Override
        public ConfigBean createFromParcel(Parcel source) {
            return new ConfigBean(source);
        }

        @Override
        public ConfigBean[] newArray(int size) {
            return new ConfigBean[size];
        }
    };

    public EcgSettingBean getEcgSettingBean() {
        return ecgSettingBean;
    }

//    public void setEcgSettingBean(EcgSettingBean ecgSettingBean) {
//        this.ecgSettingBean = ecgSettingBean;
//    }
//
//    public PatientInfoSettingBean getPatientInfoSettingBean() {
//        return patientInfoSettingBean;
//    }
//
//    public void setPatientInfoSettingBean(PatientInfoSettingBean patientInfoSettingBean) {
//        this.patientInfoSettingBean = patientInfoSettingBean;
//    }

//    public SampleSettingBean getSampleSettingBean() {
//        return sampleSettingBean;
//    }
//
//    public void setSampleSettingBean(SampleSettingBean sampleSettingBean) {
//        this.sampleSettingBean = sampleSettingBean;
//    }
//
//    public RecordSettingBean getRecordSettingBean() {
//        return recordSettingBean;
//    }
//
//    public void setRecordSettingBean(RecordSettingBean recordSettingBean) {
//        this.recordSettingBean = recordSettingBean;
//    }
//
//    public CommunSettingBean getCommunSettingBean() {
//        return communSettingBean;
//    }
//
//    public void setCommunSettingBean(CommunSettingBean communSettingBean) {
//        this.communSettingBean = communSettingBean;
//    }

    public SystemSettingBean getSystemSettingBean() {
        return systemSettingBean;
    }

    public void setSystemSettingBean(SystemSettingBean systemSettingBean) {
        this.systemSettingBean = systemSettingBean;
    }

}