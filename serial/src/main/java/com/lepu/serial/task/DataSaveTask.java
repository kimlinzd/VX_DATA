package com.lepu.serial.task;

/**
 *    任务  任务/ 需要排队的任务， 支持扩展
 */
public class DataSaveTask extends BaseTask{


    private static DataSaveTask instance = null;

    public static DataSaveTask getInstance() {
        if (instance == null) {
            instance = new DataSaveTask();
        }
        return instance;
    }


}

