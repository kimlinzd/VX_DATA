package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * 血压模块信息
 */
public class NibpModuleInfo implements Serializable,Cloneable{
    /**
     * 主MCU软件版本
     * 格式为MV:W.X.Y.Z
     * 其中W,X,Y,Z为0~99的整数
     * 如MV:0.0.0.17
     */
    String mainMCU;
    /**
     * 副MCU软件版本
     * 格式为PV:W.X.Y.Z
     * 其中W,X,Y,Z为0~99的整数
     * 如PV:0.0.1.0
     */
    String auxiliaryMCU;
    /**
     * 模块名称
     */
    String moduleName;

    public NibpModuleInfo() {
    }

    public NibpModuleInfo(byte[] buf) {
        try {
            String str = new String(buf, "utf-8");
            String[] strArr = str.split(" ");
            mainMCU = strArr[0];
            auxiliaryMCU = strArr[0];
            moduleName = strArr[0];
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getMainMCU() {
        return mainMCU;
    }

    public void setMainMCU(String mainMCU) {
        this.mainMCU = mainMCU;
    }

    public String getAuxiliaryMCU() {
        return auxiliaryMCU;
    }

    public void setAuxiliaryMCU(String auxiliaryMCU) {
        this.auxiliaryMCU = auxiliaryMCU;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @NonNull
    @Override
    protected NibpModuleInfo clone() throws CloneNotSupportedException {
        return (NibpModuleInfo)super.clone();
    }
}
