package com.lepu.serial.listener;

import com.lepu.serial.obj.CmdReply;

/**
 * 串口连接监听
 */
public interface SerialConnentListener {
    //连接成功
    void onSuccess();

    //连接失败
    void onFail();
}
