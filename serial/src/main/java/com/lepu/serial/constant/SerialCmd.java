package com.lepu.serial.constant;

import com.lepu.serial.constant.SerialContent;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.uitl.CRCUitl;

import java.util.Arrays;

/**
 * 串口命令合成
 */
public class SerialCmd {

    static byte index = 0;

    /**
     * 开始传输数据命令
     *
     * @return
     */
    public static byte[] cmdDataStart() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_COMMON, SerialContent.TYPE_DATA_START, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 停止传输命令
     *
     * @return
     */
    public static byte[] cmdDataStop() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_COMMON, SerialContent.TYPE_DATA_STOP, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    public static void main(String[] args) {
        System.out.println(CRCUitl.CRC8(cmdDataStart()));

    }

}
