package com.lepu.algorithm.ecg.entity.dictionary;

//package com.setting.entity.dictionary;

import android.content.Context;
import androidx.annotation.StringRes;

//import com.appstart.MyAppProxy;
import com.lepu.algorithm.R;
//import com.lib.common.BaseApplication;
//import com.main.R;


public class PatientSettingConfigEnum {
    public enum PatientInfoConfigMustType{
        /**
         * 患者id
         */
        PATIENT_ID(R.string.usercenter_text_patientinfo_id, ""),
        /**
         * 姓
         */
        LAST_NAME(R.string.usercenter_text_patientinfo_lastname, ""),
        /**
         * 名
         */
        FIRST_NAME(R.string.usercenter_text_patientinfo_firstname, ""),

        /**
         * 性别
         */
        SEX(R.string.usercenter_text_patientinfo_sex, ""),

        /**
         * 年龄
         */
        AGE(R.string.usercenter_text_patientinfo_age, ""),

        /**
         * 出生日期
         */
        BIRTHDATE(R.string.usercenter_text_patientinfo_birth, "");

        //PACEMAKER(BaseApplication.getInstance().getResources().getString(R.string.usercenter_text_patientinfo_pacemaker), "");


        private @StringRes int nameResId;
        private Object value;

        PatientInfoConfigMustType(@StringRes int nameResId, Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

//        public String getName() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(nameResId);
//        }


        public Object getValue() {
            return value;
        }

    }

    public enum PatientInfoConfigType{
        /**
         * 中间名
         */
        MIDDLE_NAME (R.string.usercenter_text_patientinfo_midname,""),

        /**
         * 身高
         */
        HEIGHT (R.string.usercenter_text_patientinfo_height,""),

        /**
         * 体重
         */
        WEIGHT (R.string.usercenter_text_patientinfo_weight,""),

        /**
         * 血压
         */
        BLOOD_PRESSURE (R.string.usercenter_text_patientinfo_bloodpressure,""),

        /**
         * 种族
         */
        RACE (R.string.usercenter_text_patientinfo_race,""),

        /**
         * 用药
         */
        MEDICATION (R.string.usercenter_text_patientinfo_medical,""),

        /**
         * 病史
         */
        MEDICAL_HISTORY (R.string.usercenter_text_patientinfo_medicalhistory,""),

        /**
         * 患者来源
         */
        PATIENT_FROM (R.string.usercenter_text_patientinfo_source,""),

        /**
         *申请科室
         */
        APPLY_DEPARTMENT (R.string.usercenter_text_patientinfo_applicdepart,""),

        /**
         *检查科室
         */
        CHECK_DEPARTMENT (R.string.usercenter_text_patientinfo_insecdepart,""),

        /**
         *申请医生
         */
        APPLY_DOCTOR (R.string.usercenter_text_patientinfo_applicdoctor,""),

        /**
         *检查医生
         */
        CHECK_DOCTOR (R.string.usercenter_text_patientinfo_insecdoctor,""),

        /**
         *门诊号
         */
        OUT_PATIENT_NUMBER (R.string.usercenter_text_patientinfo_outpatientno,""),

        /**
         *住院号
         */
        HOSPITAL_NUMBER (R.string.usercenter_text_patientinfo_inpatientno,""),

        /**
         *体检号
         */
        PHYSICAL_NUMBER (R.string.usercenter_text_patientinfo_testno,""),

        /**
         *床号
         */
        BED_NUMBER (R.string.usercenter_text_patientinfo_bedno,""),
        /**
         *身份证号码
         */
        ID_NUMBER (R.string.usercenter_text_patientinfo_idnumber,""),
        /**
         * 起搏器
         */
        PACEMAKER(R.string.usercenter_text_patientinfo_pacemaker, ""),

        /**
         * 加急
         */
//		URGENT(R.string.main_text_menu_add_busy,""),
        /**
         *检查项目
         */
        INSPECT_ITEM (R.string.usercenter_text_patientinfo_inspecitem,""),
        /**
         *检查单号
         */
        CHECK_NO (R.string.usercenter_text_patientinfo_checkno,"");

        /**
         * 加急
         * */
//		URGENT(R.string.usercenter_text_case_manage_urgent,"");
        private @StringRes int nameResId;
        private Object value;

        PatientInfoConfigType(@StringRes int nameResId, Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

//        public String getName() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(nameResId);
//        }

        public Object getValue() {
            return value;
        }

    }

    public enum UniqueCodeType{
        /**
         * 患者id
         */
        UNIQUE_CODE_ID(R.string.usercenter_text_patientinfo_id,""),
        /**
         * 病历号
         */
        UNIQUE_CODE_CASE_NO(R.string.usercenter_text_patientinfo_inpatientno,""),
        /**
         * 门诊号
         */
        UNIQUE_CODE_OUTPATIENT_NO(R.string.usercenter_text_patientinfo_outpatientno,""),
        /**
         * 挂号号码
         */
        UNIQUE_CODE_REGISTER_NO(R.string.usercenter_text_patientinfo_registerno,"");

