package com.lepu.serial.obj;

import com.lepu.serial.constant.SerialContent;
import com.lepu.serial.uitl.CRCUitl;

/**
 * 串口消息处理 生成数据包和数据包解析
 */
public class SerialMsg {
    int len;  //Length		：报文总长度  （6+ Contents长度）
    byte index; //Index		：报文序列号，范围0x00 ~ 0xFF       （报文序列号（Index字节）用于丢包测试）
    byte type; //Class		：报文类型，  范围0xF0 ~ 0xFB
    SerialContent content;
    byte crc;


    public static final byte TYPE_CMD = (byte) 0xF0;  //命令包	Master → Slave
    public static final byte TYPE_ACK = (byte) 0xf1;  //命令确认包（若有回复包，就不发确认包）	Master ← Slave
    public static final byte TYPE_REPLY = (byte) 0xf2;  //回复包	Master ← Slave
    public static final byte TYPE_DATA = (byte) 0xf3;  //数据包	Master ← Slave
    public static final byte TYPE_STATUS = (byte) 0xf4;  //如心跳包，异常状态包等等（主动传输）	双向
    public static final byte TYPE_UPDATE = (byte) 0xf5;    //升级包	Master → Slave

    /**
     * 解析数据包 例如0xAA  0x55  0x08  0x00  0xF0  0x00  0x05  0xBA
     * SYNC_H		：同步包头高八位，固定为 0xAA
     * SYNC_L		：同步包头低八位，固定为 0x55
     * Length		：报文总长度（6+Contents长度）
     * Index		：报文序列号，范围0x00 ~ 0xFF（报文序列号（Index字节）用于丢包测试）
     * Class		：报文类型，范围0xF0 ~ 0xFB
     * Contents		：业务内容
     * CRC		：校验位，使用循环冗余校验方式计算该字节，计算方式见附录1
     */
    public SerialMsg(byte[] buf) {
        len = buf[2];
        index = buf[3];
        type = buf[4];
        byte[] contentbyte = new byte[len-6];
        System.arraycopy(buf, 5, contentbyte, 0, len-6);
        content=new SerialContent(contentbyte);
        crc=buf[buf.length-1];

    }

    /**
     * 生成命令包
     */
    public SerialMsg(byte index, byte type, SerialContent content) {
        len = content.data == null ? 8 : (content.data.length + 8);
        this.index = index;
        this.type = type;
        this.content = content;
    }

    public byte[] toBytes() {
        byte[] buf = new byte[len];
        buf[0] = (byte) 0xAA;
        buf[1] = (byte) 0x55;
        buf[2] = (byte) len;
        buf[3] = (byte) (len >> 8);
        buf[4] = type;
        System.arraycopy(content.toBytes(), 0, buf, 5, content.toBytes().length);

        // todo: 计算crc
        crc = CRCUitl.getCRC8(buf, len - 1);
        buf[len - 1] = crc;
        return buf;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte getIndex() {
        return index;
    }

    public void setIndex(byte index) {
        this.index = index;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public SerialContent getContent() {
        return content;
    }

    public void setContent(SerialContent content) {
        this.content = content;
    }

    public byte getCrc() {
        return crc;
    }

    public void setCrc(byte crc) {
        this.crc = crc;
    }
}
