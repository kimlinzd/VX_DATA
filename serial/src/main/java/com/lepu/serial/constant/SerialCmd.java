package com.lepu.serial.constant;

import com.lepu.serial.enums.Ecg1mVEnum;
import com.lepu.serial.enums.EcgChn0IndexEnum;
import com.lepu.serial.enums.EcgModeEnum;
import com.lepu.serial.enums.PatientTypeEnum;
import com.lepu.serial.enums.RespLeadIndexEnum;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.uitl.CRCUitl;

import org.jetbrains.annotations.NotNull;

/**
 * 串口命令合成
 */
public class SerialCmd {

    static byte index = 0;


    /**
     * 复位参数板
     *
     * @return
     */
    public static byte[] cmdReset() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_PARAM, SerialContent.TYPE_RESET, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /**
     * 查询参数板信息
     *
     * @return
     */
    public static byte[] cmdInfo() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_PARAM, SerialContent.TYPE_INFO, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /**
     * 参数板总参数
     * 注：在启动传输命令前，需要发送此命令，用于同步设置
     *
     * @param patientTypeEnum
     * @param ecgModeEnum
     * @param ecgChn0IndexEnum
     * @param ecg1mVEnum
     * @param respLeadIndexEnum
     * @param apenaDelay  窒息报警时间，单位：秒
     * @return
     */
    public static byte[] cmdParam(@NotNull PatientTypeEnum patientTypeEnum, @NotNull EcgModeEnum ecgModeEnum
            , @NotNull EcgChn0IndexEnum ecgChn0IndexEnum, @NotNull Ecg1mVEnum ecg1mVEnum, @NotNull RespLeadIndexEnum respLeadIndexEnum
            , int apenaDelay) {
        byte[] data = new byte[6];
        data[0] = patientTypeEnum.getValue();
        data[1] = ecgModeEnum.getValue();
        data[2] = ecgChn0IndexEnum.getValue();
        data[3] = ecg1mVEnum.getValue();
        data[4] = respLeadIndexEnum.getValue();
        data[5] = (byte) apenaDelay;
        SerialContent content = new SerialContent(SerialContent.TOKEN_PARAM, SerialContent.TYPE_PARAM, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 开始传输数据命令
     *
     * @return
     */
    public static byte[] cmdDataStart() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_PARAM, SerialContent.TYPE_DATA_START, null);
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
        SerialContent content = new SerialContent(SerialContent.TOKEN_PARAM, SerialContent.TYPE_DATA_STOP, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    public static void main(String[] args) {
        System.out.println(CRCUitl.CRC8(cmdDataStart()));

    }

}
