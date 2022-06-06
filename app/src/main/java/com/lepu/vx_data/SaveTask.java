package com.lepu.vx_data;

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
