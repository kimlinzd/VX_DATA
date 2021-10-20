package com.lepu.serial.manager;

import android.os.AsyncTask;
import android.serialport.SerialPort;
import android.util.Log;


import com.lepu.serial.constant.SerialPortConstants;
import com.lepu.serial.task.ConsumptionTask;
import com.lepu.serial.task.SerialPortDataTask;
import com.lepu.serial.uitl.CRCUitl;
import com.lepu.serial.uitl.StringtoHexUitl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 串口管理 打开串口 读取数据流 写入数据流
 */
public class SerialPortManager {

    private ReadThread mReadThread;
    SerialPort mSerialPort;
    InputStream mInputStream;
    OutputStream mOutputStream;
    //处理串口数据队列
    SerialPortDataTask lineUpTaskHelp;


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
                Log.d("SerialPortManager","初始化串口");
                //打开串口
                SerialPort serialPort = SerialPort //
                        .newBuilder("/dev/ttyS1", 921600) // 串口地址地址，波特率
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
                if (data[i] == SerialPortConstants.SYNC_H && data[i + 1] == SerialPortConstants.SYNC_L) {
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

    int i=0;

    /**
     * 分发消息
     *
     * @param msgdata
     */
    public void distributeMsg(byte[] msgdata) {
        //报文类型（Class字节）用于区分不同的报文便于处理。其范围为0xF0 ~ 0xFB。
        byte classByte = msgdata[4];
        //Token：模块类型，用于区分不同的模块，如总模块，心电，血氧等
        byte tokenByte = msgdata[5];
        //Type:内容种类，用于识别不同的内容，一个模块里有多种内容。
        byte typeByte = msgdata[6];

        Log.d("分发消息--", "i=== "+i);
        i++;

         switch (classByte) {
            case SerialPortConstants.CLASS_CMD: {//命令包 0xF0
                Log.d("分发命令--", "命令包 ");


            }
            break;
            case SerialPortConstants.CLASS_ACK: {//命令确认包（若有回复包，就不发确认包）0xF1
                Log.d("分发命令--", "命令确认包（若有回复包，就不发确认包）");
                if (tokenByte == SerialPortConstants.TOKEN_MSG_CMD) {
                    switch (typeByte) {
                        case SerialPortConstants.TYPE_START_DATA_TRANSFER: {
                            Log.d("分发命令--", "接受到开始传输命令");

                        }
                        break;
                         case SerialPortConstants.TYPE_STOP_DATA_TRANSFER: {
                            Log.d("分发命令--", "接受到停止传输命令");

                        }
                        break;
                    }
                }
            }
            break;
            case SerialPortConstants.CLASS_REPLY: {//回复包 0xF2
                Log.d("分发命令--", "回复包");
            }
            break;
            case SerialPortConstants.CLASS_DATA: {//数据包 0xF3
                Log.d("分发命令--", "数据包");
                switch (tokenByte) {
                    case SerialPortConstants.TOKEN_MSG_ECG: {
                        //上传心电数据
                        Log.d("分发命令--", "心电数据数据包");
                    }
                    break;
                    case SerialPortConstants.TOKEN_MSG_RESP: {
                        //上传呼吸RESP
                        Log.d("分发命令--", "呼吸RESP数据包");
                    }
                    break;
                    case SerialPortConstants.TOKEN_MSG_TEMP: {
                        //上传体温数据
                        Log.d("分发命令--", "上传体温数据 数据包");
                    }
                    break;
                    case SerialPortConstants.TOKEN_MSG_NIBP: {
                        //血压NIBP
                        if (typeByte == SerialPortConstants.TYPE_NIBP) {
                            //上传实时袖带压
                            Log.d("分发命令--", "血压NIBP 数据包");
                        } else if (typeByte == SerialPortConstants.TYPE_NIBP_ORIGINAL) {
                            //上传实时袖带压原始数据
                            Log.d("分发命令--", "血压NIBP 数据包");
                        }

                    }
                    break;
                    case SerialPortConstants.TOKEN_MSG_SPO2: {
                        //血氧SpO2
                        if (typeByte == SerialPortConstants.TYPE_SPO2_ORIGINAL) {
                            //上传波形数据_原始数据
                            Log.d("分发命令--", "上传波形数据_原始数据 数据包");
                        } else if (typeByte == SerialPortConstants.TYPE_SPO2) {
                            //上传SpO2数据
                            Log.d("分发命令--", "上传SpO2数据 数据包");
                        }
                    }
                    break;
                }


            }
            break;
            case SerialPortConstants.CLASS_STATUS: {//如心跳包，异常状态包等等（主动传输）	双向 0xF4
                Log.d("分发命令--", "如心跳包，异常状态包等等（主动传输）双向");
            }
            break;
            case SerialPortConstants.CLASS_UPDATA: {//升级包 0xF5
                Log.d("分发命令--", "升级包");
            }
            break;
            default:
                Log.d("分发命令--", "完成");
        }


    }


    public static void main(String[] args) {
        byte[] surplusData = null;//用于记录任务剩余的数据 放入下一个任务继续遍历
        ConsumptionTask task = new ConsumptionTask();
     /*   task.data = new byte[]{AA 55 27 6B F3 01 00 04 03 07
                03 3C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 11 00 9A AA 55 0E 6C F3 02 00 11 00 3C 00 00 00 0C};
*/
        //  Log.d("收到数据", byteArrayToHexStr(task.data));
        List<byte[]> list = new ArrayList<>();
        byte[] data;
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
            if (i + 2 > data.length) {
                //把最后的数据 放在下一个任务中
                surplusData = new byte[data.length - i];
                System.arraycopy(data, i, surplusData, 0, data.length - i);
                return;
            }
            //判断开头
            if (data[i] == SerialPortConstants.SYNC_H && data[i + 1] == SerialPortConstants.SYNC_L) {
                completeData = new byte[(0x00ff & data[i + 2])];
                if (i + completeData.length > data.length) {
                    //把最后的数据 放在下一个任务中
                    surplusData = new byte[data.length - i];
                    System.arraycopy(data, i, surplusData, 0, data.length - i);
                    return;
                } else {
                    System.arraycopy(data, i, completeData, 0, completeData.length);
                    //校验数据
                    if (CRCUitl.CRC8(completeData)) {
                        i = i + completeData.length - 1;
                        list.add(completeData);
                    }
                }


            }

        }


    }


}
