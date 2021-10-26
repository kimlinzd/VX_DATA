package com.lepu.algorithm.ecg.entity;

import android.os.Parcel;
import android.os.Parcelable;


import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;

import java.io.Serializable;

public class EcgWorkModeBean implements Parcelable, Serializable {
    //是否选中
    private boolean value;
    private EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType;

    public EcgWorkModeBean(boolean value, EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType) {
       this.value = value;
       this.leadWorkModeType = leadWorkModeType;
    }

    protected EcgWorkModeBean(Parcel in) {
        value = in.readByte() != 0;
        leadWorkModeType = EcgSettingConfigEnum.LeadWorkModeType.values()[in.readInt()];
    }

    public static final Creator<EcgWorkModeBean> CREATOR = new Creator<EcgWorkModeBean>() {
        @Override
        public EcgWorkModeBean createFromParcel(Parcel in) {
            return new EcgWorkModeBean(in);
        }

        @Override
        public EcgWorkModeBean[] newArray(int size) {
            return new EcgWorkModeBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (value ? 1 : 0));
        dest.writeInt(leadWorkModeType.ordinal());
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public EcgSettingConfigEnum.LeadWorkModeType getLeadWorkModeType() {
        return leadWorkModeType;
    }

    public void setLeadWorkModeType(EcgSettingConfigEnum.LeadWorkModeType leadWorkModeType) {
        this.leadWorkModeType = leadWorkModeType;
    }
}
