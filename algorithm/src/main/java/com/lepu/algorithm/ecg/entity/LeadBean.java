package com.lepu.algorithm.ecg.entity;

/**
 * Created by chuanyhu on 2019-06-15.
 */

public class LeadBean {
    String leadName;
    int  indexOfData;

    public int getIndexOfPrintData() {
        return indexOfPrintData;
    }

    public String getLeadName() {
        return leadName;
    }


    int  indexOfPrintData;

    public boolean isRhythmLead() {
        return isRhythmLead;
    }

    boolean isRhythmLead;

    public LeadBean(String leadName, int indexOfData, int indexOfPrintData, boolean rhythmLeadFlag)
    {
        this.leadName = leadName;
        this.indexOfData = indexOfData;
        this.indexOfPrintData = indexOfPrintData;
        this.isRhythmLead = rhythmLeadFlag;
    }


}
