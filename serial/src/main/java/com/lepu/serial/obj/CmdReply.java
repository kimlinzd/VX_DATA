package com.lepu.serial.obj;

import com.lepu.serial.constant.SerialContent;

/**
 * 命令回复
 */
public class CmdReply {
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
                        this.cmdReplyType = CmdReplyType. CMD_TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT;
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
         *开始手动血压测量
         */
        CMD_TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT,
        /**
         *开始连续测量
         */
        CMD_TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT,


    }
}
