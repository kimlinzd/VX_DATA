package com.lepu.serial.task;


import android.os.AsyncTask;

/**
 * 任务请求管理
 * 当第一个任务执行完成，发现列队中还存在任务， 将继续执行下一个任务
 */
public interface OnTaskListener<T extends BaseTaskBean>   {
    /**
     * 执行下一个任务
     *
     * @param task
     */
    void exNextTask(T task);

    /**
     * 所有任务执行完成
     */
    void noTask();
}