package com.lepu.serial.constant;

import com.lepu.serial.enums.EcgCalEnum;
import com.lepu.serial.enums.EcgChn0IndexEnum;
import com.lepu.serial.enums.EcgLeadModeEnum;
import com.lepu.serial.enums.NibpCalibrationMode;
import com.lepu.serial.enums.NibpValveControlEnum;
import com.lepu.serial.enums.NipbpWmEnum;
import com.lepu.serial.enums.PatientTypeEnum;
import com.lepu.serial.enums.RespLeadIndexEnum;
import com.lepu.serial.enums.RespWaveGainEnum;
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
            , int apneaDelay,@NotNull RespWaveGainEnum respWaveGainEnum) {
        byte[] data = new byte[6];
        data[0] = (byte)patientTypeEnum.getValue();
        data[1] = ecgLeadModeEnum.getValue();
        data[2] = ecgChn0IndexEnum.getValue();
        data[3] = ecgCalEnum.getValue();
        data[4] = respLeadIndexEnum.getValue();
        data[5] = (byte) apneaDelay;
        data[6] = respWaveGainEnum.getValue();
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

    /**
     * 设置RESP 波形增益
     * @param respWaveGainEnum
     * @return
     */
    public static byte[] cmdSetRespWaveGain(@NotNull RespWaveGainEnum respWaveGainEnum) {
        byte[] data = new byte[1];
        data[0] = (byte)respWaveGainEnum.getValue();
        SerialContent content = new SerialContent(SerialContent.TOKEN_RESP, SerialContent.TYPE_RESP_WAVE_GAIN, data);
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
    public static byte[] cmdNibpSetInitialPressure(short initPre) {
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

    /**
     * 主动读取血压模块工作状态
     * 模块响应“血压模块工作状态”包
     * 上电会主动上发一次，上位机读取也会上传一次。
     */
    public static byte[] cmdNibpReadBpWorkStatus() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_READ_THE_WORKING_STATUS_OF_THE_BLOOD_PRESSURE_MODULE, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 读取血压模块信息
     * 模块响应“血压模块信息”包
     */
    public static byte[] cmdNibpReadBpModuleInfo() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_READ_BLOOD_PRESSURE_MODULE_INFO, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 控制泵
     * 0 表示停止，1-100 表示不同的充气力度，100 是泵最大力度的充气。
     * 模块响应“O”包,表示接收到指令。
     */
    public static byte[] cmdNibpControlPump(int inflationStrength) {
        byte[] data = new byte[1];
        data[0] = (byte) inflationStrength;
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_CONTROL_PUMP, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 控制快阀。模块响应“O”包,表示接收到指令。
     */
    public static byte[] cmdNibpControlQuickValve(NibpValveControlEnum nibpValveControlEnum) {
        byte[] data = new byte[1];
        data[0] = nibpValveControlEnum.getValue();
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_CONTROL_QUICK_VALVE, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 控制慢阀。模块响应“O”包,表示接收到指令。
     */
    public static byte[] cmdNibpControlQSlowValve(NibpValveControlEnum nibpValveControlEnum) {
        byte[] data = new byte[1];
        data[0] = nibpValveControlEnum.getValue();
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_CONTROL_SLOW_VALVE, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 睡眠模式
     * 设置NIBP进入睡眠模式。模块响应“S”包,表示模块进入睡眠模式。
     */
    public static byte[] cmdNibpSetSleepMode() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_SLEEP_MODE, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 复位模块
     * 复位模块。模块响应“R”包,表示接收到复位指令。
     */
    public static byte[] cmdNibpSetResetModule() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_RESET_MODULE, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 辅助静脉穿刺
     * IP ：初始充气压
     * 成人：20~120mmHg(自动80mmHg)，模块设置step：1mmHg，上位机设置step：10mmHg，需要上层在170秒时取消测量。
     * 儿童：20~80mmHg(自动60mmHg)，模块设置step：1mmHg，上位机设置step：10mmHg，需要上层在170秒时取消测量。
     * 新生儿：20~50mmHg(自动60mmHg)，模块设置step：1mmHg，上位机设置step：10mmHg，需要上层在85秒时取消测量。
     * 大动物：同成人\或不支持此功能
     * 小动物：同儿童\或不支持此功能
     * <p>
     * 上位机下发“辅助静脉穿刺”命令后，若模块上行“O”包,表示接收到指令，并执行命令，
     * 若上位机下发“辅助静脉穿刺”命令后，模块响应“B”包,表示模块正忙，不会执行命令，正常执行这项功能后，
     * 如果出现超时或者过压后模块依次上行“血压参数”包（主要目的是提供相应的错误信息）和“K”包(表示这项功能测量完成)
     * <p>
     * Note:在接收到“O”包后，“K”包前，只响应“取消测量”、“复位模块包”、“设置波形传输模式”、“读取血压参数”、
     * “读取血压模块工作状态”、“读取血压模块信息”。其它命令会响应“B”包；
     *
     * @param pre 初始充气压
     * @return
     */
    public static byte[] cmdNibpAuxiliaryVenipuncture(short pre) {
        byte[] initPreByte = ByteUtils.shortToBytes(pre, ByteOrder.BIG_ENDIAN);
        byte[] data = new byte[2];
        data[0] = initPreByte[0];
        data[1] = initPreByte[1];
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_AUXILIARY_VENIPUNCTURE, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 压力校验模式1（内部充气源）
     * <p>
     * 成人类型下，压力值建议设置为250mmHg
     * 儿童类型下，压力值建议设置为200mmHg
     * 新生儿类型下，压力值建议设置为120mmHg
     * <p>
     * 上位机下发“压力检验模式1”命令后，若模块上行“O”包,表示接收到指令，并执行命令，
     * 若上位机下发“压力检验模式1”命令后，模块响应“B”包,表示模块正忙，不会执行命令，
     * 正常执行这项功能后，如果出现超时或者过压后模块依次上行“血压参数”包（主要目的是提供相应的错误信息）和“K”包(表示这项功能测量完成)
     * Note:在接收到“O”包后，“K”包前，只响应“取消测量”、“复位模块包”、“设置波形传输模式”、
     * “读取血压参数”、“读取血压模块工作状态”、“读取血压模块信息”。其它命令会响应“B”包；
     *
     * @param pre 气压
     * @return
     */
    public static byte[] cmdNibpCalibrationMode1(short pre) {
        byte[] initPreByte = ByteUtils.shortToBytes(pre, ByteOrder.BIG_ENDIAN);
        byte[] data = new byte[2];
        data[0] = initPreByte[0];
        data[1] = initPreByte[1];
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }

    /**
     * 压力校验模式2（外部充气源）
     * <p>
     * 上位机下发“压力检验模式2”命令后，若模块上行“O”包,表示接收到指令，并执行命令，
     * 若上位机下发“压力检验模式2”命令后，模块响应“B”包,表示模块正忙，不会执行命令，
     * 正常执行这项功能后，如果出现超时或者过压后模块依次上行“血压参数”包（主要目的是提供相应的错误信息）和“K”包(表示这项功能测量完成)
     * <p>
     * Note:在接收到“O”包后，“K”包前，只响应“取消测量”、“复位模块包”、“设置波形传输模式”、
     * “读取血压参数”、“读取血压模块工作状态”、“读取血压模块信息”。其它命令会响应“B”包；
     */
    public static byte[] cmdNibpCalibrationMode2() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /**
     * 漏气检测
     * <p>
     * 模块实现： 成人/小儿充气至150mmHg左右(新生儿充气至120mmHg左右)，并且保持一段时间直到漏气检测完成，通常会在30秒内完成检测。上位机同事请注意，上位机只支持成人和小儿类型的漏气检测，新生儿类型下不支持漏气检测，且新生儿类型下漏气检测的选项应该设置成灰色的(参照迈瑞N12/ePM )。
     * <p>
     * 上位机下发“漏气检测”命令后，若模块上行“O”包,表示接收到指令，并执行命令，
     * 若上位机下发“漏气检测”命令后，模块响应“B”包,表示模块正忙，不会执行命令，
     * 正常执行这项功能后，漏气检测完成后模块依次上行“血压参数”包（主要目的是提供相应的错误信息）和“K”包(表示这项功能测量完成)
     * Note:在接收到“O”包后，“K”包前，只响应“取消测量”、“复位模块包”、“设置波形传输模式”、“读取血压参数”、
     * “读取血压模块工作状态”、“读取血压模块信息”。其它命令会响应“B”包；
     */
    public static byte[] cmdNibpLeakDetection() {
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_LEAK_DETECTION, null);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /**
     * 校准压力传感器
     * <p>
     * 设置压力检验模式1。模块响应“O”包,表示接收到指令。
     * 压力值建议设置为250mmHg，且需要在成人类型下使用此功能。
     *
     * @param nibpCalibrationMode
     * @param pre
     * @return
     */
    public static byte[] cmdNibpCalibration(NibpCalibrationMode nibpCalibrationMode, short pre) {
        byte[] data = new byte[2];
        short mode = nibpCalibrationMode.getValue();
        short dataShort = (short) ((mode << 12) | (pre));
        data = ByteUtils.shortToBytes(dataShort, ByteOrder.BIG_ENDIAN);

        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_CALIBRATE_THE_PRESSURE_SENSOR, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    /**
     * 血压参数回答包
     **/
    public static byte[] cmdNibpPressureReply() {
        byte[] data = new byte[2];
        SerialContent content = new SerialContent(SerialContent.TOKEN_NIBP, SerialContent.TOKEN_NIBP_PRESSURE_REPLY, data);
        SerialMsg msg = new SerialMsg(index, SerialMsg.TYPE_CMD, content);
        index++;
        return msg.toBytes();
    }


    //

    /*************************************************** 血压NIBP业务 end*************************************************************/

    public static void main(String[] args) {
        byte[] data = new byte[2];
        short mode = NibpCalibrationMode.STOP_CALIBRATION.getValue();
        short dataShort = (short) ((mode << 12) | (220));
        data = ByteUtils.shortToBytes(dataShort, ByteOrder.BIG_ENDIAN);

        int oo = ((data[0] & 0xf0) >> 4);
        data[0] = (byte) ((data[0] & 0x0f) << 4);

        System.out.println(ByteUtils.byte2short(data) + "");
       /* short argB1 = 2;
        short argB2 = 230;


        System.out.println("argB1==" + shortToBit(argB1));
        System.out.println("argB2==" + shortToBit(argB2));


        //   short s = (short) ((a << 12) + b);
// (short) ((argB1 & 0xFF)| (argB2 << 8));

        short s = (short) ((argB1 << 12) | (argB2));
        System.out.println("合并==" + shortToBit(s));


        byte[] c = ByteUtils.shortToBytes(s, ByteOrder.BIG_ENDIAN);
        System.out.println("分解==" + byteToBit(c[0]));
        System.out.println("分解==" + byteToBit(c[1]));
        int oo = ((c[0] & 0xf0) >> 4);


        byte dd = (byte) ((c[0] & 0x0f) << 4);
        System.out.println("dd==" + byteToBit(dd));

        // short a＝(c[0]<<8)|(c[1]&0xff);
        c[0] = (byte) ((c[0] & 0x0f) << 4);

        System.out.println(ByteUtils.byte2short(c) + "ee");

        System.out.println("" + shortToBit((short) (((c[0] & 0x0f) << 4) + c[1])));

        System.out.println(oo + "---" + (short) (((c[0] & 0x0f) << 8) + c[1]));*/


    }

    /**
     * Byte转Bit
     */
    public static String byteToBit(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) +
                (byte) ((b >> 6) & 0x1) +
                (byte) ((b >> 5) & 0x1) +
                (byte) ((b >> 4) & 0x1) +
                (byte) ((b >> 3) & 0x1) +
                (byte) ((b >> 2) & 0x1) +
                (byte) ((b >> 1) & 0x1) +
                (byte) ((b >> 0) & 0x1);
    }



}
