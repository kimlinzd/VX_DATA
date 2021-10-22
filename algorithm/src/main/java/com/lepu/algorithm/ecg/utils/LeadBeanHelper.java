package com.lepu.algorithm.ecg.utils;

import com.lepu.algorithm.ecg.entity.LeadBean;
import com.lepu.algorithm.ecg.entity.dictionary.EcgSettingConfigEnum;

public class LeadBeanHelper {

    static int [] printLeadInd2DataLeadInd ={0,1,-1,-1,-1,-1,2,3,4,5,6,7,8,9,10,11,12,13};

//    public static LeadBean createLeadFromPrintLeadIndex(int printLeadInd, boolean rhythmLeadFlag, EcgSettingConfigEnum.LeadType leadType, boolean leadCabreraMode)
//    {
//        int dataLeadInd = printLeadInd2DataLeadInd[printLeadInd];
//        String[] leadNameArray = MainEcgManager.getInstance().getLeadNameArrayCurrentLeadModeArray(BaseApplication.getInstance(), leadType, leadCabreraMode);
//
//        if(rhythmLeadFlag){
//            switch (leadType){
//                case LEAD_9:
//                    if(printLeadInd == EcgSettingConfigEnum.LeadNameType.V3.ordinal()){
//                        printLeadInd -= 1;
//                    }else if(printLeadInd == EcgSettingConfigEnum.LeadNameType.V5.ordinal()){
//                        printLeadInd -= 2;
//                    }
//                    break;
//            }
//        }
//
//        return  new LeadBean(leadNameArray[printLeadInd],dataLeadInd,printLeadInd,rhythmLeadFlag );
//    }

}
