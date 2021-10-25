package com.lepu.algorithm.ecg.entity.dictionary;

public enum EcgDataAnalysisDiagnosisEnum {

    MODE_5000("5000 lib",0),
    MODE_CSE("CSE",1);

    private String name;
    private int diagnosisMode;

    EcgDataAnalysisDiagnosisEnum(String name, int diagnosisMode){
        this.name = name;
        this.diagnosisMode = diagnosisMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiagnosisMode() {
        return diagnosisMode;
    }

    public void setDiagnosisMode(int diagnosisMode) {
        this.diagnosisMode = diagnosisMode;
    }
}
