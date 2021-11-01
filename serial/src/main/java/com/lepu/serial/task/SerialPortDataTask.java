package com.lepu.serial.task;

import android.text.TextUtils;
import android.util.Log;

import com.lepu.serial.manager.SerialPortManager;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *    任务  任务/ 需要排队的任务， 支持扩展
 */
public class SerialPortDataTask extends BaseTask{


    private static SerialPortDataTask instance = null;

    public static SerialPortDataTask getInstance() {
        if (instance == null) {
            instance = new SerialPortDataTask();
        }
        return instance;
    }


}

