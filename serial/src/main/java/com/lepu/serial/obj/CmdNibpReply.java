package com.lepu.serial.obj;

/**
 * 血压NIBP业务
 * 应答包
 */
public class CmdNibpReply {
    /**
     * 接收到的指令的TYPE位
     */
    byte cmdType;
    /**
     * ‘O’包	0x4F	表示接收到下发的指令,支持响应“O”的命令类型type = 0x01，0x02，0x04，0x05，0x0A，0x0B，0x0C，0x0E，0x0F，0x10，0x11，0x12
     * ‘K’包	0x4B	表示接收到的指令已经完成执行,支持响应“K”的命令类型type =
     * 0x01，0x02，0x0F，0x10，0x11，0x12
     * ‘B’包	0x42	表示正在执行其它的命令，或者正在自动归零,可能会遇到响应“B”的命令类型type = 0x01，0x02，0x04，0x05，0x0A，0x0B，0x0C，0x0E，0x0F，0x10，0x11，0x12
     * ‘A’包	0x41	表示接收到取消测量的指令,支持响应“A”的命令类型type = 0x03
     * ‘N’包	0x4E	表示接收到的包是无效的，所有只是校验码
     * 错了,或者是参数（数据）不在可设置范围内的命令都会响应“N”包
     * ‘S’包	0x53	表示模块进入休眠，支持响应“S”的命令类型type = 0x0D，在模块主动进入睡眠时，也会向上
     * 发送
     * ‘R’包	0x52	表示模块软件复位，支持响应“R”的命令类型type = 0x0E
     */
    byte ACK;

    public CmdNibpReply(byte[] buf) {
        cmdType = buf[0];
        ACK = buf[1];

    }

    public byte getCmdType() {
        return cmdType;
    }

    public void setCmdType(byte cmdType) {
        this.cmdType = cmdType;
    }

    public byte getACK() {
        return ACK;
    }

    public void setACK(byte ACK) {
        this.ACK = ACK;
    }
}
