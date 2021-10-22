package com.lepu.algorithm.ecg.manager;

//import com.Carewell.OmniEcg.jni.JniFilter;
//import com.Carewell.OmniEcg.jni.JniFilterNew;
//import com.Carewell.OmniEcg.jni.JniSample;
//import com.ecg.entity.NotifyFilterBean;
//import com.ecg.utils.CycleBuffer;
//import com.main.utils.Const;
//import com.main.utils.WaveEncodeUtil;
//import com.setting.entity.ConfigBean;
//import com.setting.entity.dictionary.EcgSettingConfigEnum;
//import com.setting.manager.SettingManager;

import com.lepu.algorithm.Carewell.OmniEcg.jni.JniFilter;
import com.lepu.algorithm.Carewell.OmniEcg.jni.JniFilterNew;
import com.lepu.algorithm.Carewell.OmniEcg.jni.JniSample;
import com.lepu.algorithm.ecg.entity.ConfigBean;
import com.lepu.algorithm.ecg.entity.NotifyFilterBean;
import com.lepu.algorithm.ecg.utils.Const;
import com.lepu.algorithm.ecg.utils.CycleBuffer;

import java.util.Arrays;
import java.util.LinkedList;

import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;
import com.lepu.algorithm.ecg.utils.WaveEncodeUtil;


/**
 * 滤波使用
 */
public class FilterManager {
    private static FilterManager instance = null;

    private NotifyFilterBean notifyFilterBean = new NotifyFilterBean();
    //滤波器变化
    private boolean powerFrequencyChange = true;
    private boolean powerFrequencyHpChange = true;
    private CycleBuffer filterCycleBuffer = null;

    private FilterManager(){

    }

    public static FilterManager getInstance(){
        if(instance == null){
            instance = new FilterManager();
        }
        return instance;
    }

    private void init(){

    }

    private void destroy(){

    }

    //======================================

    /**
     * 重置滤波器
     */
    public synchronized void resetFilter(){
        JniFilterNew.getInstance().resetFilter();
    }

    /**
     *
     * @param configBean
     * @param ecgDataArray
     * @param filterLeadNum 滤波通道数据。如果数据中包含，导联脱落通道，起搏通道数据。需要-2
     * @return
     */
    public synchronized short[][] filter(ConfigBean configBean, short[][] ecgDataArray, int filterLeadNum, LinkedList<Boolean> leadOffElectrode){
//        if(JniSample.getInstance().getFilterType() == 0){
//            ecgDataArray = filterB120(configBean,ecgDataArray);
//        }else{
//            //滤波前，把数据拷贝到循环缓冲里
//            if(filterCycleBuffer == null){
//                filterCycleBuffer = new CycleBuffer(ecgDataArray.length,Const.FILTER_ARRAY_COUNT);
//            }
//
//            filterCycleBuffer.putECGData(ecgDataArray);
//            ecgDataArray = filterC120(configBean,ecgDataArray[0].length);
//        }

        // 将导联脱落转化为整形数值：脱落为1，未脱落为0；
        // 将10/16电极的脱落信息，转换为8/14通道的脱落信息，与数据对应；
        // I导联数据，对应导联脱落标识位为LA/L导联；
        // II导联数据，对应导联脱落标志位为LL/F导联

        int len = ecgDataArray.length;
        int[] leadOffArr = new int[len-2];

        if (null != leadOffElectrode) {
            LinkedList<Boolean> leadOff = new LinkedList<>(leadOffElectrode); // 不直接操作原列表
            for(int i=leadOff.size();i<16;i++) { // 将所有导联脱落标识填充到与导联数据组数一致
                leadOff.add(true);
            }
            for(int i=0;i<16;i++) { // 根据不同的导联类型，补充缺失的导联位置为脱落(注：补导联脱落标识的位置要与组数一致)
                switch (configBean.getEcgSettingBean().getLeadType()){
                    case LEAD_9:
                        if(i==5)
                        {
                            leadOff.set(i+3, leadOff.get(i+1));//V5
                            leadOff.set(i+1, leadOff.get(i));//V3
                            leadOff.set(i, true);//5
                            leadOff.set(i+2, true);//7
                        }
                        break;
                    case LEAD_12:
                    case LEAD_18:
                        break;
                    case LEAD_15_STANDARD_RIGHT:
                        if(i==10)
                        {
                            leadOff.set(i+5, leadOff.get(i+2));//V5R
                            leadOff.set(i+4, leadOff.get(i+1));//V4R
                            leadOff.set(i+3, leadOff.get(i));//V3R
                            leadOff.set(i+2, true);//V9
                            leadOff.set(i+1, true);//V8
                            leadOff.set(i, true);//V7
                        }
                        break;
                    case LEAD_15_STANDARD_AFTER:
                        break;
                    case LEAD_15_CHILD:
                        if(i==10)
                        {
                            leadOff.set(i+4, leadOff.get(i+2));//V4R
                            leadOff.set(i+3, leadOff.get(i+1));//V3R
                            leadOff.set(i, leadOff.get(i));//V7
                            leadOff.set(i+5, true);//V5R
                            leadOff.set(i+2, true);//V9
                            leadOff.set(i+1, true);//V8
                        }
                        break;
                }
            }
            for (int i = 0; i < len; i++) {
                if (i == 2 || i == 3) { // 2和3为另外两个计算电极
                    continue;
                }
                if(i > 3) {
                    if (leadOff.get(i)) {
                        leadOffArr[i-2] = 1;
                    } else {
                        leadOffArr[i-2] = 0;
                    }
                } else {
                    if (leadOff.get(i)) {
                        leadOffArr[i] = 1;
                    } else {
                        leadOffArr[i] = 0;
                    }
                }
            }
        }
        //new filter
        ecgDataArray = filterNew(configBean,ecgDataArray,filterLeadNum,leadOffArr);
        return ecgDataArray;
    }

