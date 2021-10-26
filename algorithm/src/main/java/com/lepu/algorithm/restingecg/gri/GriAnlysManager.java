package com.lepu.algorithm.restingecg.gri;

import android.text.TextUtils;

import com.lepu.algorithm.Init;
import com.lepu.algorithm.ecg.entity.AiResultBean;
import com.lepu.algorithm.ecg.entity.AiResultDiagnosisBean;
import com.lepu.algorithm.ecg.entity.AiResultMeasuredValueBean;
import com.lepu.algorithm.ecg.entity.ConfigBean;
import com.lepu.algorithm.ecg.entity.EcgSettingBean;
import com.lepu.algorithm.ecg.entity.MeasureResultBean;
import com.lepu.algorithm.ecg.entity.MinnesotaCodeItemBean;
import com.lepu.algorithm.ecg.entity.PatientInfoBean;
import com.lepu.algorithm.ecg.entity.dictionary.EcgMacureResultEnum;
import com.lepu.algorithm.ecg.entity.dictionary.EcgMeasureResultEnum;
import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;
import com.lepu.algorithm.ecg.entity.dictionary.PatientSettingConfigEnum;
import com.lepu.algorithm.ecg.utils.Const;
import com.lepu.algorithm.ecg.utils.DateUtil;
import com.lepu.algorithm.ecg.utils.SdLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class GriAnlysManager {

    private ConfigBean configBeanTemp;

    static {
        System.loadLibrary("GlasgowEcg");
    }

    private static GriAnlysManager instance = null;

    private GriAnlysManager() {

    }

    public static GriAnlysManager getInstance() {
        if (instance == null) {
            instance = new GriAnlysManager();
        }
        return instance;
    }

    /**
     * 暂时只支持12导。代码里的其它导联，暂时不删
     * @param patientInfoBean
     * @param datas
     * @param leadType
     * @param leadStates
     * @param saveLocal
     * @param preview
     * @return
     */
    public MeasureResultBean processEcg(PatientInfoBean patientInfoBean, short[][] datas, EcgSettingConfigEnum.LeadType leadType, List<Boolean> leadStates, boolean saveLocal, boolean preview) {
        RestingEcg restingEcg = new RestingEcg(Init.getAppContext());
        //GriAsc ascData= restingEcg.LoadFromGriAsc( Uri.parse("mnt/sdcard/NeoEcg/000d87c0-be6b-48b9-950a-268325aa9909.asc").getPath() );
//        int leadNum = SettingManager.getInstance().getCurrentNeedLeadNum();
//        InputStream inputStream = null;
//        try {
//            String value = (String) SystemSettingConfigEnum.DemoMode.DEMOMODE_NORMALE.getValue();
//            inputStream = BaseApplication.getInstance().getAssets().open(String.format("demo_ecg_data/wave_%s.xml", value));
//            EcgDataInfoBean ecgDataInfoBean = XmlUtil.getEcgDataFromXml(BaseApplication.getInstance(), inputStream, false, leadNum);
//            datas = WaveEncodeUtil.leadDataSwitch(ecgDataInfoBean.getEcgDataArray(),false,leadType);
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
//        KLog.d(Arrays.toString(leadStates.toArray()));
        //先把导联脱落位置换下 LA LL RA RL 放最后面
        for (int i = 0; i <4 ; i++) {
            leadStates.add(leadStates.remove(0));
        }
//        KLog.d(Arrays.toString(leadStates.toArray()));
        datas = downSample(datas);

        GriAsc ascData = new GriAsc();


        ascData.FileVersion = "1.4";
        ascData.PatientId = patientInfoBean.getPatientNumber();
        ascData.PatientSurname = patientInfoBean.getLastName();
        ascData.PatientForenames = String.format(Locale.getDefault(), "%s%s", TextUtils.isEmpty(patientInfoBean.getMiddleName()) ? "" : patientInfoBean.getMiddleName(), TextUtils.isEmpty(patientInfoBean.getFirstName()) ? "" : patientInfoBean.getFirstName());
        if (patientInfoBean.getSex().equals("0"))
            ascData.Gender = "M".getBytes()[0];
        if (patientInfoBean.getSex().equals("1"))
            ascData.Gender = "F".getBytes()[0];
        if (patientInfoBean.getSex().equals("2"))
            ascData.Gender = "U".getBytes()[0];
        if (patientInfoBean.getRace() == PatientSettingConfigEnum.Race.WESTERN.ordinal())
            ascData.Race = "C".getBytes()[0];
        if (patientInfoBean.getRace() == PatientSettingConfigEnum.Race.AFRICAN.ordinal())
            ascData.Race = "N".getBytes()[0];
        if (patientInfoBean.getRace() == PatientSettingConfigEnum.Race.ASIAN.ordinal())
            ascData.Race = "O".getBytes()[0];
        if (patientInfoBean.getRace() == PatientSettingConfigEnum.Race.UNKNOWN.ordinal())
            ascData.Race = "U".getBytes()[0];

        int ageDays = -1;
        if (!TextUtils.isEmpty(patientInfoBean.getAge()))
            if (patientInfoBean.getAgeUnit() == PatientSettingConfigEnum.AgeUnit.YEAR) {
                ageDays = 365 * Integer.parseInt(patientInfoBean.getAge());
            } else if (patientInfoBean.getAgeUnit() == PatientSettingConfigEnum.AgeUnit.MONTH) {
                ageDays = 30 * Integer.parseInt(patientInfoBean.getAge());
            } else {
                ageDays = Integer.parseInt(patientInfoBean.getAge());
            }
        ascData.AgeDays = ageDays;

        String birth = DateUtil.formatDate(DateUtil.parseTime(patientInfoBean.getBirthdate(), DateUtil.FormatType.yyyyMMdd), DateUtil.FormatType.ddMMyyyy);
        String recordTime = DateUtil.formatDate(new Date(), DateUtil.FormatType.ddMMyyyy);
        if (!TextUtils.isEmpty(birth))
            ascData.Dates = String.format(Locale.getDefault(), "%s,%s", birth, recordTime);
        ascData.RecordingTime = DateUtil.formatDate(new Date(), DateUtil.FormatType.HHmmss);
        /**
         * Adult Standard lead positioning
         * Paediatric Paediatric lead positioning
         */
        ascData.ElectrodePositioning = "Adult";
        /**
         * 1 No medication
         * 2 Unknown medication
         * 3 Digitalis
         * 4 Diuretic
         * 5 Beta blocker
         * 6 Quinidine
         * 7 Procainamide
         * 8 Amiodarone
         * 9 Disopyramide
         * 10 Lignocaine
         * 11 Other anti-arrhythmics
         * 12 Psychotropic
         * 13 Steroid
         * 14 Other medication
         * 0 Undefined
         */
        ascData.Drugs = "1 255";
        /**
         * 10 Normal
         * 11 Myocardial infarction
         * 12 Myocardial ischemia
         * 13 Hypertension
         * 15 Congenital heart disease
         * 17 Valvular heart disease
         * 30 Pericarditis
         * 31 Respiratory disease
         * 33 Endocrine disease
         * 46 Implanted pacemaker
         * 47 Pulmonary embolism
         * 49 Post operative cardiac surgery
         * 50 Other
         * 51 Cardiomyopathy
         * 52 Unknown
         * 0 Undefined
         */
        ascData.Clinical = "50 255";
        /**
         * 50 Apply 50Hz mains filter
         * 60 Apply 60Hz mains filter
         * 0 Do not apply mains filter (assume data already filtered)
         */
        ascData.MainsFilterFrequency = 0;
        EcgSettingBean ecgSettingBean = configBeanTemp.getEcgSettingBean();
        ascData.BradyTachyLimit = String.format(Locale.getDefault(), "%d %d", ecgSettingBean.getBradycardiaValue(), ecgSettingBean.getTachycardiaValue());
        /**
         * QTcHodge
         * QTcBazett
         * QTcFridericia
         * QTcFramingham
         */
        ascData.QTcMeasure = "QTcHodge";

        ascData.Pacers = "0 0";
        ascData.LSBperMV = 200;
        ascData.SamplingRate = 500;
        ascData.NumSamplesPerLead = 5000;

        if (true){ //默认demo或预览模式
            StringBuilder sb = new StringBuilder();
            sb.append("110000111111");
            if(leadType.ordinal() > EcgSettingConfigEnum.LeadType.LEAD_12.ordinal()){
                sb.append("111111");

                ascData.AnalysisType = "15Lead";
                ascData.NumLeads = 15;
            }else{
                ascData.AnalysisType = "12Lead";
                ascData.NumLeads = 12;
            }
            ascData.LeadAvailability = sb.toString();
        }else{
            /**
             * 12Lead
             * 15Lead
             * 15LeadDerivedXYZ
             * Reduced1Lead
             * Reduced1LeadI
             * Reduced1LeadII
             * Reduced2Lead
             * Reduced6Lead
             * Reduced3LeadV1
             * Reduced3LeadV5
             */
            StringBuilder leadAvailable1 = new StringBuilder();
            StringBuilder leadAvailable2 = new StringBuilder();
            if (leadType == EcgSettingConfigEnum.LeadType.LEAD_9 || leadType == EcgSettingConfigEnum.LeadType.LEAD_12) {
                ascData.AnalysisType = "12Lead";
                ascData.NumLeads = 12;

                //LA LL RA RL v1-v5R
                //LA
                //I(LA) II(F/LL) III avr avl avf
                boolean laOff = leadStates.get(0);
                boolean llOff = leadStates.get(1);
                if (laOff && llOff)
                    leadAvailable2.append("000000");
                else if (laOff)
                    leadAvailable2.append("010000");
                else if (llOff) {
                    leadAvailable2.append("100000");
                } else {
                    leadAvailable2.append("110000");
                }

                if (leadType == EcgSettingConfigEnum.LeadType.LEAD_9){
                    for (int j = 4; j < leadStates.size(); j++) {
                        //v1-v6
                        if (leadStates.get(j))
                            leadAvailable1.append("0");
                        else
                            leadAvailable1.append("1");

                        //模拟剩下的导联为正常
                        leadAvailable1.append("1");
                    }
                }else{
                    for (int j = 4; j < leadStates.size(); j++) {
                        //v1-v6
                        if (leadStates.get(j))
                            leadAvailable1.append("0");
                        else
                            leadAvailable1.append("1");
                    }
                }


                ascData.LeadAvailability = leadAvailable2.toString() + leadAvailable1.toString();

            } else {
                ascData.AnalysisType = "15Lead";
                ascData.NumLeads = 15;

                for (int j = 0; j < leadStates.size(); j++) {
                    //v1-v6
                    if (j < 6) {
                        if (leadStates.get(j))
                            leadAvailable1.append("0");
                        else
                            leadAvailable1.append("1");
                    }
                }
                switch (leadType) {
                    case LEAD_15_CHILD:
                        //V3R V4R V7
//                    leadAvailable1.append(leadStates.get(9)?"0":"1");
//                    leadAvailable1.append(leadStates.get(10)?"0":"1");
//                    leadAvailable1.append(leadStates.get(6)?"0":"1");
                        //先用12导儿童模式
                        ascData.AnalysisType = "12Lead";
                        ascData.ElectrodePositioning = "Paediatric";
                        ascData.NumLeads = 12;
                        leadAvailable1.delete(0, leadAvailable1.length());
                        leadAvailable1.append(leadStates.get(10) ? "0" : "1");
                        for (int j = 0; j < leadStates.size(); j++) {
                            //v1-v6
                            if (j < 6 && j != 2) {
                                if (leadStates.get(j))
                                    leadAvailable1.append("0");
                                else
                                    leadAvailable1.append("1");
                            }
                        }
                        //15导按12导处理
//                    ascData.Leads = new IntLead[ascData.NumLeads];
//                    for (int a = 0; a < ascData.NumLeads; a++) {
//                        ascData.Leads[a] = new IntLead(ascData.NumSamplesPerLead);
//                        if (a < 6)
//                            ascData.Leads[a].SetLeadData(datas[a]);
//                    }
//                    //V4R V1 V2 V4 V5 V6
//                    ascData.Leads[6].SetLeadData(datas[13]);
//                    ascData.Leads[7].SetLeadData(datas[6]);
//                    ascData.Leads[8].SetLeadData(datas[7]);
//                    ascData.Leads[9].SetLeadData(datas[9]);
//                    ascData.Leads[10].SetLeadData(datas[10]);
//                    ascData.Leads[11].SetLeadData(datas[11]);
                        break;
                    case LEAD_15_STANDARD_AFTER:
                        //V7 V8 V9
                        leadAvailable1.append(leadStates.get(6) ? "0" : "1");
                        leadAvailable1.append(leadStates.get(7) ? "0" : "1");
                        leadAvailable1.append(leadStates.get(8) ? "0" : "1");

                        break;
                    case LEAD_15_STANDARD_RIGHT:
                        //V3R V4R V5R
                        leadAvailable1.append(leadStates.get(9) ? "0" : "1");
                        leadAvailable1.append(leadStates.get(10) ? "0" : "1");
                        leadAvailable1.append(leadStates.get(11) ? "0" : "1");
                        break;
                }

                //LA
                //I II III avr avl avf
                boolean laOff = leadStates.get(12);
                boolean llOff = leadStates.get(13);
                if (laOff && llOff)
                    leadAvailable2.append("000000");
                else if (laOff)
                    leadAvailable2.append("010000");
                else if (llOff) {
                    leadAvailable2.append("100000");
                } else {
                    leadAvailable2.append("110000");
                }
                ascData.LeadAvailability = leadAvailable2.toString() + leadAvailable1.toString();
            }
        }

        //正常按顺序赋值
        //order I, II, III, aVR, aVL, aVF and V1-V6
        ascData.Leads = new IntLead[ascData.NumLeads];
        for (int a = 0; a < ascData.NumLeads; a++) {
            ascData.Leads[a] = new IntLead(ascData.NumSamplesPerLead);
            ascData.Leads[a].SetLeadData(datas[a]);
        }

        //test
//        ascData.LeadAvailability="110000111111";
        restingEcg.LoadFromGriAsc(ascData);
        restingEcg.controlInfo.setQTcHodge();
        AnalysisStatus status = restingEcg.Analyse();
//        ascData.printContent();
//        KLog.e(status);
//        KLog.e(restingEcg.measMatrix.globalMeas.toString());
//        KLog.e(Arrays.toString(restingEcg.statementTuples.getInterpText()));
        if (saveLocal)
        {
            String resultFilePath = SdLocal.getExportDataTxtPath(Init.getAppContext(),
                    String.format("%s_%d",patientInfoBean.getPatientNumber(),System.currentTimeMillis()));
            restingEcg.SaveResults(new EcgDataFileName(resultFilePath));
        }

        //检查测量值是否异常值
        RestingEcg.checkValue(restingEcg);

        //转换成MacureResultBean
        GlobalMeas globalMeas = restingEcg.measMatrix.globalMeas;

        MeasureResultBean resultBean = new MeasureResultBean();
        AiResultBean aiResultBean = new AiResultBean();

        aiResultBean.setHR(globalMeas.HeartRate);
        aiResultBean.setLeadcount(ascData.NumLeads);
        aiResultBean.setPAxis(globalMeas.PFrontalAxis);
        aiResultBean.setQRSAxis(globalMeas.QrsFrontalAxis);
        aiResultBean.setTAxis(globalMeas.TFrontalAxis);
        aiResultBean.setPd(globalMeas.OverallPDuration);
        aiResultBean.setPR(globalMeas.OverallPRInterval);
        aiResultBean.setQRS(globalMeas.OverallQrsDuration);
        aiResultBean.setQT(globalMeas.OverallQTInterval);
        aiResultBean.setQTc(globalMeas.QTc);
        aiResultBean.setSV1(globalMeas.SAmpV1);//待定
        aiResultBean.setRV5(globalMeas.RAmpV5);//待定

        int paSum=0,qaSum=0,raSum=0,saSum=0,taSum=0,sta20Sum=0,sta40Sum=0,sta60Sum=0,sta80Sum=0;
        short leadCount = 0;

        List<AiResultMeasuredValueBean> leadValues = new ArrayList<>();
        LeadMeas[] leadMeas= restingEcg.measMatrix.leadMeas;
        for(int i=0;i<ascData.NumLeads;i++){
            LeadMeas meas = leadMeas[i];
            if(i < 2 || i > 5) {
                //I II V1-V6
                paSum+=meas.PPosAmplitude;
                qaSum+=meas.QAmplitude;
                raSum+=meas.RAmplitude;
                saSum+=meas.SAmplitude;
                taSum+=meas.TposAmplitude;
                sta20Sum+=meas.STT28Amplitude;
                sta40Sum+=meas.STT38Amplitude;
                sta60Sum+=meas.ST60Amplitude;
                sta80Sum+=meas.ST80Amplitude;

                leadCount ++;
            }

            AiResultMeasuredValueBean aiResultMeasuredValueBean = new AiResultMeasuredValueBean();
            aiResultMeasuredValueBean.setQRStype("QRS");
            aiResultMeasuredValueBean.setPa1(meas.PPosAmplitude);
            aiResultMeasuredValueBean.setQa(meas.QAmplitude);
            aiResultMeasuredValueBean.setRa1(meas.RAmplitude);
            aiResultMeasuredValueBean.setSa1(meas.SAmplitude);
            aiResultMeasuredValueBean.setTa1(meas.TposAmplitude);
            aiResultMeasuredValueBean.setST1(RestingEcg.GRI_UNDEFINED);
            aiResultMeasuredValueBean.setSTj(RestingEcg.GRI_UNDEFINED);
            aiResultMeasuredValueBean.setST20(RestingEcg.GRI_UNDEFINED);
            aiResultMeasuredValueBean.setST40(RestingEcg.GRI_UNDEFINED);
            aiResultMeasuredValueBean.setST60(meas.ST60Amplitude);
            aiResultMeasuredValueBean.setST80(meas.ST80Amplitude);

            aiResultMeasuredValueBean.setQd(meas.getQDuration());
            aiResultMeasuredValueBean.setRd1(meas.getRDuration());
            aiResultMeasuredValueBean.setSd1(meas.getSDuration());
            leadValues.add(aiResultMeasuredValueBean);
        }

        //平均值辐值
        aiResultBean.setPA((short) (paSum / leadCount));
        aiResultBean.setQA((short) (qaSum / leadCount));
        aiResultBean.setRA((short) (raSum / leadCount));
        aiResultBean.setSA((short) (saSum / leadCount));
        aiResultBean.setTA((short) (taSum / leadCount));
        aiResultBean.setST20A((short) (sta20Sum / leadCount));
        aiResultBean.setST40A((short) (sta40Sum / leadCount));
        aiResultBean.setST60A((short) (sta60Sum / leadCount));
        aiResultBean.setST80A((short) (sta80Sum / leadCount));

        //所有测量值辐值
        aiResultBean.setMeasuredValueList(leadValues);
        aiResultBean.setMacureValueArray(AiResultBean.transferToMacureValueArray(aiResultBean.getMeasuredValueList()));

        AiResultDiagnosisBean aiResultDiagnosisBean = new AiResultDiagnosisBean();
        List<MinnesotaCodeItemBean> minnesotaCodeItemBeans = new LinkedList<>();
        for (String result : restingEcg.statementTuples.getInterpText()) {
            MinnesotaCodeItemBean bean = new MinnesotaCodeItemBean();
            bean.setTitle(result);
            minnesotaCodeItemBeans.add(bean);
        }

        aiResultDiagnosisBean.setDiagnosisCode(minnesotaCodeItemBeans);
        String jsonResult = AiResultDiagnosisBean.makeJson(aiResultDiagnosisBean);
        jsonResult = String.format("JSON:%s", jsonResult);
        aiResultBean.setArrDiagnosis(jsonResult);
        resultBean.setAiResultBean(aiResultBean);
        resultBean.getAiResultBean().setAiResultDiagnosisBean(aiResultDiagnosisBean);
        resultBean.setEcgMacureResultEnum(EcgMeasureResultEnum.TYPE_GLASGOW_RESULT);

        return resultBean;
    }
    /**
     * 降采样
     *
     * @param src
     * @return
     */
    private short[][] downSample(short[][] src) {
        short[][] result = new short[src.length][src[0].length / 2];
        for (int i = 0; i < src.length; i++) {
            int index = 0;
            for (int j = 0; j < src[i].length; j++) {
                if (j % 2 == 0) {
                    //result[i][index++] = (short) (src[i][j] / 1000F * 200);
                    result[i][index++] = (short) (src[i][j] * Const.SHORT_MV_GAIN * 200);
                }
            }

        }
        return result;
    }


}
