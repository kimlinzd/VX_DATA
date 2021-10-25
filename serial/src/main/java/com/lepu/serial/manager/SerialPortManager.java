package com.lepu.serial.manager;

import android.os.AsyncTask;
import android.serialport.SerialPort;
import android.util.Log;


import com.jeremyliao.liveeventbus.LiveEventBus;
import com.lepu.serial.constant.SerialContent;
import com.lepu.serial.obj.EcgData1;
import com.lepu.serial.obj.EventMsgConst;
import com.lepu.serial.obj.NibpData;
import com.lepu.serial.obj.NibpOriginalData;
import com.lepu.serial.obj.RespData;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.obj.SpO2Data;
import com.lepu.serial.obj.SpO2OriginalData;
import com.lepu.serial.obj.TempData;
import com.lepu.serial.task.ConsumptionTask;
import com.lepu.serial.task.SerialPortDataTask;
import com.lepu.serial.uitl.CRCUitl;
import com.lepu.serial.uitl.StringtoHexUitl;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 串口管理 打开串口 读取数据流 写入数据流
 */
public class SerialPortManager {

    private ReadThread mReadThread;
    SerialPort mSerialPort;
    InputStream mInputStream;
    OutputStream mOutputStream;
    //处理串口数据队列
    public SerialPortDataTask lineUpTaskHelp;


    private static SerialPortManager instance = null;

    public static SerialPortManager getInstance() {
        if (instance == null) {
            instance = new SerialPortManager();
        }
        return instance;
    }

