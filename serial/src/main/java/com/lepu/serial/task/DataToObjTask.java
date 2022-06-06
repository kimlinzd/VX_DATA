package com.lepu.serial.task;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.lepu.serial.constant.EventMsgConst;
import com.lepu.serial.constant.SerialCmd;
import com.lepu.serial.constant.SerialContent;
import com.lepu.serial.manager.ServeComManager;
import com.lepu.serial.obj.EcgData;
import com.lepu.serial.obj.NibpCP200HZData;
import com.lepu.serial.obj.NibpCP5HZData;
import com.lepu.serial.obj.NibpModuleInfo;
import com.lepu.serial.obj.NibpPramAndStatus;
import com.lepu.serial.obj.NibpWorkingStatus;
import com.lepu.serial.obj.RespData;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.obj.SpO2Data;
import com.lepu.serial.obj.SpO2WaveData;
import com.lepu.serial.obj.TempData;

/**
 * 把数据解析成OBJ 放到eventbus线程
 */
public class DataToObjTask extends Thread {
    SerialMsg serialMsg;
    byte[] data;
    byte tokenByte;
    byte typeByte;
    byte classByte;

    public static int index;

    public DataToObjTask(SerialMsg serialMsg) {
        this.data = serialMsg.getContent().data;
        this.tokenByte = serialMsg.getContent().token;
        this.typeByte = serialMsg.getContent().type;
        this.classByte=serialMsg.getType();
        this.serialMsg=serialMsg;
    }


    @Override
    public void run() {
        switch (classByte) {
            case SerialMsg.TYPE_CMD: {//命令包 0xF0

            }
            break;
            case SerialMsg.TYPE_ACK: //命令确认包（若有回复包，就不发确认包）0xF1
            case SerialMsg.TYPE_REPLY: {//命令确认包 回复包 0xF2


            }
            break;
            case SerialMsg.TYPE_DATA: {//数据包 0xF3
                switch (tokenByte) {
                    case SerialContent.TOKEN_ECG: {
                        index++;
                        if (index==125){
                            index=0;
                        }
                        //上传心电数据
                        EcgData ecgData = new EcgData(data);
                        ecgData.setEcgIndex(index);
                        LiveEventBus.get(EventMsgConst.MsgEcgData)
                                .post(ecgData);


                    }
                    break;
                    case SerialContent.TOKEN_RESP: {
                        //上传呼吸RESP
                        RespData respData = new RespData(data);
                        LiveEventBus.get(EventMsgConst.MsgRespData)
                                .post(respData);
                    }
                    break;
                    case SerialContent.TOKEN_TEMP: {
                        //上传体温数据
                        TempData tempData = new TempData(data);
                        LiveEventBus.get(EventMsgConst.MsgTempData)
                                .post(tempData);
                    }
                    break;

                    case SerialContent.TOKEN_SP02: {

                        //血氧SpO2
                        if (serialMsg.getContent().type == SerialContent.TYPE_DATA_SP02) {
                            //上传SpO2数据
                            SpO2Data spO2Data = new SpO2Data(data);
                            LiveEventBus.get(EventMsgConst.MsgSpO2Data)
                                    .post(spO2Data);

                        } else if (serialMsg.getContent().type == SerialContent.TYPE_DATA_SP02_WAVE) {
                            //上传波形数据
                            SpO2WaveData spO2WaveData = new SpO2WaveData(data);
                            LiveEventBus.get(EventMsgConst.MsgSpO2WaveData)
                                    .post(spO2WaveData);

                        }
                    }
                    break;

                }
            }
            break;
            case SerialMsg.TYPE_STATUS: {//如心跳包，异常状态包等等（主动传输）	双向 0xF4

            }
            break;
            case SerialMsg.TYPE_UPDATE: {//升级包 0xF5

            }
            break;

            case SerialMsg.TYPE_PASS0: {//上行转传包	Master ← Slave
                switch (tokenByte) {
                    case SerialContent.TOKEN_NIBP: { //血压NIBP
                        switch (typeByte) {
                            case SerialContent.TYPE_NIBP_REPLY_PACKET: {//应答包

                            }
                            break;
                            case SerialContent.TOKEN_NIBP_DATA_5HZ: {//血压NIBP 实时袖带压（5Hz）
                               NibpCP5HZData nibpData = new NibpCP5HZData(data);
                                LiveEventBus.get(EventMsgConst.MsgNibpCP5HZData)
                                        .post(nibpData);
                            }
                            break;
                            case SerialContent.TOKEN_NIBP_DATA_200HZ: {//实时原始数据（200Hz）
                                NibpCP200HZData nibpOriginalData = new NibpCP200HZData(data);
                                LiveEventBus.get(EventMsgConst.MsgNibpCP200HZData)
                                        .post(nibpOriginalData);

                            }
                            break;
                            case SerialContent.TOKEN_NIBP_BLOOD_PRESSURE_PARAM_MODULE_STATUS: {//血压参数和模块状态

                                NibpPramAndStatus nibpPramAndStatus = new NibpPramAndStatus(data);
                                LiveEventBus.get(EventMsgConst.NibpPramAndStatus)
                                        .post(nibpPramAndStatus);
                                ServeComManager.getInstance().nibpInfo.setLastMeasureTime(System.currentTimeMillis());
                                //收到结果包需要回复 否者会连续的到三次结果包
                                ServeComManager.getInstance()
                                        .serialSendData(SerialCmd.cmdNibpPressureReply());
                                //
                                ServeComManager.getInstance()
                                        .serialSendData(SerialCmd.cmdNibpReadBpWorkStatus());
                            }
                            break;
                            case SerialContent.TOKEN_NIBP_WORKING_STATUS_OF_BLOOD_PRESSURE_MODULE: {//血压模块工作状态
                                NibpWorkingStatus nibpPramAndStatus = new NibpWorkingStatus(data);
                                ServeComManager.getInstance().nibpInfo.setNibpMsmEnum(nibpPramAndStatus.getNibpMsmEnum());
                                LiveEventBus.get(EventMsgConst.NibpWorkingStatus)
                                        .post(nibpPramAndStatus);
                            }
                            break;
                            case SerialContent.TOKEN_NIBP_BLOOD_PRESSURE_MODULE_INFO: {//血压模块信息

                                NibpModuleInfo nibpModuleInfo = new NibpModuleInfo(data);
                                LiveEventBus.get(EventMsgConst.NibpModuleInfo)
                                        .post(nibpModuleInfo);
                            }
                            break;

                            default:

                        }

                    }
                    break;

                    default:
                }

            }
            break;

            case SerialMsg.TYPE_PASS1: {//下行转传包	Master → Slave

            }
            break;


            default:
        }







    }


}
