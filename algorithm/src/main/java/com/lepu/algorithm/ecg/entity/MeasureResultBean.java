package com.lepu.algorithm.ecg.entity.dictionary;

import android.os.Parcel;
import android.os.Parcelable;

public class MeasureResultBean implements Parcelable {

    //算法结果类型
    private EcgMacureResultEnum ecgMacureResultEnum;
    //RR分析，HRV分析结果
    private RRAnalysisResultBean rrAnalysisResultBean;
    //本地传统算法分析结果 / AI 本地算法结果
    private AiResultBean aiResultBean;

    public MeasureResultBean(){}

    protected MeasureResultBean(Parcel in) {
        ecgMacureResultEnum = EcgMacureResultEnum.values()[in.readInt()];
        rrAnalysisResultBean = in.readParcelable(RRAnalysisResultBean.class.getClassLoader());
        aiResultBean = in.readParcelable(AiResultBean.class.getClassLoader());
    }

    public static final Creator<MeasureResultBean> CREATOR = new Creator<MeasureResultBean>() {
        @Override
        public MeasureResultBean createFromParcel(Parcel in) {
            return new MeasureResultBean(in);
        }

        @Override
        public MeasureResultBean[] newArray(int size) {
            return new MeasureResultBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ecgMacureResultEnum.ordinal());
        dest.writeParcelable(rrAnalysisResultBean,flags);
        dest.writeParcelable(aiResultBean, flags);
    }

    public EcgMacureResultEnum getEcgMeasureResultEnum() {
        return ecgMacureResultEnum;
    }

    public void setEcgMeasureResultEnum(EcgMacureResultEnum ecgMacureResultEnum) {
        this.ecgMacureResultEnum = ecgMacureResultEnum;
    }

    public RRAnalysisResultBean getRrAnalysisResultBean() {
        return rrAnalysisResultBean;
    }

    public void setRrAnalysisResultBean(RRAnalysisResultBean rrAnalysisResultBean) {
        this.rrAnalysisResultBean = rrAnalysisResultBean;
    }

    public AiResultBean getAiResultBean() {
        return aiResultBean;
    }

    public void setAiResultBean(AiResultBean aiResultBean) {
        this.aiResultBean = aiResultBean;
    }
}
