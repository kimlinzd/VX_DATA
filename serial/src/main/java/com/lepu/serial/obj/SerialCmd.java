package com.lepu.serial.obj;

public class SerialCmd {

    static byte index = 0;

    public static byte[] cmdDataStart() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_COMMON, SerialContent.TYPE_DATA_START, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }
}