    /**
     * 初始化串口
     */
    public void init() {
        AsyncTask.execute(() -> {
            try {
                Log.d("SerialPortManager", "初始化串口");
                //打开串口
                SerialPort serialPort = SerialPort //
                        .newBuilder("/dev/ttyS1", 480600) // 串口地址地址，波特率
                        .parity(0) // 校验位；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
                        .dataBits(8) // 数据位,默认8；可选值为5~8
                        .stopBits(1) // 停止位，默认1；1:1位停止位；2:2位停止位
                        .build();
                mSerialPort = serialPort;
                mInputStream = mSerialPort.getInputStream();
                mOutputStream = mSerialPort.getOutputStream();
                //开始定时获取心电图数据
                startGetEcgData();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        lineUpTaskHelp = SerialPortDataTask.getInstance();
        lineUpTaskHelp.setOnTaskListener(onTaskListener);
    }

    /**
     * 开始读取串口数据
     */
    public void startGetEcgData() {
        mReadThread = new ReadThread();
        mReadThread.start();
    }

    /**
     * 向串口写入数据
     */
    public void serialSendData(String sendStr) {
        try {

            OutputStream mOutputStream;
            mOutputStream = mSerialPort.getOutputStream();

            byte[] msg = StringtoHexUitl
                    .toByteArray(sendStr
                            .replace(" ", ""));
            for (int i = 0; i < msg.length; i++) {
                mOutputStream.write(msg[i]);
            }
            mOutputStream.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        if (mReadThread != null) mReadThread.interrupt();

    }


    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    if (mInputStream == null) return;
                    byte[] buffer = readStream(mInputStream);
                    ConsumptionTask task = new ConsumptionTask();
                    task.taskNo = String.valueOf(System.currentTimeMillis()); // 确保唯一性
                    task.data = buffer;
                    // 添加到排队列表中去
                    lineUpTaskHelp.addTask(task);
                    sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    byte[] surplusData;//用于记录任务剩余的数据 放入下一个任务继续遍历
    SerialPortDataTask.OnTaskListener onTaskListener = new SerialPortDataTask.OnTaskListener() {
        @Override
        public void exNextTask(ConsumptionTask task) {
            Log.d("收到数据", StringtoHexUitl.byteArrayToHexStr(task.data));
            byte[] data;
            //如果有剩余的数据，需要把剩余的剩余的数据重新处理
            if (surplusData != null) {
                data = new byte[surplusData.length + task.data.length];
                System.arraycopy(surplusData, 0, data, 0, surplusData.length);
                System.arraycopy(task.data, 0, data, surplusData.length, task.data.length);
            } else {
                data = task.data;
            }
            //用于记录一段完整的报文
            byte[] completeData = null;
            //遍历数据
            for (int i = 0; i < data.length; i++) {
                //第三个是长度
                if (i + 2 >= data.length) {
                    //把最后的数据 放在下一个任务中
                    surplusData = new byte[data.length - i];
                    System.arraycopy(data, i, surplusData, 0, data.length - i);
                    break;
                }
                //判断开头
                if (data[i] == SerialContent.SYNC_H && data[i + 1] == SerialContent.SYNC_L) {
                    completeData = new byte[(0x00ff & data[i + 2])];
                    if (i + completeData.length > data.length) {
                        //把最后的数据 放在下一个任务中
                        surplusData = new byte[data.length - i];
                        System.arraycopy(data, i, surplusData, 0, data.length - i);
                        break;
                    } else {
                        System.arraycopy(data, i, completeData, 0, completeData.length);
                        //校验数据
                        if (CRCUitl.CRC8(completeData)) {
                            //越过已处理数据
                            i = i + completeData.length - 1;
                            //分发数据
                            distributeMsg(completeData);

                        }
                    }
                }

            }


            lineUpTaskHelp.exOk(task);
        }

        @Override
        public void noTask() {
            Log.d("noTask", "任务完成");
        }
    };


    /**
     * @param inStream
     * @return 字节数组
     * @throws Exception
     * @功能 读取流
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        int count = 0;
        while (count == 0) {
            count = inStream.available();
        }
        byte[] b = new byte[count];
        inStream.read(b);
        return b;
    }

    int i = 0;

    /**
     * 分发消息
     *
     * @param msgdata
     */
    public  void distributeMsg(byte[] msgdata) {
        //解析包
        SerialMsg serialMsg = new SerialMsg(msgdata);
        //报文类型（Class字节）用于区分不同的报文便于处理。其范围为0xF0 ~ 0xFB。
        byte classByte = serialMsg.getType();
        //Token：模块类型，用于区分不同的模块，如总模块，心电，血氧等
        byte tokenByte = serialMsg.getContent().token;
        //Type:内容种类，用于识别不同的内容，一个模块里有多种内容。
        byte typeByte = serialMsg.getContent().type;
        i++;
        Log.d("命令index--", i+"");

        switch (classByte) {
            case SerialMsg.TYPE_CMD: {//命令包 0xF0
                Log.d("分发命令--", "命令包 ");


            }
            break;
            case SerialMsg.TYPE_ACK: {//命令确认包（若有回复包，就不发确认包）0xF1
                Log.d("分发命令--", "命令确认包（若有回复包，就不发确认包）");

                switch (typeByte) {
                    case SerialContent.TYPE_DATA_START: {
                        Log.d("分发命令--", "接受到开始传输命令");

                    }
                    break;
                    case SerialContent.TYPE_DATA_STOP: {
                        Log.d("分发命令--", "接受到停止传输命令");

                    }
                    break;
                }

            }
            break;
            case SerialMsg.TYPE_REPLY: {//回复包 0xF2
                Log.d("分发命令--", "回复包");
            }
            break;
            case SerialMsg.TYPE_DATA: {//数据包 0xF3
                 switch (tokenByte) {
                    case SerialContent.TOKEN_ECG: {
                        //上传心电数据
                        Log.d("分发命令--", "心电数据数据包");


                        EcgData1 ecgData1 = new EcgData1(serialMsg.getContent().data);
                        LiveEventBus.get(EventMsgConst.MsgEcgData1)
                                .post(ecgData1);
                    }
                    break;
                    case SerialContent.TOKEN_RESP: {
                        //上传呼吸RESP
                        Log.d("分发命令--", "呼吸RESP数据包");
                        RespData respData = new RespData(serialMsg.getContent().data);
                        LiveEventBus.get(EventMsgConst.MsgRespData)
                                .post(respData);
                    }
                    break;
                    case SerialContent.TOKEN_TEMP: {
                        //上传体温数据
                        Log.d("分发命令--", "上传体温数据 数据包");
                        TempData tempData = new TempData(serialMsg.getContent().data);
                        LiveEventBus.get(EventMsgConst.MsgTempData)
                                .post(tempData);
                    }
                    break;
                    case SerialContent.TOKEN_NIBP: {
                        //血压NIBP
                        if (typeByte == SerialContent.TYPE_DATA_NIBP) {
                            //上传实时袖带压
                            Log.d("分发命令--", "血压NIBP 数据包");
                            NibpData nibpData = new NibpData(serialMsg.getContent().data);
                            LiveEventBus.get(EventMsgConst.MsgNibpData)
                                    .post(nibpData);
                        } else if (typeByte == SerialContent.TYPE_DATA_NIBP_ORIGINAL) {
                            //上传实时袖带压原始数据
                            Log.d("分发命令--", "血压NIBP 数据包");
                            NibpOriginalData nibpOriginalData = new NibpOriginalData(serialMsg.getContent().data);
                            LiveEventBus.get(EventMsgConst.MsgNibpOriginalData)
                                    .post(nibpOriginalData);
                        }

                    }
                    break;
                    case SerialContent.TOKEN_SP02: {
                        //血氧SpO2
                        if (typeByte == SerialContent.TYPE_DATA_SP02) {
                            //上传波形数据_原始数据
                            Log.d("分发命令--", "上传波形数据_原始数据 数据包");
                            SpO2OriginalData spO2OriginalData = new SpO2OriginalData(serialMsg.getContent().data);
                            LiveEventBus.get(EventMsgConst.MsgSpO2OriginalData)
                                    .post(spO2OriginalData);
                        } else if (typeByte == SerialContent.TYPE_DATA_SP02_ORIGINAL) {
                            //上传SpO2数据
                            Log.d("分发命令--", "上传SpO2数据 数据包");
                            SpO2Data spO2Data=new SpO2Data(serialMsg.getContent().data);
                            LiveEventBus.get(EventMsgConst.MsgSpO2Data)
                                    .post(spO2Data);

                        }
                    }
                    break;
                }


            }
            break;
            case SerialMsg.TYPE_STATUS: {//如心跳包，异常状态包等等（主动传输）	双向 0xF4
                Log.d("分发命令--", "如心跳包，异常状态包等等（主动传输）双向");
            }
            break;
            case SerialMsg.TYPE_UPDATE: {//升级包 0xF5
                Log.d("分发命令--", "升级包");
            }
            break;
            default:
                Log.d("分发命令--", "完成");
        }


    }


    public static void main(String[] args) {
        byte[] data = {(byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0x6B, (byte) 0xF3, (byte) 0x01, (byte) 0x00,
                (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x03, (byte) 0x3C, (byte) 0x00, (byte) 0x00,

                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x00,
                (byte) 0x9A};

       // distributeMsg(data);


    }


}
