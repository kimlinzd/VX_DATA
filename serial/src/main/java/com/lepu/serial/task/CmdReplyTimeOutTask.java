package com.lepu.serial.task;

import static java.lang.Thread.sleep;

import com.lepu.serial.listener.CmdNibpReplyListener;
import com.lepu.serial.listener.CmdReplyListener;
import com.lepu.serial.obj.CmdReply;

/**
 * 命令超时监听线程
 */
public class CmdReplyTimeOutTask extends Thread{
    CmdReplyListener cmdReplyListener;
    CmdNibpReplyListener cmdNibpReplyListener;
    CmdReply cmdReply;
    long timeOut;


    public CmdReplyTimeOutTask(CmdReplyListener cmdReplyListener, CmdReply cmdReply,long timeOut) {
        this.cmdReplyListener = cmdReplyListener;
        this.cmdReply = cmdReply;
        this.timeOut=timeOut;
    }

    public CmdReplyTimeOutTask(CmdNibpReplyListener cmdNibpReplyListener, CmdReply cmdReply, long timeOut) {
        this.cmdNibpReplyListener = cmdNibpReplyListener;
        this.cmdReply = cmdReply;
        this.timeOut=timeOut;
    }

    @Override
    public void run() {
        try {
            sleep(timeOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (cmdReplyListener!=null){
            cmdReplyListener.onTimeOut(cmdReply);
        }
        if (cmdNibpReplyListener!=null){
            cmdNibpReplyListener.onTimeOut(cmdReply);
        }

    }

    public CmdReplyListener getCmdReplyListener() {
        return cmdReplyListener;
    }

    public void setCmdReplyListener(CmdReplyListener cmdReplyListener) {
        this.cmdReplyListener = cmdReplyListener;
    }

    public CmdReply getCmdReply() {
        return cmdReply;
    }

    public void setCmdReply(CmdReply cmdReply) {
        this.cmdReply = cmdReply;
    }
    public void cencel(){
        cmdReplyListener=null;
        cmdNibpReplyListener=null;
    }
}
