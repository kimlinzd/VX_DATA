package com.lepu.serial.constant;

import com.lepu.serial.enums.EcgCalEnum;
import com.lepu.serial.enums.EcgChn0IndexEnum;
import com.lepu.serial.enums.EcgLeadModeEnum;
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

    /***************************************************参数板整体业务 start*************************************************************/


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
    public static byte[] cmdGetVersionInfo() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_PARAM, SerialContent.TYPE_VERSION_INFO, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /**
     * 参数板总参数
     * 注：在启动传输命令前，需要发送此命令，用于同步设置
     *
     * @param patientTypeEnum
     * @param ecgLeadModeEnum
     * @param ecgChn0IndexEnum
     * @param ecgCalEnum
     * @param respLeadIndexEnum
     * @param apneaDelay        窒息报警时间，单位：秒
     * @return
     */
    public static byte[] cmdSetParam(@NotNull PatientTypeEnum patientTypeEnum, @NotNull EcgLeadModeEnum ecgLeadModeEnum
            , @NotNull EcgChn0IndexEnum ecgChn0IndexEnum, @NotNull EcgCalEnum ecgCalEnum, @NotNull RespLeadIndexEnum respLeadIndexEnum
            , int apneaDelay) {
        byte[] data = new byte[6];
        data[0] = (byte)patientTypeEnum.getValue();
        data[1] = ecgLeadModeEnum.getValue();
        data[2] = ecgChn0IndexEnum.getValue();
        data[3] = ecgCalEnum.getValue();
        data[4] = respLeadIndexEnum.getValue();
        data[5] = (byte) apneaDelay;
        SerialContent content = new SerialContent(SerialContent.TOKEN_PARAM, SerialContent.TYPE_SET_PARAM, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /**
     * 设置病人类型
     *
     * @param patientTypeEnum
     * @return
     */
    public static byte[] cmdSetPatientType(@NotNull PatientTypeEnum patientTypeEnum) {
        byte[] data = new byte[1];
        data[0] = (byte)patientTypeEnum.getValue();
        SerialContent content = new SerialContent(SerialContent.TOKEN_PARAM, SerialContent.TYPE_PATIENT, data);
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

    /***************************************************参数板整体业务 end*************************************************************/

    /*************************************************** 心电ECG业务 start*************************************************************/
    /**
     * 设置导联模式
     * 此命令用于设置导联模式的
     *
     * @param ecgLeadModeEnum
     * @param ecgChn0IndexEnum
     * @return
     */
    public static byte[] cmdSetLeadMode(@NotNull EcgLeadModeEnum ecgLeadModeEnum, @NotNull EcgChn0IndexEnum ecgChn0IndexEnum) {
        byte[] data = new byte[2];
        data[0] = ecgLeadModeEnum.getValue();
        data[1] = ecgChn0IndexEnum.getValue();
        SerialContent content = new SerialContent(SerialContent.TOKEN_ECG, SerialContent.TYPE_ECG_LEAD_MODE, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /**
     * 设置定标信号
     *
     * @param ecgCalEnum
     * @return
     */
    public static byte[] cmdSetCal(@NotNull EcgCalEnum ecgCalEnum) {
        byte[] data = new byte[1];
        data[0] = ecgCalEnum.getValue();
        SerialContent content = new SerialContent(SerialContent.TOKEN_ECG, SerialContent.TYPE_CALIBRATION_SIGNAL, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /*************************************************** 心电ECG业务 end*************************************************************/


    /*************************************************** 呼吸RESP业务 start*************************************************************/


    /**
     * 设置呼吸导联
     *
     * @param respLeadIndexEnum
     * @return
     */
    public static byte[] cmdSetRespLead(@NotNull RespLeadIndexEnum respLeadIndexEnum) {
        byte[] data = new byte[1];
        data[0] = respLeadIndexEnum.getValue();
        SerialContent content = new SerialContent(SerialContent.TOKEN_RESP, SerialContent.TYPE_RESP_LEAD, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 设置窒息报警时间
     *
     * @param apneaDelay
     * @return
     */
    public static byte[] cmdSetApneaDelay(int apneaDelay) {
        byte[] data = new byte[1];
        data[0] = (byte) apneaDelay;
        SerialContent content = new SerialContent(SerialContent.TOKEN_RESP, SerialContent.TYPE_SUFFOCATION_ALARM_TIME, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /*************************************************** 呼吸RESP业务 end*************************************************************/


    /*************************************************** 体温TEMP业务 start*************************************************************/
    //体温业务暂时没有
    /*************************************************** 体温TEMP业务 end*************************************************************/

    /*************************************************** 血压NIBP业务 start*************************************************************/
    // * 注意：血压模块属于独立的子模块，多参数板只负责转传（转协议后透传）
    // * 原协议为《第二代无创血压模块通讯协议V1.0.0.0_20211109》

    /**
     * 0x01 开始手动血压测量
     */
    public static byte[] cmdStartManualBp() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /*************************************************** 血压NIBP业务 end*************************************************************/

    public static void main(String[] args) {
        System.out.println(CRCUitl.CRC8(cmdDataStart()));

    }

}
