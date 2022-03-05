package com.lepu.vx_data;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.lepu.serial.constant.EventMsgConst;
import com.lepu.serial.constant.SerialCmd;
import com.lepu.serial.constant.SerialContent;
import com.lepu.serial.manager.SerialPortManager;
import com.lepu.serial.obj.EcgData;
import com.lepu.serial.obj.NibpCP200HZData;
import com.lepu.serial.obj.NibpCP5HZData;
import com.lepu.serial.obj.NibpModuleInfo;
import com.lepu.serial.obj.NibpPramAndStatus;
import com.lepu.serial.obj.NibpWorkingStatus;
import com.lepu.serial.obj.RespData;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.obj.SpO2Data;
import com.lepu.serial.obj.SpO2WaveData;
import com.lepu.serial.obj.TempData;

import java.io.File;

/**
 * 把数据解析成OBJ 放到eventbus线程
 */
public class SaveTask extends Thread {

    byte[] data;
    File file;


    public SaveTask(byte[]  data, File file) {
        this.data = data;
        this.file=file;
    }


    @Override
    public void run() {
        FileUtils.write3File(file,data);
    }


}
