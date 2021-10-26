package com.lepu.algorithm.ecg.entity.dictionary;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.text.TextUtils;

import com.lepu.algorithm.R;

public class EcgSettingConfigEnum {
    public enum LeadElectrodeType {
        TYPE_IEC(R.string.setting_text_ecg_setting_iec_standard,"IEC"),//欧标
        TYPE_AHA(R.string.setting_text_ecg_setting_aha_standard,"AHA");//美标

        private @StringRes int nameResId;
        private Object value;

        LeadElectrodeType(@StringRes int nameResId, Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

        public String getName(@NonNull Context context) {
            return context.getString(nameResId);
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public enum LeadFilterType {
        //肌电滤波器
        FILTER_EMG_25 ("25Hz",25),
        FILTER_EMG_35 ("35Hz",35),
        FILTER_EMG_45 ("45Hz",45),
        FILTER_EMG_CLOSE(R.string.setting_text_off,-1) ,

        //低通滤波器
        FILTER_LOWPASS_75 ("75Hz",75),
        FILTER_LOWPASS_100 ("100Hz",100),
        FILTER_LOWPASS_150 ("150Hz",150),
        FILTER_LOWPASS_300 ("300Hz",300),
        FILTER_LOWPASS_CLOSE (R.string.setting_text_off,-2),

        //基线漂移滤波器
        FILTER_BASELINE_001 ("0.01Hz",0.01F),
        FILTER_BASELINE_005 ("0.05Hz",0.05F),
        FILTER_BASELINE_032 ("0.32Hz",0.32F),
        FILTER_BASELINE_067 ("0.67Hz",0.67F),

        //交流滤波器
        FILTER_AC_CLOSE (R.string.setting_text_off,0),
        FILTER_AC_OPEN (R.string.app_open,1),
        FILTER_AC_50_HZ ("50Hz",50),
        FILTER_AC_60_HZ ("60Hz",60);

        private String name;
        private Object value;
        private @StringRes int nameResId = 0;

        LeadFilterType(String name, Object value){
            this.name = name;
            this.value = value;
        }

        LeadFilterType(@StringRes int nameResId,Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

//        public String getName() {
//            if (!TextUtils.isEmpty(name)){
//                return name;
//            }
//            if (nameResId != 0){
//                Context context = MyAppProxy.getInstance().getResumeAct();
//                if (context == null){
//                    context = BaseApplication.getInstance();
//                }
//                try {
//                    return context.getString(nameResId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return "";
//        }

        public Object getValue() {
            return value;
        }

        public static LeadFilterType getLeadEnumByValue(float value){
            LeadFilterType type = null;
            for(LeadFilterType item : LeadFilterType.values()){
                Object obj = item.getValue();
                if(obj instanceof Float){
                    if((float)obj == value){
                        type = item;
                        break;
                    }
                }
            }
            return type;
        }

        public static LeadFilterType getLeadEnumByValue(int value){
            LeadFilterType type = LeadFilterType.FILTER_LOWPASS_300;
            for(LeadFilterType item : LeadFilterType.values()){
                Object obj = item.getValue();
                if(obj instanceof Integer){
                    if((int)obj == value){
                        type = item;
                        break;
                    }
                }
            }
            return type;
        }
    }

    public enum LeadGainType {
        GAIN_AUTO (R.string.app_auto,"auto"),
        GAIN_2_P_5 ("2.5mm/mV","2.5"),
        GAIN_5 ("5mm/mV","5"),
        GAIN_10 ("10mm/mV","10"),
        GAIN_20 ("20mm/mV","20"),
        GAIN_10_D_5 ("10/5mm/mV","10/5"),
        GAIN_20_D_10("20/10mm/mV","20/10"),
        GAIN_40 ("40mm/mV","40");

        private String name;
        private Object value;
        private @StringRes int nameResId;

        LeadGainType(String name, Object value){
            this.name = name;
            this.value = value;
        }

        LeadGainType(@StringRes int nameResId, Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

//        public String getName() {
//            if (name != null){
//                return name;
//            }
//            if (nameResId != 0){
//                Context context = MyAppProxy.getInstance().getResumeAct();
//                if (context == null){
//                    context = BaseApplication.getInstance();
//                }
//                try {
//                    return context.getString(nameResId);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//            return "";
//        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public static LeadGainType getLeadEnumByValue(Object value){
            LeadGainType type = LeadGainType.GAIN_10;
            for(LeadGainType item : LeadGainType.values()){
                if(item.getValue() == value){
                    type = item;
                    break;
                }
            }
            return type;
        }
    }

    public enum LeadModeType {
        LEADMODE_STANDARD(R.string.setting_text_ecg_setting_standard,"Standard"),//标准
        LEADMODE_CABREREA(R.string.Lead_mode_cabrera,"Cabrera");//cabrera

        private @StringRes int nameResId;
        private Object value;

        LeadModeType(@StringRes int nameResId, Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

        public String getName(@NonNull Context context) {
            return context.getString(nameResId);
        }

        public Object getValue() {
            return value;
        }

    }

    public enum LeadNameType {
        I ("I","I"),
        II ("II","II"),
        III ("III","III"),
        aVR ("aVR","aVR"),
        aVL ("aVL","aVL"),
        aVF ("aVF","aVF"),
        V1 ("V1","V1"),
        V2 ("V2","V2"),
        V3 ("V3","V3"),
        V4("V4","V4"),
        V5 ("V5","V5"),
        V6 ("V6","V6"),
        V3R ("V3R","V3R"),
        V4R ("V4R","V4R"),
        V5R("V5R","V5R"),
        V7("V7","V7"),
        V8 ("V8","V8"),
        V9 ("V9","V9"),;

        private String name;
        private Object value;

        LeadNameType(String name, Object value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public enum CabreraLeadNameType {
        aVL ("aVL","aVL"),
        I ("I","I"),
        aVR ("-aVR","-aVR"),
        II ("II","II"),
        aVF ("aVF","aVF"),
        III ("III","III"),
        V1 ("V1","V1"),
        V2 ("V2","V2"),
        V3 ("V3","V3"),
        V4("V4","V4"),
        V5 ("V5","V5"),
        V6 ("V6","V6"),
        V3R ("V3R","V3R"),
        V4R ("V4R","V4R"),
        V5R("V5R","V5R"),
        V7("V7","V7"),
        V8 ("V8","V8"),
        V9 ("V9","V9"),;

        private String name;
        private Object value;

        CabreraLeadNameType(String name, Object value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public enum LeadRhythmType {
        /**
         * 单节律
         */
        RHYTHM_ONE (R.string.setting_text_ecg_setting_rhythm_one,"RHYTHM_ONE"),
        /**
         * 三节律
         */
        RHYTHM_THREE (R.string.setting_text_ecg_setting_rhythm_three,"RHYTHM_THREE");

        private @StringRes int nameResId;
        private Object value;

        LeadRhythmType(@StringRes int nameResId, Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

        public String getName(@NonNull Context context) {
            return context.getString(nameResId);
        }

        public Object getValue() {
            return value;
        }

    }

    public enum LeadShowStyleType {

        //常规导联模板
        //9 lead
        FORMAT_9_9_1("9x1","FORMAT_9_9_1",9,1),
        FORMAT_9_3_3 ("3x3","FORMAT_9_3_3",3,3),
        FORMAT_9_3_3_1R("3x3+1R","FORMAT_9_3_3_1R",4,3),//default
        FORMAT_9_3_3_3R("3x3+3R","FORMAT_9_3_3_3R",6,3),
        FORMAT_9_6_3("6+3","FORMAT_9_6_3",6,2),

        //12 lead
        FORMAT_12_12_1 ("12x1","FORMAT_12_12_1",12,1),
        FORMAT_12_6_2 ("6x2","FORMAT_12_6_2",6,2),
        FORMAT_12_6_2_1R ("6x2+1R","FORMAT_12_6_2_1R",7,2),//default
        FORMAT_12_3_4 ("3x4","FORMAT_12_3_4",3,4),
        FORMAT_12_3_4_1R("3x4+1R","FORMAT_12_3_4_1R",4,4),
        FORMAT_12_3_4_3R("3x4+3R","FORMAT_12_3_4_3R",6,4),

        //15导联
        FORMAT_15_15_1 ("15x1","FORMAT_15_15_1",15,1),
        FORMAT_15_6_9 ("6+9","FORMAT_15_6_9",9,2),
        FORMAT_15_6_6_3 ("6+6+3","FORMAT_15_6_6_3",6,3),
        FORMAT_15_6_6_3_1R ("6+6+3+1R","FORMAT_15_6_6_3_1R",7,3),//default
        FORMAT_15_3_5 ("3x5","FORMAT_15_3_5",3,5),
        FORMAT_15_3_5_1R ("3x5+1R","FORMAT_15_3_5_1R",4,5),
        FORMAT_15_3_5_3R ("3x5+3R","FORMAT_15_3_5_3R",6,5),

        //18导联
        FORMAT_18_18_1 ("18x1","FORMAT_18_18_1",18,1),//暂时先不用了
        FORMAT_18_12_1_6_1 ("12x1+6x1","FORMAT_18_12_1_6_1",12,2),
        FORMAT_18_6_3_1R ("6x3+1R","FORMAT_18_6_3_1R",7,3),//default
        FORMAT_18_6_2_6_1 ("6x2+6X1","FORMAT_18_6_2_6_1",6,3),
        FORMAT_18_6_2_6_1_2R ("6x2+6x1+2R","FORMAT_18_6_2_6_1_2R",8,3),

        //其它工作模式特殊模板
        /**
         * 药物试验
         * 单节律
         */
        FORMAT_RHYTHM_ONE_DRUG_TEST (R.string.setting_text_ecg_setting_rhythm_one,"FORMAT_RHYTHM_ONE_DRUG_TEST",6,1),

        /**
         * RR 单节律
         */
        FORMAT_RHYTHM_ONE_RR (R.string.setting_text_ecg_setting_rhythm_one,"FORMAT_RHYTHM_ONE_RR",6,1),
        /**
         * RR 三节律
         */
        FORMAT_RHYTHM_THREE_RR (R.string.setting_text_ecg_setting_rhythm_three,"FORMAT_RHYTHM_THREE_RR",6,1),
        /**
         * HRV 模板
         */
        FORMAT_HRV_3R (R.string.setting_text_ecg_setting_hrv,"FORMAT_HRV_3R",3,1),
        /**
         * frank 模板
         */
        FORMAT_FRANK (R.string.setting_text_ecg_setting_fank,"FORMAT_FRANK",3,1),

        /**
         * 常规报告的特殊模板
         */
        FORMAT_REPORT_9_6_3__6("FORMAT_REPORT_9_6_3__6","FORMAT_REPORT_9_6_3__6",6,1),
        FORMAT_REPORT_9_6_3__3("FORMAT_REPORT_9_6_3__3","FORMAT_REPORT_9_6_3__3",3,1),

        FORMAT_REPORT_15_6_6_3__6_1("FORMAT_REPORT_15_6_6_3__6_1","FORMAT_REPORT_15_6_6_3__6_1",6,1),
        FORMAT_REPORT_15_6_6_3__6_2("FORMAT_REPORT_15_6_6_3__6_2","FORMAT_REPORT_15_6_6_3__6_2",6,1),
        FORMAT_REPORT_15_6_6_3__3("FORMAT_REPORT_15_6_6_3__3","FORMAT_REPORT_15_6_6_3__3",3,1),
        FORMAT_REPORT_15_6_9__6("FORMAT_REPORT_15_6_9__6","FORMAT_REPORT_15_6_9__6",6,1),
        FORMAT_REPORT_15_6_9__9("FORMAT_REPORT_15_6_9__9","FORMAT_REPORT_15_6_9__9",9,1),

        FORMAT_REPORT_18_12_6__12("FORMAT_REPORT_18_12_6__12","FORMAT_REPORT_18_12_6__12",12,1),
        FORMAT_REPORT_18_12_6__6("FORMAT_REPORT_18_12_6__6","FORMAT_REPORT_18_12_6__6",6,1),
        FORMAT_REPORT_18_6_2_6_1__6_2("FORMAT_REPORT_18_6_2_6_1__6_2","FORMAT_REPORT_18_6_2_6_1__6_2",6,2),
        FORMAT_REPORT_18_6_2_6_1__6_1("FORMAT_REPORT_18_6_2_6_1__6_1","FORMAT_REPORT_18_6_2_6_1__6_1",6,1);

        private String name;
        private Object value;
        private int leadLines;
        private int leadColumns;
        private @StringRes int nameResId;

        LeadShowStyleType(String name, Object value, int leadLines, int leadColumns) {
            this.name = name;
            this.value = value;
            this.leadLines = leadLines;
            this.leadColumns = leadColumns;
        }

        LeadShowStyleType(@StringRes int nameResId, Object value,int leadLines, int leadColumns){
            this.nameResId = nameResId;
            this.value = value;
            this.leadLines = leadLines;
            this.leadColumns = leadColumns;
        }

//        public String getName() {
//            if (!TextUtils.isEmpty(name)){
//                return name;
//            }
//            if (nameResId != 0){
//                Context act = MyAppProxy.getInstance().getResumeAct();
//                if (act == null){
//                    act = BaseApplication.getInstance();
//                }
//                try {
//                    return act.getString(nameResId);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//            return "";
//        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public int getOriginalLeadLines(){
            return leadLines;
        }

        public int getOriginalLeadColumns(){
            return leadColumns;
        }
    }

    public enum LeadSpeedType {
        FORMFEED_5 ("5mm/s",5F),
        FORMFEED_6_P_25 ("6.25mm/s",6.25F),
        FORMFEED_10 ("10mm/s",10F),
        FORMFEED_12_P_5 ("12.5mm/s",12.5F),
        FORMFEED_25 ("25mm/s",25F),
        FORMFEED_50 ("50mm/s",50F);

        private String name;
        private Object value;

        LeadSpeedType(String name, Object value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public static LeadSpeedType getLeadEnumByValue(Object value){
            LeadSpeedType type = LeadSpeedType.FORMFEED_25;
            for(LeadSpeedType item : LeadSpeedType.values()){
                if(item.getValue() == value){
                    type = item;
                    break;
                }
            }
            return type;
        }
    }

    public enum LeadType {
        //value值不能修改，传统算法分析时用
        LEAD_9(R.string.setting_text_ecg_setting_lead_type_9,8),/**9导联**/
        LEAD_12(R.string.setting_text_ecg_setting_lead_type_12,1),/**12导联**/
        LEAD_15_STANDARD_RIGHT(R.string.setting_text_ecg_setting_lead_type_15_right,6),/**15导联 标准+右**/
        LEAD_15_STANDARD_AFTER(R.string.setting_text_ecg_setting_lead_type_15_after,5),/**15导联 标准+后**/
        LEAD_15_CHILD(R.string.setting_text_ecg_setting_lead_type_15_child,9),/**15导联 儿童模式**/
        LEAD_18(R.string.setting_text_ecg_setting_lead_type_18,7);/**18导联**/

        private @StringRes int nameResId;
        private Object value;

        LeadType(@StringRes int nameResId, Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

//        public String getName(){
//            Activity act = MyAppProxy.getInstance().getResumeAct();
//            if (act != null)
//                return act.getString(nameResId);
//            return BaseApplication.getInstance().getString(nameResId);
//        }

        public Object getValue() {
            return value;
        }

    }

    public enum LeadWorkModeType {

        /**
         * 其它
         */
        WORK_MODE_OTHER (R.string.type_other,"WORK_MODE_OTHER",""),

        /**
         * 无工作模式
         */
        WORK_MODE_NO (R.string.type_no,"WORK_MODE_NO",""),

        /**
         * 手动
         */
        WORK_MODE_MANUAL(R.string.setting_text_ecg_setting_work_mode_manual,"WORK_MODE_MANUAL","00400001"),

        /**
         * 实时采样
         */
        WORK_MODE_SAMPLE_REALTIME (R.string.setting_text_ecg_setting_work_mode_realtime_sample,"WORK_MODE_SAMPLE_REALTIME","00400002"),
        /**
         * 预采样
         */
        WORK_MODE_SAMPLE_PRE (R.string.setting_text_ecg_setting_work_mode_pre_sample,"WORK_MODE_SAMPLE_PRE","00400003"),
        /**
         * 周期采样
         */
        WORK_MODE_SAMPLE_PERIODIC (R.string.setting_text_ecg_setting_work_mode_cycle_sample,"WORK_MODE_SAMPLE_PERIODIC","00400004"),
        /**
         * 触发采样
         */
        WORK_MODE_SAMPLE_TRIGGER (R.string.setting_text_ecg_setting_work_mode_triger_sample,"WORK_MODE_SAMPLE_TRIGGER","00400005"),

        /**
         * rr
         */
        WORK_MODE_RR (R.string.setting_text_ecg_setting_work_mode_rr,"WORK_MODE_RR",""),

        /**
         * hrv
         */
        WORK_MODE_HRV (R.string.setting_text_ecg_setting_work_mode_hrv,"WORK_MODE_HRV",""),

        /**
         * 心电事件
         */
        WORK_MODE_ECG_EVENT (R.string.setting_text_ecg_setting_work_mode_ecg_event,"WORK_MODE_ECG_EVENT",""),

        /**
         * 药物试验
         */
        WORK_MODE_DRUG_TEST (R.string.setting_text_ecg_setting_work_mode_drug_test,"WORK_MODE_DRUG_TEST",""),

        /**
         * 12导联扩展 暂时不支持
         */
        WORK_MODE_EXTEND_LEAD12 (R.string.setting_text_ecg_setting_work_mode_12_lead_add,"WORK_MODE_EXTEND_LEAD12",""),

        /**
         * frank 暂时不支持
         */
        WORK_MODE_FRANK (R.string.setting_text_ecg_setting_work_mode_frank,"WORK_MODE_FRANK","");

        private @StringRes int nameResId;
        private Object value;
        private String code;

        LeadWorkModeType(@StringRes int nameResId, Object value,String code){
            this.nameResId = nameResId;
            this.value = value;
            this.code = code;
        }

//        public String getName() {
//            Activity act = MyAppProxy.getInstance().getResumeAct();
//            if (act != null)
//                return act.getString(nameResId);
//            return BaseApplication.getInstance().getString(nameResId);
//        }

        public Object getValue() {
            return value;
        }

        public String getCode(){
            return code;
        }

    }

    public enum LeadSampleDataFrequency {
        TYPE_NORMAL("","TYPE_NORMAL"),//正常频率
        TYPE_HIGH("","TYPE_HIGH");//高频率

        private String name;
        private Object value;

        LeadSampleDataFrequency(String name, Object value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
