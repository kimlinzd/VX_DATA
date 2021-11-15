package com.lepu.serial.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.lepu.serial.constant.SaveDataFlieContent;
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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
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
 * <p>
 * 心电数据数据保存
 */
public class EcgDataSaveManager {

    private static EcgDataSaveManager instance = null;
    //数据定时保存任务
    private ScheduledExecutorService mEcgDataSaveTimer = null;
    //任务
    DataSaveTask dataSaveTask;
    //缓存的数据
    byte[] cacheData = new byte[0];
    //上下文
    Context mContext;


    public static EcgDataSaveManager getInstance() {
        if (instance == null) {
            instance = new EcgDataSaveManager();
        }
        return instance;
    }

    public void init(Context context) {

        this.mContext = context;
        dataSaveTask = DataSaveTask.getInstance();
        dataSaveTask.setOnTaskListener(onTaskListener);
    }

    /**
     * 添加缓存ecg数据
     *
     * @param data
     */
    public void addCacheEcgDate(byte[] data) {
        synchronized (cacheData) {
            if (TextUtils.isEmpty(SaveDataFlieContent.PATITENT_ID)) {
                return;
            }
            cacheData = ByteUtils.add(cacheData, data);
        }

        //   Log.d("addCacheEcgDate","addCacheEcgDate");
    }

