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

    public  CmdReply(SerialMsg serialMsg) {
        cmdReplyType= getCmdReplyTypeByType(serialMsg.content.token, serialMsg.content.type);
        if (serialMsg.content.data != null && serialMsg.content.data.length > 0) {
            version = new String(serialMsg.content.data);
        }
    }

    public CmdReply(byte token, byte type) {
        cmdReplyType = getCmdReplyTypeByType(token, type);
     }

    public CmdReply() {
    }

    public CmdReplyType getCmdReplyType() {
        return cmdReplyType;
    }

    public void setCmdReplyType(CmdReplyType cmdReplyType) {
       this.cmdReplyType=cmdReplyType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static CmdReplyType  getCmdReplyTypeByType(byte token, byte type) {
        switch (token) {
            case SerialContent.TOKEN_PARAM: {
                switch (type) {
                    case SerialContent.TYPE_RESET:
                       return  CmdReplyType.CMD_TYPE_RESET;

                    case SerialContent.TYPE_VERSION_INFO:
                        return CmdReplyType.CMD_VERSION_INFO;

                    case SerialContent.TYPE_SET_PARAM:
                        return CmdReplyType.CMD_SET_PARAM;

                    case SerialContent.TYPE_PATIENT:
                        return CmdReplyType.CMD_PATIENT;

                    case SerialContent.TYPE_DATA_START:
                        return CmdReplyType.CMD_TYPE_DATA_START;

                    case SerialContent.TYPE_DATA_STOP:
                        return CmdReplyType.CMD_TYPE_DATA_STOP;

                    default:

                }
            }
            break;
            case SerialContent.TOKEN_ECG:
                switch (type) {
                    case SerialContent.TYPE_ECG_LEAD_MODE:
                        return  CmdReplyType.CMD_TYPE_ECG_LEAD_MODE;

                    case SerialContent.TYPE_CALIBRATION_SIGNAL:
                        return CmdReplyType.CMD_TYPE_CALIBRATION_SIGNAL;


                    default:

                }
                break;
            case SerialContent.TOKEN_RESP:
                switch (type) {
                    case SerialContent.TYPE_RESP_LEAD:
                        return CmdReplyType.CMD_TYPE_RESP_LEAD;

                    case SerialContent.TYPE_SUFFOCATION_ALARM_TIME:
                        return CmdReplyType.CMD_TYPE_SUFFOCATION_ALARM_TIME;

                    default:

                }
                break;

            case SerialContent.TOKEN_NIBP:
                switch (type) {
                    case SerialContent.TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT:
                        return CmdReplyType.CMD_TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT;

                    case SerialContent.TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT:
                        return CmdReplyType.CMD_TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT;

                    case SerialContent.TOKEN_NIBP_CANCEL_MEASUREMENT:
                        return CmdReplyType.CMD_TOKEN_NIBP_CANCEL_MEASUREMENT;

                    case SerialContent.TOKEN_NIBP_SET_PATIENT_TYPE:
                        return CmdReplyType.CMD_TOKEN_NIBP_SET_PATIENT_TYPE;

                    case SerialContent.TOKEN_NIBP_SET_INITIAL_INFLATION_PRESSURE:
                        return CmdReplyType.CMD_TOKEN_NIBP_SET_INITIAL_INFLATION_PRESSURE;

                    case SerialContent.TOKEN_NIBP_SET_WAVE_TRANSMISSION_MODE:
                        return CmdReplyType.CMD_TOKEN_NIBP_SET_WAVE_TRANSMISSION_MODE;

                    case SerialContent.TOKEN_NIBP_READ_BLOOD_PRESSURE_PARAMETERS:
                        return CmdReplyType.CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_PARAMETERS;

                    case SerialContent.TOKEN_NIBP_READ_THE_WORKING_STATUS_OF_THE_BLOOD_PRESSURE_MODULE:
                        return CmdReplyType.CMD_TOKEN_NIBP_READ_THE_WORKING_STATUS_OF_THE_BLOOD_PRESSURE_MODULE;


                    case SerialContent.TOKEN_NIBP_READ_BLOOD_PRESSURE_MODULE_INFO:
                        return CmdReplyType.CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_MODULE_INFO;

                    case SerialContent.TOKEN_NIBP_CONTROL_PUMP:
                        return CmdReplyType.CMD_TOKEN_NIBP_CONTROL_PUMP;

                    case SerialContent.TOKEN_NIBP_CONTROL_QUICK_VALVE:
                        return CmdReplyType.CMD_TOKEN_NIBP_CONTROL_QUICK_VALVE;

                    case SerialContent.TOKEN_NIBP_CONTROL_SLOW_VALVE:
                        return CmdReplyType.CMD_TOKEN_NIBP_CONTROL_SLOW_VALVE;

                     case SerialContent.TOKEN_NIBP_SLEEP_MODE:
                        return CmdReplyType.CMD_TOKEN_NIBP_SLEEP_MODE;


                    case SerialContent.TOKEN_NIBP_RESET_MODULE:
                        return CmdReplyType.CMD_TOKEN_NIBP_RESET_MODULE;


                    case SerialContent.TOKEN_NIBP_AUXILIARY_VENIPUNCTURE:
                        return CmdReplyType.CMD_TOKEN_NIBP_AUXILIARY_VENIPUNCTURE;


                    case SerialContent.TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1:
                        return CmdReplyType.CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1;

                    case SerialContent.TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2:
                        return CmdReplyType.CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2;

                    case SerialContent.TOKEN_NIBP_LEAK_DETECTION:
                        return CmdReplyType.CMD_TOKEN_NIBP_LEAK_DETECTION;

                    case SerialContent.TOKEN_NIBP_CALIBRATE_THE_PRESSURE_SENSOR:
                        return CmdReplyType.CMD_TOKEN_NIBP_CALIBRATE_THE_PRESSURE_SENSOR;


                    default:

                }
                break;

            default:

        }

return null;

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