    /**
     * 肌电 低通，不能调用多次
     * @param inputDataCount
     * @return
     */
    private short[][] filterC120(ConfigBean configBean,int inputDataCount) {
        short[][] outDataArray = null;
        if(filterCycleBuffer.getDataLen() <= Const.FILTER_JUMP_DATA_COUNT*2){
            return null;
        }

        JniFilter jniFilter = JniFilter.getInstance();
        notifyFilterBean.setOutDataLen(inputDataCount);
        float[][] mvDataArray;

        //肌电 低通 是否开启了滤波
        EcgSettingConfigEnum.LeadFilterType filterEmg = configBean.getEcgSettingBean().getFilterEmg();
        EcgSettingConfigEnum.LeadFilterType filterLowpass = configBean.getEcgSettingBean().getFilterLowpass();
        if(filterEmg != EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_CLOSE || filterLowpass != EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_300){
            mvDataArray = WaveEncodeUtil.switchEcgDataArray(getEcgDataArrayFilterLowPass(inputDataCount));
            if(mvDataArray == null){
                return null;
            }
            notifyFilterBean.setDataArray(mvDataArray);

            if(filterEmg == EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_25) {
                jniFilter.electromyography25(mvDataArray,mvDataArray[0].length,1.0f,false, notifyFilterBean);
            } else if(filterEmg == EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_35) {
                jniFilter.electromyography35(mvDataArray,mvDataArray[0].length,1.0f,false, notifyFilterBean);
            } else if(filterEmg == EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_45) {
                jniFilter.electromyography45(mvDataArray,mvDataArray[0].length,1.0f,false, notifyFilterBean);
            }else{
                //肌电滤波 close,启用低通滤波
                //低通滤波
                if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_75) {
                    jniFilter.lowPass75(mvDataArray,mvDataArray[0].length,1.0f,false, notifyFilterBean);
                }
//                else if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_90) {
//                    jniFilter.lowPass90(mvDataArray,mvDataArray[0].length,1.0f,false, notifyFilterBean);
//                }
                else if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_100) {
                    jniFilter.lowPass100(mvDataArray,mvDataArray[0].length,1.0f,false, notifyFilterBean);
                }
//                else if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_165) {
//                    jniFilter.lowPass165(mvDataArray,mvDataArray[0].length,1.0f,false, notifyFilterBean);
//                }
            }
            mvDataArray = notifyFilterBean.getDataArray();
        }else{
            //工频滤波是否打开
            mvDataArray = WaveEncodeUtil.switchEcgDataArray(getEcgDataArrayFilterTempJiaoliu(inputDataCount));
            if(mvDataArray == null){
                return null;
            }
        }

        //工频滤波
        EcgSettingConfigEnum.LeadFilterType filterAc = configBean.getEcgSettingBean().getFilterAc();
        EcgSettingConfigEnum.LeadFilterType filterHz = configBean.getSystemSettingBean().getLeadFilterTypeAc();
        if(filterAc == EcgSettingConfigEnum.LeadFilterType.FILTER_AC_OPEN) {
            //打开
            notifyFilterBean.setDataArray(mvDataArray);
            boolean powerF = isPowerFrequencyChange();

            //工频滤波
            if(filterHz == EcgSettingConfigEnum.LeadFilterType.FILTER_AC_50_HZ){
                //50hz
                if(JniFilter.getInstance().getPowerFrequency50attenuation() == 1){
                    jniFilter.powerFrequency50attenuation(mvDataArray,mvDataArray[0].length,1.0f,powerF, notifyFilterBean);
                }else{
                    jniFilter.powerFrequency50(mvDataArray,mvDataArray[0].length,1.0f,powerF, notifyFilterBean);
                }
            }else{
                //60hz
                if(JniFilter.getInstance().getPowerFrequency60attenuation() == 1){
                    jniFilter.powerFrequency60attenuation(mvDataArray,mvDataArray[0].length,1.0f,powerF, notifyFilterBean);
                }else{
                    jniFilter.powerFrequency60(mvDataArray,mvDataArray[0].length,1.0f,powerF, notifyFilterBean);
                }
            }
            mvDataArray = notifyFilterBean.getDataArray();
            setPowerFrequencyChange(false);
        }

        //漂移
        EcgSettingConfigEnum.LeadFilterType filterBaseline = configBean.getEcgSettingBean().getFilterBaseline();
        if(filterBaseline == EcgSettingConfigEnum.LeadFilterType.FILTER_BASELINE_001){
            //0.01
            boolean powerHp = isPowerFrequencyHpChange();
            notifyFilterBean.setDataArray(mvDataArray);
            jniFilter.HP0p01(mvDataArray,mvDataArray[0].length,1.0f,powerHp, notifyFilterBean);
            mvDataArray = notifyFilterBean.getDataArray();

            setPowerFrequencyHpChange(false);
        }

        outDataArray = WaveEncodeUtil.switchEcgDataArray(mvDataArray);
        return outDataArray;
    }

    /**
     * 滤波 b120
     * @param configBean
     * @param ecgDataArray
     * @return
     */
    private short[][] filterB120(ConfigBean configBean,short[][] ecgDataArray) {

        int inputDataCount = ecgDataArray[0].length;
        int sampleRate = Const.SAMPLE_RATE;

        JniSample jniFilterB120 = JniSample.getInstance();
        notifyFilterBean.setOutDataLen(inputDataCount);

        short[][] tmpDataArray = ecgDataArray;

        //肌电 低通 是否开启了滤波
        EcgSettingConfigEnum.LeadFilterType filterEmg = configBean.getEcgSettingBean().getFilterEmg();
        EcgSettingConfigEnum.LeadFilterType filterLowpass = configBean.getEcgSettingBean().getFilterLowpass();

        if(filterEmg != EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_CLOSE || filterLowpass != EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_300){
            notifyFilterBean.setShortDataArray(tmpDataArray);

            if(filterEmg == EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_25) {
                jniFilterB120.lowPass(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,25,sampleRate);
            } else if(filterEmg == EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_35) {
                jniFilterB120.lowPass(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,35,sampleRate);
            } else if(filterEmg == EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_45) {
                jniFilterB120.lowPass(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,45,sampleRate);
            }else{
                //肌电滤波 close,启用低通滤波
                //低通滤波
                if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_75) {
                    jniFilterB120.lowPass(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,75,sampleRate);
                }
//                else if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_90) {
//                    jniFilterB120.lowPass(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,90,sampleRate);
//                }
                else if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_100) {
                    jniFilterB120.lowPass(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,100,sampleRate);
                }
//                else if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_165) {
//                    jniFilterB120.lowPass(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,165,sampleRate);
//                }
            }

            tmpDataArray = notifyFilterBean.getShortDataArray();
        }

        //工频滤波
        EcgSettingConfigEnum.LeadFilterType filterAc = configBean.getEcgSettingBean().getFilterAc();
        EcgSettingConfigEnum.LeadFilterType filterHz = configBean.getSystemSettingBean().getLeadFilterTypeAc();
        if(filterAc == EcgSettingConfigEnum.LeadFilterType.FILTER_AC_OPEN) {
            //c120 工频滤波
            JniFilter filter = JniFilter.getInstance();
            float[][] mvDataArray = WaveEncodeUtil.switchEcgDataArray(tmpDataArray);
            notifyFilterBean.setDataArray(mvDataArray);
            boolean powerF = isPowerFrequencyChange();

            //工频滤波
            if(filterHz == EcgSettingConfigEnum.LeadFilterType.FILTER_AC_50_HZ){
                //50hz
                if(JniFilter.getInstance().getPowerFrequency50attenuation() == 1){
                    filter.powerFrequency50attenuation(mvDataArray,mvDataArray[0].length,1.0f,powerF, notifyFilterBean);
                }else{
                    filter.powerFrequency50(mvDataArray,mvDataArray[0].length,1.0f,powerF, notifyFilterBean);
                }
            }else{
                //60hz
                if(JniFilter.getInstance().getPowerFrequency60attenuation() == 1){
                    filter.powerFrequency60attenuation(mvDataArray,mvDataArray[0].length,1.0f,powerF, notifyFilterBean);
                }else{
                    filter.powerFrequency60(mvDataArray,mvDataArray[0].length,1.0f,powerF, notifyFilterBean);
                }
            }
            mvDataArray = notifyFilterBean.getDataArray();
            setPowerFrequencyChange(false);
            tmpDataArray = WaveEncodeUtil.switchEcgDataArray(mvDataArray);
        }

        //漂移 会影响其它的，先注释掉
        EcgSettingConfigEnum.LeadFilterType filterBaseline = configBean.getEcgSettingBean().getFilterBaseline();
        if(filterBaseline == EcgSettingConfigEnum.LeadFilterType.FILTER_BASELINE_001){
            //0.01
            notifyFilterBean.setShortDataArray(tmpDataArray);
            jniFilterB120.highPass(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,0.01,sampleRate);
            tmpDataArray = notifyFilterBean.getShortDataArray();
        }

        return tmpDataArray;
    }

    /**
     *
     * @param configBean
     * @param ecgDataArray
     * @param filterLeadNum
     * @return
     */
    private short[][] filterNew(ConfigBean configBean,short[][] ecgDataArray,int filterLeadNum,int[] leadOffArr) {

        JniFilterNew jniFilter = JniFilterNew.getInstance();
        int inputDataCount = ecgDataArray[0].length;
        notifyFilterBean.setOutDataLen(inputDataCount);
//        if (SettingManager.getInstance().isDemoMode()) { // DEMO模式下，进入算法的导联标识需要置为“未脱落”
//            Arrays.fill(leadOffArr, 0);
//        }
        jniFilter.DCRecover(ecgDataArray,ecgDataArray[0].length, notifyFilterBean,filterLeadNum,leadOffArr);
        //dc后，数据值大，需要用int接收
        int[][] tmpDataArray = notifyFilterBean.getIntDataArray();

        //导联脱落通道，起搏通道，不用滤波.目前传的数据没有新加的2个通道了。所以不用-2
        //int filterLeadNum = ecgDataArray.length;//-2
        //notifyFilterBean.setShortDataArray(tmpDataArray);

        //漂移 高通
        if(Const.FILTER_ON_HP){
            EcgSettingConfigEnum.LeadFilterType filterBaseline = configBean.getEcgSettingBean().getFilterBaseline();
            if(filterBaseline == EcgSettingConfigEnum.LeadFilterType.FILTER_BASELINE_001){
                //0.01
                jniFilter.HP0p01(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            }else if(filterBaseline == EcgSettingConfigEnum.LeadFilterType.FILTER_BASELINE_005){
                //0.05
                jniFilter.HP0p05(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            }else if(filterBaseline == EcgSettingConfigEnum.LeadFilterType.FILTER_BASELINE_032){
                //0.32
                jniFilter.HP0p32(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            }else if(filterBaseline == EcgSettingConfigEnum.LeadFilterType.FILTER_BASELINE_067){
                //0.67
                jniFilter.HP0p67(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            }
        }

        //肌电 低通 是否开启了滤波
        EcgSettingConfigEnum.LeadFilterType filterEmg = configBean.getEcgSettingBean().getFilterEmg();
        EcgSettingConfigEnum.LeadFilterType filterLowpass = configBean.getEcgSettingBean().getFilterLowpass();

        if(filterEmg != EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_CLOSE){
            if(filterEmg == EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_25) {
                jniFilter.electromyography25(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            } else if(filterEmg == EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_35) {
                jniFilter.electromyography35(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            } else if(filterEmg == EcgSettingConfigEnum.LeadFilterType.FILTER_EMG_45) {
                jniFilter.electromyography45(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            }
        }else if(filterLowpass != EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_CLOSE){
            //低通滤波
            if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_75) {
                jniFilter.lowPass75(tmpDataArray,tmpDataArray[0].length,  notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            }else if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_100) {
                jniFilter.lowPass100(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            }else if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_150) {
                jniFilter.lowPass150(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            }else if(filterLowpass == EcgSettingConfigEnum.LeadFilterType.FILTER_LOWPASS_300) {
                jniFilter.lowPass300(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
                tmpDataArray = notifyFilterBean.getIntDataArray();
            }
        }

        //工频滤波
        EcgSettingConfigEnum.LeadFilterType filterAc = configBean.getEcgSettingBean().getFilterAc();
        EcgSettingConfigEnum.LeadFilterType filterHz = configBean.getSystemSettingBean().getLeadFilterTypeAc();
        if(filterAc == EcgSettingConfigEnum.LeadFilterType.FILTER_AC_OPEN) {
            //c120 工频滤波
            //工频滤波
            if(filterHz == EcgSettingConfigEnum.LeadFilterType.FILTER_AC_50_HZ){
                //50hz
                jniFilter.powerFrequency50(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
            }else{
                //60hz
                jniFilter.powerFrequency60(tmpDataArray,tmpDataArray[0].length, notifyFilterBean,filterLeadNum);
            }
            tmpDataArray = notifyFilterBean.getIntDataArray();
        }

        short[][] outDataArray = new short[tmpDataArray.length][tmpDataArray[0].length];
        for(int i=0;i<tmpDataArray.length;i++){
            for(int j=0;j<tmpDataArray[i].length;j++){
                outDataArray[i][j] = (short) tmpDataArray[i][j];
            }
        }

        return outDataArray;
    }

    public boolean isPowerFrequencyChange() {
        return powerFrequencyChange;
    }

    public void setPowerFrequencyChange(boolean powerFrequencyChange) {
        this.powerFrequencyChange = powerFrequencyChange;
    }

    public boolean isPowerFrequencyHpChange() {
        return powerFrequencyHpChange;
    }

    public void setPowerFrequencyHpChange(boolean powerFrequencyHpChange) {
        this.powerFrequencyHpChange = powerFrequencyHpChange;
    }

    /**
     * 获取低通 肌电滤波的数据
     * @return
     */
    public short[][] getEcgDataArrayFilterLowPass(int inputDataCount){
        return filterCycleBuffer.getFilterECGData(inputDataCount);
    }

    /**
     * 获取交流滤波的数据
     * @return
     */
    public short[][] getEcgDataArrayFilterTempJiaoliu(int inputDataCount){

        short[][] totalArray = filterCycleBuffer.getFilterECGData(inputDataCount);
        if(totalArray == null){
            return null;
        }
        short[][] destArray = new short[totalArray.length][inputDataCount];

        int  startPos =  totalArray[0].length -Const.FILTER_JUMP_DATA_COUNT -inputDataCount;
        for(int i=0;i<totalArray.length;i++){
            //算法规则，取最后200个点以前的数据
            System.arraycopy(totalArray[i],startPos,destArray[i],0,inputDataCount);
        }
        return destArray;
    }

    public CycleBuffer getFilterCycleBuffer() {
        return filterCycleBuffer;
    }

    public void setFilterCycleBuffer(CycleBuffer filterCycleBuffer) {
        this.filterCycleBuffer = filterCycleBuffer;
    }
}
