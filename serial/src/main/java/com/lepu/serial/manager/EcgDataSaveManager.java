package com.lepu.serial.manager;

import android.content.Context;
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
import com.lepu.serial.uitl.FileUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * a) 病人数最多：100
 * b) 趋势数据最长：240小时/病人
 * c) 全息心电波形最长：140小时/病人
 * d) NIBP 测量结果最多：2000组/病人
 * e) 报警事件最多：1000条/病人
 * f) 报告最多：20份/病人
 *
 * 心电数据数据保存
 */
public class EcgDataSaveManager {

    private static EcgDataSaveManager instance = null;
    //数据定时保存任务
    private ScheduledExecutorService mEcgDataSaveTimer = null;
    //
    DataSaveTask dataSaveTask;
    //缓存的数据
    byte[] cacheData = new byte[0];
    //
    Context mContext;


    public static EcgDataSaveManager getInstance() {
        if (instance == null) {
            instance = new EcgDataSaveManager();
        }
        return instance;
    }

    public void init(Context context) {

        if (mEcgDataSaveTimer == null) {
            mEcgDataSaveTimer = new ScheduledThreadPoolExecutor(1);
            mEcgDataSaveTimer.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                  saveCacheEcgData();
                 }
            }, 30000, 30000, TimeUnit.MILLISECONDS);//每30秒保存一次数据
        }
        this.mContext=context;
        dataSaveTask = DataSaveTask.getInstance();
        dataSaveTask.setOnTaskListener(onTaskListener);
    }

    /**
     * 添加缓存ecg数据
     *
     * @param data
     */
    public void addCacheEcgDate(byte[] data) {
         cacheData = ByteUtils.add(cacheData, data);
         Log.d("addCacheEcgDate","addCacheEcgDate");
    }

    /**
     * 保存ecg数据
     */
    public void saveCacheEcgData() {
        if (cacheData == null) {
            return;
        }
        try {
            FileUtil.writeBytesToFile(mContext,cacheData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cacheData=new byte[0];


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
                    //读取数据

                }

                dataSaveTask.exOk(task);
            }).start();

        }

        @Override
        public void noTask() {
          //  Log.d("noTask", cacheData.length + "");
        }
    };

    public void stopTask(){
        if (mEcgDataSaveTimer != null) {
            try {
                // shutdown只是起到通知的作用
                // 只调用shutdown方法结束线程池是不够的
                mEcgDataSaveTimer.shutdown();
                // (所有的任务都结束的时候，返回TRUE)
                if (!mEcgDataSaveTimer.awaitTermination(0, TimeUnit.MILLISECONDS)) {
                    // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                    mEcgDataSaveTimer.shutdownNow();
                }
            } catch (InterruptedException e) {
                // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
                e.printStackTrace();
            } finally {
                mEcgDataSaveTimer.shutdownNow();
                mEcgDataSaveTimer = null;
            }
        }
    }


}
