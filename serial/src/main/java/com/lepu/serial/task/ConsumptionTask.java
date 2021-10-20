package com.lepu.serial.task;

/**
 *   任务实体，
 */
public class ConsumptionTask{
    /*
     *  子ID，在列队中保持唯一。对应的是一条数据。
     */
    public String taskNo;


    /**
     * 数据
     */
    public byte[] data;
}
