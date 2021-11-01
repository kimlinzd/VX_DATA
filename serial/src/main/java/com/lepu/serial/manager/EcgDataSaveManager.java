package com.lepu.serial.manager;

import android.os.AsyncTask;
import android.util.Log;

import com.lepu.serial.constant.SerialContent;
import com.lepu.serial.task.BaseTaskBean;
import com.lepu.serial.task.DataSaveTask;
import com.lepu.serial.task.EcgSaveTaskBean;
import com.lepu.serial.task.OnTaskListener;
import com.lepu.serial.task.SerialPortDataTask;
import com.lepu.serial.task.SerialTaskBean;
import com.lepu.serial.uitl.ByteUtils;
import com.lepu.serial.uitl.CRCUitl;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 心电数据数据保存
 */
public class EcgDataSaveManager {

    private static EcgDataSaveManager instance = null;
    //数据定时保存任务
    private ScheduledExecutorService ecgDataSaveTimer = null;
    //
    DataSaveTask dataSaveTask;
    //缓存的数据
    byte[] cacheData = new byte[0];


    public static EcgDataSaveManager getInstance() {
        if (instance == null) {
            instance = new EcgDataSaveManager();
        }
        return instance;
    }

    public void init() {

        if (ecgDataSaveTimer == null) {
            ecgDataSaveTimer = new ScheduledThreadPoolExecutor(1);
            ecgDataSaveTimer.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                  saveCacheEcgData();
                 }
            }, 30000, 30000, TimeUnit.MILLISECONDS);//每30秒保存一次数据
        }
        dataSaveTask = DataSaveTask.getInstance();
        dataSaveTask.setOnTaskListener(onTaskListener);
    }

    /**
     * 添加缓存ecg数据
     *
     * @param data
     */
    public void addCacheEcgDate(byte[] data) {
        //    concurrentLinkedQueue.offer(data);
        cacheData = ByteUtils.add(cacheData, data);
    }

    /**
     * 保存ecg数据
     */
    public void saveCacheEcgData() {
        if (cacheData == null) {
            return;
        }



    }

    OnTaskListener<BaseTaskBean<EcgSaveTaskBean>> onTaskListener = new OnTaskListener<BaseTaskBean<EcgSaveTaskBean>>() {
        @Override
        public void exNextTask(BaseTaskBean<EcgSaveTaskBean> task) {
            new Thread(() -> {
                if (task.taskBaen.getEcgSaveTaskBeanType() == EcgSaveTaskBean.EcgSaveTaskBeanType.ECG_SAVE_TASK_BEAN_TYPE_ADD_CACHE_DATA) {
                    //添加缓存数据
                    addCacheEcgDate(task.taskBaen.getEcgdata());
                } else if (task.taskBaen.getEcgSaveTaskBeanType() == EcgSaveTaskBean.EcgSaveTaskBeanType.ECG_SAVE_TASK_BEAN_TYPE_SAVE_CACHE_DATA) {
                    //储存缓存数据
                    saveCacheEcgData();
                } else if (task.taskBaen.getEcgSaveTaskBeanType() == EcgSaveTaskBean.EcgSaveTaskBeanType.ECG_SAVE_TASK_BEAN_TYPE_READ_FILE_DATA) {

                }

                dataSaveTask.exOk(task);
            }).start();

        }

        @Override
        public void noTask() {
            Log.d("noTask", cacheData.length + "");
        }
    };


}
