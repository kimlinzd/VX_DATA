package com.lepu.serial.constant;

import com.lepu.serial.enums.EcgCalEnum;
import com.lepu.serial.enums.EcgChn0IndexEnum;
import com.lepu.serial.enums.EcgLeadModeEnum;
import com.lepu.serial.enums.NipbpWmEnum;
import com.lepu.serial.enums.PatientTypeEnum;
import com.lepu.serial.enums.RespLeadIndexEnum;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.uitl.ByteUtils;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

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
     * 开始手动血压测量
     * 开始一次手动血压测量。
     * 模块响应“O”包,表示接收到开始测量的指令。
     * 模块响应“K”包，表示一次手动血压测量完成。
     * 模块响应“B”包，表示模块正忙。
     * 注意：当模块正在进行一次血压测量时，有效的命令是“取消测量”、“复位模块”命令，其它命令模块会返回一个“B”包。
     */
    public static byte[] cmdStartManualBp() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 开始连续测量
     * 开始连续测量。连续5分钟的测量，一次测量完成，需要泄气低于15mmHg(5mmHg)，且持续时间超过2秒，才能开始进行下一次测量。
     * 1.连续测量过程中，当一次测量完成后，如果5分钟倒计时小于20s，则不再继续测量，退出连续测量模式。
     * 2.五分钟倒计时结束，如果当次测量还未完成，会等待当次测量完成，才会退出连续模式。
     * 模块响应“O”包,表示接收到开始紧急测量的指令。
     * 模块响应“K”包，表示紧急测量血压完成。
     * 注意：当模块正在进行紧急血压测量，有效的命令是“取消测量”、“复位模块”命令，其它命令模块会返回一个“B”包。
     */
    public static byte[] cmdStartContinuousBp() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 取消测量
     * 停止并取消当前的测量。
     * 模块响应“A”包,表示接收到取消测量的指令。
     * Note: 取消测量包括手动测量，连续测量(连续五分钟测量)，漏气检测，校验模式1，校验模式
     */
    public static byte[] cmdCancelBp() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_CANCEL_MEASUREMENT, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 血压测量 设置病人 模块响应“O”包,表示接收到指令。
     *
     * @param patientTypeEnum
     * @return
     */
    public static byte[] cmdNibpSetPatient(@NotNull PatientTypeEnum patientTypeEnum) {
        byte[] data = new byte[1];
        data[0] = (byte) patientTypeEnum.getValue();
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_SET_PATIENT_TYPE, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 设置初始充气压
     * 成人：80~280mmHg(默认160mmHg)
     * 儿童：80~210mmHg(默认140mmHg)
     * 新生儿：60~140mmHg(默认90mmHg)
     * 大动物：80~280mmHg(默认160mmHg)
     * 小动物：80~210mmHg(默认140mmHg)
     * 切换病人类型后，初始充气压会设置为对应病人类型的默认值
     * 模块响应“O”包,表示接收到指令
     */
    public static byte[] cmdNibpSetPatient(short initPre) {
        byte[] initPreByte = ByteUtils.shortToBytes(initPre, ByteOrder.BIG_ENDIAN);
        byte[] data = new byte[2];
        data[0] = initPreByte[0];
        data[1] = initPreByte[1];
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_SET_INITIAL_INFLATION_PRESSURE, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 设置波形传输模式
     */
    public static byte[] cmdNibpSetTransferMode(NipbpWmEnum nipbpWmEnum) {
        byte[] data = new byte[1];
        data[0] = (byte) nipbpWmEnum.getValue();
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_SET_WAVE_TRANSMISSION_MODE, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 读取血压参数
     * 模块响应“血压参数”包
     * 上电会主动上发一次，每次测量结束会主动上传一次，上位机读取也会上传一次
     */
    public static byte[] cmdNibpReadBpParam() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_READ_BLOOD_PRESSURE_PARAMETERS, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /*************************************************** 血压NIBP业务 end*************************************************************/

    public static void main(String[] args) {
        byte a = (byte) 127;
        int b = (int) a;
        System.out.println("b=" + b);

    }

}
