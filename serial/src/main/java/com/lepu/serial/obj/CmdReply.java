package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.constant.SerialContent;

import java.io.Serializable;

/**
 * 命令回复
 */
public class CmdReply implements Serializable,Cloneable {
    /**
     * 命令回复类型
     */
    CmdReplyType cmdReplyType;

    /**
     * 参数板版本
     */
    String version;

    public CmdReply(SerialMsg serialMsg) {
        getCmdReplyTypeByType(serialMsg.content.token, serialMsg.content.type);
        if (serialMsg.content.data != null && serialMsg.content.data.length > 0) {
            version = new String(serialMsg.content.data);
        }
    }

    public CmdReply(byte token, byte type) {
        getCmdReplyTypeByType(token, type);
     }

    public CmdReply() {
    }

    public CmdReplyType getCmdReplyType() {
        return cmdReplyType;
    }

    public void setCmdReplyType(CmdReplyType cmdReplyType) {
        this.cmdReplyType = cmdReplyType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void getCmdReplyTypeByType(byte token, byte type) {
        switch (token) {
            case SerialContent.TOKEN_PARAM: {
                switch (type) {
                    case SerialContent.TYPE_RESET:
                        this.cmdReplyType = CmdReplyType.CMD_TYPE_RESET;
                        break;
                    case SerialContent.TYPE_VERSION_INFO:
                        this.cmdReplyType = CmdReplyType.CMD_VERSION_INFO;
                        break;
                    case SerialContent.TYPE_SET_PARAM:
                        this.cmdReplyType = CmdReplyType.CMD_SET_PARAM;
                        break;
                    case SerialContent.TYPE_PATIENT:
                        this.cmdReplyType = CmdReplyType.CMD_PATIENT;
                        break;
                    case SerialContent.TYPE_DATA_START:
                        this.cmdReplyType = CmdReplyType.CMD_TYPE_DATA_START;
                        break;
                    case SerialContent.TYPE_DATA_STOP:
                        this.cmdReplyType = CmdReplyType.CMD_TYPE_DATA_STOP;
                        break;
                    default:

                }
            }
            break;
            case SerialContent.TOKEN_ECG:
                switch (type) {
                    case SerialContent.TYPE_ECG_LEAD_MODE:
                        this.cmdReplyType = CmdReplyType.CMD_TYPE_ECG_LEAD_MODE;
                        break;
                    case SerialContent.TYPE_CALIBRATION_SIGNAL:
                        this.cmdReplyType = CmdReplyType.CMD_TYPE_CALIBRATION_SIGNAL;
                        break;

                    default:

                }
                break;
            case SerialContent.TOKEN_RESP:
                switch (type) {
                    case SerialContent.TYPE_RESP_LEAD:
                        this.cmdReplyType = CmdReplyType.CMD_TYPE_RESP_LEAD;
                        break;
                    case SerialContent.TYPE_SUFFOCATION_ALARM_TIME:
                        this.cmdReplyType = CmdReplyType.CMD_TYPE_SUFFOCATION_ALARM_TIME;
                        break;

                    default:

                }
                break;

            case SerialContent.TOKEN_NIBP:
                switch (type) {
                    case SerialContent.TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT;
                        break;
                    case SerialContent.TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT;
                        break;
                    case SerialContent.TOKEN_NIBP_CANCEL_MEASUREMENT:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_CANCEL_MEASUREMENT;
                        break;
                    case SerialContent.TOKEN_NIBP_SET_PATIENT_TYPE:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_SET_PATIENT_TYPE;
                        break;
                    case SerialContent.TOKEN_NIBP_SET_INITIAL_INFLATION_PRESSURE:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_SET_INITIAL_INFLATION_PRESSURE;
                        break;
                    case SerialContent.TOKEN_NIBP_SET_WAVE_TRANSMISSION_MODE:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_SET_WAVE_TRANSMISSION_MODE;
                        break;
                    case SerialContent.TOKEN_NIBP_READ_BLOOD_PRESSURE_PARAMETERS:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_PARAMETERS;
                        break;
                    case SerialContent.TOKEN_NIBP_READ_THE_WORKING_STATUS_OF_THE_BLOOD_PRESSURE_MODULE:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_READ_THE_WORKING_STATUS_OF_THE_BLOOD_PRESSURE_MODULE;
                        break;

                    case SerialContent.TOKEN_NIBP_READ_BLOOD_PRESSURE_MODULE_INFO:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_MODULE_INFO;
                        break;
                    case SerialContent.TOKEN_NIBP_CONTROL_PUMP:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_CONTROL_PUMP;
                        break;
                    case SerialContent.TOKEN_NIBP_CONTROL_QUICK_VALVE:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_CONTROL_QUICK_VALVE;
                        break;
                    case SerialContent.TOKEN_NIBP_CONTROL_SLOW_VALVE:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_CONTROL_SLOW_VALVE;
                        break;
                     case SerialContent.TOKEN_NIBP_SLEEP_MODE:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_SLEEP_MODE;
                        break;

                    case SerialContent.TOKEN_NIBP_RESET_MODULE:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_RESET_MODULE;
                        break;

                    case SerialContent.TOKEN_NIBP_AUXILIARY_VENIPUNCTURE:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_AUXILIARY_VENIPUNCTURE;
                        break;

                    case SerialContent.TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1;
                        break;
                    case SerialContent.TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2;
                        break;
                    case SerialContent.TOKEN_NIBP_LEAK_DETECTION:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_LEAK_DETECTION;
                        break;
                    case SerialContent.TOKEN_NIBP_CALIBRATE_THE_PRESSURE_SENSOR:
                        this.cmdReplyType = CmdReplyType.CMD_TOKEN_NIBP_CALIBRATE_THE_PRESSURE_SENSOR;
                        break;

                    default:

                }
                break;

            default:

        }


    }

    public enum CmdReplyType {

        /**
         * 复位参数板
         */
        CMD_TYPE_RESET,

        /**
         * 查询参数板信息
         */
        CMD_VERSION_INFO,

        /**
         * 设置总参数
         */
        CMD_SET_PARAM,

        /**
         * 设置病人类型
         */
        CMD_PATIENT,

        /**
         * 收到启动数据传输命令回复
         */
        CMD_TYPE_DATA_START,

        /**
         * 收到停止数据传输命令回复
         */
        CMD_TYPE_DATA_STOP,

        /**
         * 设置导联模式
         */
        CMD_TYPE_ECG_LEAD_MODE,
        /**
         * 设置定标信号
         */
        CMD_TYPE_CALIBRATION_SIGNAL,
        /**
         *设置呼吸导联
         */
        CMD_TYPE_RESP_LEAD,
        /**
         *设置窒息报警时间
         */
        CMD_TYPE_SUFFOCATION_ALARM_TIME,

        /**
         * 开始手动血压测量
         */
        CMD_TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT,
        /**
         * 开始连续测量
         */
        CMD_TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT,
        /**
         * 取消测量
         */
        CMD_TOKEN_NIBP_CANCEL_MEASUREMENT,
        /**
         * 血压 设置病人
         */
        CMD_TOKEN_NIBP_SET_PATIENT_TYPE,
        /**
         * 设置初始充气压
         */
        CMD_TOKEN_NIBP_SET_INITIAL_INFLATION_PRESSURE,
        /**
         * 血压设置传输模式
         */
        CMD_TOKEN_NIBP_SET_WAVE_TRANSMISSION_MODE,
        /**
         * 读取血压参数
         */
        CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_PARAMETERS,
        /**
         *  读取血压模块工作状态
         */
        CMD_TOKEN_NIBP_READ_THE_WORKING_STATUS_OF_THE_BLOOD_PRESSURE_MODULE,
        /**
         *  读取血压模块信息
         */
        CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_MODULE_INFO,
        /**
         *   控制泵
         */
        CMD_TOKEN_NIBP_CONTROL_PUMP,
        /**
         *   控制块泵
         */
        CMD_TOKEN_NIBP_CONTROL_QUICK_VALVE,
        /**
         *   控制慢泵
         */
        CMD_TOKEN_NIBP_CONTROL_SLOW_VALVE,
        /**
         *   睡眠模式
         */
        CMD_TOKEN_NIBP_SLEEP_MODE,
        /**
         *   复位模块
         */
        CMD_TOKEN_NIBP_RESET_MODULE,
        /**
         *   复位模块
         */
        CMD_TOKEN_NIBP_AUXILIARY_VENIPUNCTURE,
        /**
         *压力校验模式1（内部充气源）
         */
        CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1,
        /**
         * 压力校验模式2（外部充气源）
         */
        CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2,
        /**
         * 漏气检测
         */
        CMD_TOKEN_NIBP_LEAK_DETECTION,
        /**
         * 校准压力传感器
         */
        CMD_TOKEN_NIBP_CALIBRATE_THE_PRESSURE_SENSOR
    }

    @NonNull
    @Override
    protected CmdNibpReply clone() throws CloneNotSupportedException {
        return (CmdNibpReply)super.clone();
    }
}
