package com.lepu.serial.obj;

import com.lepu.serial.constant.SerialContent;

/**
 * 命令回复
 */
public class CmdReply {

    CmdReplyType cmdReplyType;

    public CmdReply(byte type) {
        switch (type) {
            case SerialContent.TYPE_DATA_START:
                this.cmdReplyType = CmdReplyType.CMD_TYPE_DATA_START;
                break;
            case SerialContent.TYPE_DATA_STOP:
                this.cmdReplyType = CmdReplyType.CMD_TYPE_DATA_STOP;
                break;
            default:

        }
        this.cmdReplyType = cmdReplyType;
    }

    public CmdReplyType getCmdReplyType() {
        return cmdReplyType;
    }

    public void setCmdReplyType(CmdReplyType cmdReplyType) {
        this.cmdReplyType = cmdReplyType;
    }

    public enum CmdReplyType {
        /**
         * 收到启动数据传输命令回复
         */
        CMD_TYPE_DATA_START,

        /**
         * 收到停止数据传输命令回复
         */
        CMD_TYPE_DATA_STOP,
    }
}