    /**
     * 保存ecg数据
     */
    public void saveCacheEcgData(String time) {
        synchronized (cacheData) {
            if (cacheData == null || cacheData.length == 0 || TextUtils.isEmpty(SaveDataFlieContent.PATITENT_ID)) {
                return;
            }

            try {
                //获取患者ID储存的文件
                File pantitentEcgFile = FileUtil.getEcgFilePath(mContext, SaveDataFlieContent.PATITENT_ID);

                List<File> pantitentEcgFileList = FileUtil.listFileSortByModifyTime(pantitentEcgFile.getAbsolutePath());
                //判断已经储存了多久了 单个病人心电图最多保存140个小时 正常情况是4个小时一个文件 有异常情况 中间中断
                if (pantitentEcgFileList.size() > 0) {
                    //获取该患者总储存时间
                    long totalSaveTime = 0;
                    //遍历获取该患者的储存总时间
                    for (int i = 0; i < pantitentEcgFileList.size(); i++) {
                        String fileName = pantitentEcgFileList.get(i).getName();
                        String[] fileNames = fileName.split("-");
                        totalSaveTime = totalSaveTime + Long.parseLong(fileNames[0]) - Long.parseLong(fileNames[1]);
                    }
                    //如果超过140个小时 504,000,000 144个小时518,400,000  多保存4个小时 好删除文件
                    if (totalSaveTime > ((3600000 * SaveDataFlieContent.ECG_DATA_SAVE_MAX_TIME) + 14400000)) {
                        //把第一个文件删掉
                        pantitentEcgFileList.get(0).delete();
                    }

                }

                //判断空间是否足够  预留500M的空间
                if (FileUtil.queryStorage() > SaveDataFlieContent.RESERVED_SPACE) {
                    //获取ecg文件夹
                    File ecgFile = FileUtil.getEcgFilePath(mContext, null);
                    //删除文件 把时间最早的文件删除掉   现在先把最前面的文件删掉吧
                    List<File> ecgfileList = FileUtil.listFileSortByModifyTime(ecgFile.getAbsolutePath());
                    if (ecgfileList.size() > 0) {
                        ecgfileList.get(0).delete();
                    }
                }
                //写入开始 先获取最后一个文件，看看能不能续上 不能续上就开启新的文件
                if (pantitentEcgFileList.size() == 0) {
                    //写入新文件
                    FileUtil.writeBytesToNewFile(pantitentEcgFile, cacheData, Long.parseLong(time));
                    Log.d("文件保存","写入新文件");
                } else {
                    //文件夹里面已经有文件了 获取最新的文件看看能不能继续保存
                    File lastSaveFile = pantitentEcgFileList.get(pantitentEcgFileList.size() - 1);
                    String[] lastSaveFileName = lastSaveFile.getName().split("-");
                    //判断时间差 刚好是30秒 给他2秒的时间差
                    boolean timeDifference=(Long.parseLong(time) - Long.parseLong(lastSaveFileName[1]) < 32000);
                    //一个文件最多储存4个小时的数据 所以在这也判断一下
                    boolean  timeMax=Long.parseLong(lastSaveFileName[0]) - Long.parseLong(time) <
                            (SaveDataFlieContent.ECG_DATA_SINGLE_FILE_SAVE_MAX_TIME * 1000 * 60 * 60);
                    if ( timeDifference&&timeMax ) {
                        //直接继续在这个文件写
                        FileUtil.writeBytesToOldFile(lastSaveFile, cacheData, Long.parseLong(time));
                        Log.d("文件保存","直接继续在这个文件写");
                    } else {
                        //有时间差表示一个文件已经写满4个小时或者是 中途出现意外 导致数据丢失 可能是程序奔溃 系统重启等
                        //写入新文件
                        FileUtil.writeBytesToNewFile(pantitentEcgFile, cacheData, Long.parseLong(time));
                        Log.d("文件保存","有时间差 写入新文件");
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("保存数据", "长度--" + cacheData.length);
            cacheData = new byte[0];
            //     Log.d("保存数据 保存后的长度", "长度--" + cacheData.length);

        }
    }

    OnTaskListener<BaseTaskBean<EcgSaveTaskBean>> onTaskListener = new OnTaskListener<BaseTaskBean<EcgSaveTaskBean>>() {
        @Override
        public void exNextTask(BaseTaskBean<EcgSaveTaskBean> task) {
            if (task.taskBaen.getEcgSaveTaskBeanType() == EcgSaveTaskBean.EcgSaveTaskBeanType.ECG_SAVE_TASK_BEAN_TYPE_ADD_CACHE_DATA) {
                //添加缓存数据
                addCacheEcgDate(task.taskBaen.getEcgdata());
            } else if (task.taskBaen.getEcgSaveTaskBeanType() == EcgSaveTaskBean.EcgSaveTaskBeanType.ECG_SAVE_TASK_BEAN_TYPE_SAVE_CACHE_DATA) {
                //储存缓存数据
                saveCacheEcgData(task.taskNo);
            } else if (task.taskBaen.getEcgSaveTaskBeanType() == EcgSaveTaskBean.EcgSaveTaskBeanType.ECG_SAVE_TASK_BEAN_TYPE_READ_FILE_DATA) {
                //读取数据

            }
            dataSaveTask.exOk(task);


        }

        @Override
        public void noTask() {
            //  Log.d("noTask", cacheData.length + "");
        }
    };

    public void startEcgDataSaveTimer() {
        if (mEcgDataSaveTimer == null) {
            mEcgDataSaveTimer = new ScheduledThreadPoolExecutor(1);
            mEcgDataSaveTimer.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {

                    EcgSaveTaskBean ecgSaveTaskBean = new EcgSaveTaskBean();
                    ecgSaveTaskBean.setEcgSaveTaskBeanType(EcgSaveTaskBean.EcgSaveTaskBeanType.ECG_SAVE_TASK_BEAN_TYPE_SAVE_CACHE_DATA);
                    BaseTaskBean<EcgSaveTaskBean> baseTaskBean = new BaseTaskBean<>();
                    baseTaskBean.taskNo = String.valueOf(System.currentTimeMillis());
                    baseTaskBean.taskBaen = ecgSaveTaskBean;
                    EcgDataSaveManager.getInstance().dataSaveTask.addTask(baseTaskBean);
                }
            }, 30000, 30000, TimeUnit.MILLISECONDS);//每30秒保存一次数据
        }
    }

    public void stopEcgDataSaveTimer() {
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

    /**
     * 设置患者ID  必须有患者ID 才回开始保存心电图信息
     *
     * @param patitentId
     */
    public void setPatitentId(String patitentId) {
         SaveDataFlieContent.PATITENT_ID = patitentId;
        //保存任务开始
        startEcgDataSaveTimer();
    }

    /**
     * 解除患者
     */
    public void dismissPatitentId() {
        SaveDataFlieContent.PATITENT_ID = null;
        //结束保存任务
        stopEcgDataSaveTimer();
    }


}