        private @StringRes int nameResId;
        private Object value;

        UniqueCodeType(@StringRes int nameResId, Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

//        public String getName() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(nameResId);
//        }


        public Object getValue() {
            return value;
        }

    }



    public enum HeightWeightUnit {
        /**
         * cm/kg
         */
        UNIT_CM_KG("cm/kg","cm/kg"),
        /**
         * inch/lb
         */
        UNIT_INCH_LB("inch/lb","inch/lb");

        private String name;
        private Object value;

        HeightWeightUnit(String name, Object value){
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

    public enum BloodPressureUnit{
        UNIT_MMHG("mmhg",""),
        UNIT_KPA("kpa","");

        private String name;
        private Object value;

        BloodPressureUnit(String name, Object value){
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

    public enum IdMakeType{
        /**
         * 累加
         */
        ID_SUMMATION (R.string.setting_text_patient_setting_id_autoincrement,""),
        /**
         * 输入
         */
        ID_INPUT (R.string.setting_text_patient_setting_id_input,"");

        private @StringRes int nameResId;
        private Object value;

        IdMakeType(@StringRes int nameResId, Object value){
            this.nameResId = nameResId;
            this.value = value;
        }

//        public String getName() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(nameResId);
//        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
    public enum AgeUnit {
        /**
         * 年
         */
        YEAR(R.string.usercenter_text_patientinfo_unit_year, R.string.usercenter_text_patientinfo_unit_year),
        /**
         * 月
         */
        MONTH(R.string.usercenter_text_patientinfo_unit_month, R.string.usercenter_text_patientinfo_unit_month),
        /**
         * 日
         */
        DAY(R.string.usercenter_text_patientinfo_unit_day, R.string.usercenter_text_patientinfo_unit_day);
        private @StringRes int nameResId;
        private @StringRes int valueResId;

        AgeUnit(@StringRes int nameResId,@StringRes int valueResId) {
            this.nameResId = nameResId;
            this.valueResId = valueResId;
        }

//        public String getName() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(nameResId);
//        }

//        public String getValue() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(valueResId);
//        }

    }
    public enum Sex {
        /**
         * 男
         */
        MAN(R.string.usercenter_text_patientinfo_man, R.string.usercenter_text_patientinfo_man),
        /**
         * 女
         */
        WOMAN(R.string.usercenter_text_patientinfo_woman, R.string.usercenter_text_patientinfo_woman),
        /**
         * 未知
         */
        OTHER(R.string.usercenter_text_patientinfo_unknown_age, R.string.usercenter_text_patientinfo_unknown_age);
        private @StringRes int nameResId;
        private @StringRes int valueResId;

        Sex(@StringRes int nameResId, @StringRes int valueResId) {
            this.nameResId = nameResId;
            this.valueResId = valueResId;
        }

//        public String getName() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(nameResId);
//        }
//
//        public String getValue() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(valueResId);
//        }

    }
    public enum Race {
        /**
         * 亚裔
         */
        ASIAN(R.string.usercenter_text_patientinfo_asian, ""),
        /**
         * 白种人
         */
        WESTERN(R.string.usercenter_text_patientinfo_western, ""),
        /**
         * 非洲裔
         */
        AFRICAN(R.string.usercenter_text_patientinfo_african, ""),
        /**
         * 其他
         */
        OTHER(R.string.usercenter_text_patientinfo_other, ""),
        /**
         * 未指定
         */
        UNKNOWN(R.string.usercenter_text_patientinfo_unknown, "");
        private @StringRes int nameResId;
        private Object value;

        Race(@StringRes int nameResId, Object value) {
            this.nameResId = nameResId;
            this.value = value;
        }

//        public String getName() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(nameResId);
//        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
    public enum PatientSource {
        /**
         * 住院
         */
        INPATIENT(R.string.usercenter_text_patientinfo_inpatient, ""),
        /**
         * 门诊
         */
        OUTPATIENT(R.string.usercenter_text_patientinfo_outpatient, ""),
        /**
         * 体检
         */
        TEST(R.string.usercenter_text_patientinfo_test, "");

        private @StringRes int nameResId;
        private Object value;

        PatientSource(@StringRes int nameResId, Object value) {
            this.nameResId = nameResId;
            this.value = value;
        }

//        public String getName() {
//            Context context = MyAppProxy.getInstance().getResumeAct();
//            if (context == null){
//                context = BaseApplication.getInstance();
//            }
//            return context.getString(nameResId);
//        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    //身高单位
    public enum HeightUnit{
        CM("cm","cm"),
        INCH("inch","inch");
        private String name;
        private String value;
        HeightUnit(String name,String value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    //体重单位
    public enum WeightUnit{
        KG("kg","kg"),
        LB("lb","lb");
        private String name;
        private String value;
        WeightUnit(String name,String value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }


}

