package com.lepu.serial.task;

/**
 * 心电图保存任务的bean
 */
public class EcgSaveTaskBean {
    /**
     * ECG数据
     */
    private byte[] ecgdata;


    private EcgSaveTaskBeanType ecgSaveTaskBeanType;


    public byte[] getEcgdata() {
        return ecgdata;
    }

    public void setEcgdata(byte[] ecgdata) {
        this.ecgdata = ecgdata;
    }

    public EcgSaveTaskBeanType getEcgSaveTaskBeanType() {
        return ecgSaveTaskBeanType;
    }

    public void setEcgSaveTaskBeanType(EcgSaveTaskBeanType ecgSaveTaskBeanType) {
        this.ecgSaveTaskBeanType = ecgSaveTaskBeanType;
    }

    public enum EcgSaveTaskBeanType {
        /**
         * 添加缓存数据
         */
        ECG_SAVE_TASK_BEAN_TYPE_ADD_CACHE_DATA,
        /**
         * 储存缓存数据
         */
        ECG_SAVE_TASK_BEAN_TYPE_SAVE_CACHE_DATA,
        /**
         * 读取文件数据
         */
        ECG_SAVE_TASK_BEAN_TYPE_READ_FILE_DATA,

    }
}
