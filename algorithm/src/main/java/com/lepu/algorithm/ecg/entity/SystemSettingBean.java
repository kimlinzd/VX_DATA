package com.lepu.algorithm.ecg.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 系统设置
 */
public class SystemSettingBean{

//    /**
//     * 语言设置
//     */
//    private SystemSettingConfigEnum.LanguageMode languageMode;
//    /**
//     * 演示模式
//     */
//    private SystemSettingConfigEnum.DemoMode demoMode;
//    /**
//     * 诊断模式
//     */
//    private SystemSettingConfigEnum.DiagnosticMode diagnosticMode;
//    /**
//     * 亮度
//     */
//    private int brightness;
//    /**
//     * 声音
//     */
//    private int volume;
//    /**
//     * 按键音开关
//     */
//    private boolean keyTone;
//    /**
//     * QRS同步音
//     */
//    private boolean QRSSyncTone;
//    /**
//     * 低电量提示音开关
//     */
//    private boolean lowBatteryTone;
//    /**
//     * 脱落脱落提示音
//     */
//    private boolean leadOffTone;
//    /**
//     * 打印完成提示音
//     */
//    private boolean printOverTone;
//    /**
//     * 自动待机时长
//     */
//    private SystemSettingConfigEnum.AutoLockScreenMode autoLockScreenMode;
//    /**
//     * 日期格式
//     */
//    private SystemSettingConfigEnum.DateFormatMode dateFormatMode;
//
//    /**
//     * 时间格式
//     */
//    private SystemSettingConfigEnum.TimeFormatMode timeFormatMode;
//    /**
//     * 机构名称
//     */
//    private String organizationName;

    /**
     * 交流频率 50,60hz值
     */
    private EcgSettingConfigEnum.LeadFilterType leadFilterTypeAc;

//    /**
//     * 打印输出开关
//     */
//    private boolean printOn;
//
//    /**
//     * 检查患者编号，上传数据时，需要配置到服务器
//     */
//    private String systemTechnician;
//
//    /**
//     * 采样数据频率
//     */
//    private EcgSettingConfigEnum.LeadSampleDataFrequency leadSampleDataFrequency;
//
//    /**
//     * glasgow算法
//     */
//    public static boolean useGlasgow;
//    /**
//     * 登录的历史用户
//     */
//    private HashMap<String, LoginInfoBean> historicalUsers;
//    /**
//     * 二维码设置
//     */
//    private List<PatientQrCodeSettingBean> patientQrCodeSettings;
//
//    /**
//     * 性别编码
//     *
//     * @return
//     */
//    private String manCode;
//    private String womanCode;
//    /**
//     * 二维码样本内容
//     */
//    private String codeSampleContent;
//
//    /**
//     * CardioT服务器IP
//     */
//    private String cardioTServerIP;
//    /**
//     * CardioT服务器端口
//     */
//    private String cardioTServerPort;

