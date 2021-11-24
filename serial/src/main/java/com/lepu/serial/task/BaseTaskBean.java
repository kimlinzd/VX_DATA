package com.lepu.serial.task;

/**
 *  基础 任务实体，
 */
public class BaseTaskBean<T> {
    /*
     *子ID，在列队中保持唯一。对应的是一条数据。
     */
    public String taskNo;

    /**
     * 父ID，通常以组的方式出现，关联一组相关的数据
     */
    public String planNo;

    public T taskBean;

}