    public EcgSettingConfigEnum.LeadFilterType getLeadFilterTypeAc() {
        return leadFilterTypeAc;
    }

//    public SystemSettingConfigEnum.LanguageMode getLanguageMode() {
//        return languageMode;
//    }
//
//    public void setLanguageMode(SystemSettingConfigEnum.LanguageMode languageMode) {
//        this.languageMode = languageMode;
//    }
//
//    public SystemSettingConfigEnum.DemoMode getDemoMode() {
//        return demoMode;
//    }
//
//    public void setDemoMode(SystemSettingConfigEnum.DemoMode demoMode) {
//        this.demoMode = demoMode;
//    }
//
//    public SystemSettingConfigEnum.DiagnosticMode getDiagnosticMode() {
//        return diagnosticMode;
//    }
//
//    public void setDiagnosticMode(SystemSettingConfigEnum.DiagnosticMode diagnosticMode) {
//        this.diagnosticMode = diagnosticMode;
//    }

//    public int getBrightness() {
//        return brightness;
//    }
//
//    public void setBrightness(int brightness) {
//        this.brightness = brightness;
//    }
//
//    public int getVolume() {
//        return volume;
//    }
//
//    public void setVolume(int volume) {
//        this.volume = volume;
//    }
//
//    public boolean isKeyTone() {
//        return keyTone;
//    }
//
//    public void setKeyTone(boolean keyTone) {
//        this.keyTone = keyTone;
//    }
//
//    public boolean isQRSSyncTone() {
//        return QRSSyncTone;
//    }
//
//    public void setQRSSyncTone(boolean QRSSyncTone) {
//        this.QRSSyncTone = QRSSyncTone;
//    }
//
//    public boolean isLowBatteryTone() {
//        return lowBatteryTone;
//    }
//
//    public void setLowBatteryTone(boolean lowBatteryTone) {
//        this.lowBatteryTone = lowBatteryTone;
//    }
//
//    public boolean isLeadOffTone() {
//        return leadOffTone;
//    }
//
//    public void setLeadOffTone(boolean leadOffTone) {
//        this.leadOffTone = leadOffTone;
//    }
//
//    public boolean isPrintOverTone() {
//        return printOverTone;
//    }
//
//    public void setPrintOverTone(boolean printOverTone) {
//        this.printOverTone = printOverTone;
//    }
//
////    public SystemSettingConfigEnum.AutoLockScreenMode getAutoLockScreenMode() {
////        return autoLockScreenMode;
////    }
////
////    public void setAutoLockScreenMode(SystemSettingConfigEnum.AutoLockScreenMode autoLockScreenMode) {
////        this.autoLockScreenMode = autoLockScreenMode;
////    }
////
////    public SystemSettingConfigEnum.DateFormatMode getDateFormatMode() {
////        return dateFormatMode;
////    }
////
////    public void setDateFormatMode(SystemSettingConfigEnum.DateFormatMode dateFormatMode) {
////        this.dateFormatMode = dateFormatMode;
////    }
////
////    public SystemSettingConfigEnum.TimeFormatMode getTimeFormatMode() {
////        return timeFormatMode;
////    }
////
////    public void setTimeFormatMode(SystemSettingConfigEnum.TimeFormatMode timeFormatMode) {
////        this.timeFormatMode = timeFormatMode;
////    }
//
//    public String getOrganizationName() {
//        return organizationName;
//    }
//
//    public void setOrganizationName(String organizationName) {
//        this.organizationName = organizationName;
//    }
//
//
////
////    public void setLeadFilterTypeAc(EcgSettingConfigEnum.LeadFilterType leadFilterTypeAc) {
////        this.leadFilterTypeAc = leadFilterTypeAc;
////    }
//
//    public boolean isPrintOn() {
//        return printOn;
//    }
//
//    public void setPrintOn(boolean printOn) {
//        this.printOn = printOn;
//    }
//
//    public String getSystemTechnician() {
//        return systemTechnician;
//    }
//
//    public void setSystemTechnician(String systemTechnician) {
//        this.systemTechnician = systemTechnician;
//    }
//
////    public EcgSettingConfigEnum.LeadSampleDataFrequency getLeadSampleDataFrequency() {
////        return leadSampleDataFrequency;
////    }
////
////    public void setLeadSampleDataFrequency(EcgSettingConfigEnum.LeadSampleDataFrequency leadSampleDataFrequency) {
////        this.leadSampleDataFrequency = leadSampleDataFrequency;
////    }
//
//    public static boolean isUseGlasgow() {
//        return useGlasgow;
//    }
//
//    public static void setUseGlasgow(boolean use_Glasgow) {
//        useGlasgow = use_Glasgow;
//    }
//
//    public HashMap<String, LoginInfoBean> getHistoricalUsers() {
//        return historicalUsers;
//    }
//
//    public void setHistoricalUsers(HashMap<String, LoginInfoBean> historicalUsers) {
//        this.historicalUsers = historicalUsers;
//    }
//
//    public List<PatientQrCodeSettingBean> getPatientQrCodeSettings() {
//        return patientQrCodeSettings;
//    }
//
//    public void setPatientQrCodeSettings(List<PatientQrCodeSettingBean> patientQrCodeSettings) {
//        this.patientQrCodeSettings = patientQrCodeSettings;
//    }
//
//    public String getManCode() {
//        return manCode;
//    }
//
//    public void setManCode(String manCode) {
//        this.manCode = manCode;
//    }
//
//    public String getWomanCode() {
//        return womanCode;
//    }
//
//    public void setWomanCode(String womanCode) {
//        this.womanCode = womanCode;
//    }
//
//    public String getCodeSampleContent() {
//        return codeSampleContent;
//    }
//
//    public void setCodeSampleContent(String codeSampleContent) {
//        this.codeSampleContent = codeSampleContent;
//    }
//
//    public String getCardioTServerIP() {
//        return cardioTServerIP;
//    }
//
//    public void setCardioTServerIP(String cardioTServerIP) {
//        this.cardioTServerIP = cardioTServerIP;
//    }
//
//    public String getCardioTServerPort() {
//        return cardioTServerPort;
//    }
//
//    public void setCardioTServerPort(String cardioTServerPort) {
//        this.cardioTServerPort = cardioTServerPort;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.languageMode == null ? -1 : this.languageMode.ordinal());
//        dest.writeInt(this.demoMode == null ? -1 : this.demoMode.ordinal());
//        dest.writeInt(this.diagnosticMode == null ? -1 : this.diagnosticMode.ordinal());
//        dest.writeInt(this.brightness);
//        dest.writeInt(this.volume);
//        dest.writeByte(this.keyTone ? (byte) 1 : (byte) 0);
//        dest.writeByte(this.QRSSyncTone ? (byte) 1 : (byte) 0);
//        dest.writeByte(this.lowBatteryTone ? (byte) 1 : (byte) 0);
//        dest.writeByte(this.leadOffTone ? (byte) 1 : (byte) 0);
//        dest.writeByte(this.printOverTone ? (byte) 1 : (byte) 0);
//        dest.writeInt(this.autoLockScreenMode == null ? -1 : this.autoLockScreenMode.ordinal());
//        dest.writeInt(this.dateFormatMode == null ? -1 : this.dateFormatMode.ordinal());
//        dest.writeInt(this.timeFormatMode == null ? -1 : this.timeFormatMode.ordinal());
//        dest.writeString(this.organizationName);
//        dest.writeInt(this.leadFilterTypeAc == null ? -1 : this.leadFilterTypeAc.ordinal());
//        dest.writeByte(this.printOn ? (byte) 1 : (byte) 0);
//        dest.writeString(this.systemTechnician);
//        dest.writeInt(this.leadSampleDataFrequency == null ? -1 : this.leadSampleDataFrequency.ordinal());
//        dest.writeByte(this.useGlasgow ? (byte) 1 : (byte) 0);
//        dest.writeSerializable(this.historicalUsers);
//        dest.writeTypedList(this.patientQrCodeSettings);
//        dest.writeString(this.manCode);
//        dest.writeString(this.womanCode);
//        dest.writeString(this.codeSampleContent);
//
//        dest.writeString(this.cardioTServerIP);
//        dest.writeString(this.cardioTServerPort);
//    }
//
//    public SystemSettingBean() {
//    }
//
//    protected SystemSettingBean(Parcel in) {
//        int tmpLanguageMode = in.readInt();
//        this.languageMode = tmpLanguageMode == -1 ? null : SystemSettingConfigEnum.LanguageMode.values()[tmpLanguageMode];
//        int tmpDemoMode = in.readInt();
//        this.demoMode = tmpDemoMode == -1 ? null : SystemSettingConfigEnum.DemoMode.values()[tmpDemoMode];
//        int tmpDiagnosticMode = in.readInt();
//        this.diagnosticMode = tmpDiagnosticMode == -1 ? null : SystemSettingConfigEnum.DiagnosticMode.values()[tmpDiagnosticMode];
//        this.brightness = in.readInt();
//        this.volume = in.readInt();
//        this.keyTone = in.readByte() != 0;
//        this.QRSSyncTone = in.readByte() != 0;
//        this.lowBatteryTone = in.readByte() != 0;
//        this.leadOffTone = in.readByte() != 0;
//        this.printOverTone = in.readByte() != 0;
//        int tmpAutoLockScreenMode = in.readInt();
//        this.autoLockScreenMode = tmpAutoLockScreenMode == -1 ? null : SystemSettingConfigEnum.AutoLockScreenMode.values()[tmpAutoLockScreenMode];
//        int tmpDateFormatMode = in.readInt();
//        this.dateFormatMode = tmpDateFormatMode == -1 ? null : SystemSettingConfigEnum.DateFormatMode.values()[tmpDateFormatMode];
//        int tmpTimeFormatMode = in.readInt();
//        this.timeFormatMode = tmpTimeFormatMode == -1 ? null : SystemSettingConfigEnum.TimeFormatMode.values()[tmpTimeFormatMode];
//        this.organizationName = in.readString();
//        int tmpLeadFilterTypeAc = in.readInt();
//        this.leadFilterTypeAc = tmpLeadFilterTypeAc == -1 ? null : EcgSettingConfigEnum.LeadFilterType.values()[tmpLeadFilterTypeAc];
//        this.printOn = in.readByte() != 0;
//        this.systemTechnician = in.readString();
//        int tmpLeadSampleDataFrequency = in.readInt();
//        this.leadSampleDataFrequency = tmpLeadSampleDataFrequency == -1 ? null : EcgSettingConfigEnum.LeadSampleDataFrequency.values()[tmpLeadSampleDataFrequency];
//        this.useGlasgow = in.readByte() != 0;
//        this.historicalUsers = (HashMap<String, LoginInfoBean>) in.readSerializable();
//        this.patientQrCodeSettings = in.createTypedArrayList(PatientQrCodeSettingBean.CREATOR);
//        this.manCode=in.readString();
//        this.womanCode=in.readString();
//        this.codeSampleContent=in.readString();
//
//        String ip = in.readString();
//        this.cardioTServerIP = TextUtils.isEmpty(ip) ? Setting.HOST_URL_CARDLOT : ip;
//        String cardioTPort = in.readString();
//        this.cardioTServerPort = TextUtils.isEmpty(cardioTPort) ? "" : cardioTPort;
//
//    }
//
//    public static final Creator<SystemSettingBean> CREATOR = new Creator<SystemSettingBean>() {
//        @Override
//        public SystemSettingBean createFromParcel(Parcel source) {
//            return new SystemSettingBean(source);
//        }
//
//        @Override
//        public SystemSettingBean[] newArray(int size) {
//            return new SystemSettingBean[size];
//        }
//    };
}
