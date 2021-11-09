package com.lepu.serial.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.serialport.SerialPort;
import android.util.Log;


import com.jeremyliao.liveeventbus.LiveEventBus;
import com.lepu.serial.constant.SerialContent;
import com.lepu.serial.constant.SerialDataFlieContent;
import com.lepu.serial.obj.CmdReply;
import com.lepu.serial.obj.EcgData1;
import com.lepu.serial.constant.EventMsgConst;
import com.lepu.serial.obj.EcgDemoWave;
import com.lepu.serial.obj.NibpData;
import com.lepu.serial.obj.NibpOriginalData;
import com.lepu.serial.obj.RespData;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.obj.SpO2Data;
import com.lepu.serial.obj.SpO2OriginalData;
import com.lepu.serial.obj.TempData;
import com.lepu.serial.task.BaseTaskBean;
import com.lepu.serial.task.EcgSaveTaskBean;
import com.lepu.serial.task.OnTaskListener;
import com.lepu.serial.task.SerialPortDataTask;
import com.lepu.serial.task.SerialTaskBean;
import com.lepu.serial.uitl.ByteUtils;
import com.lepu.serial.uitl.CRCUitl;
import com.lepu.serial.uitl.FileUtil;
import com.lepu.serial.uitl.StringtoHexUitl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 串口管理 打开串口 读取数据流 写入数据流
 */
public class SerialPortManager {
    //定时获取串口数据任务
    ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    SerialPort mSerialPort;
    InputStream mInputStream;
    OutputStream mOutputStream;
    //处理串口数据队列
    public SerialPortDataTask mSerialPortDataTask;
    //单例
    private static SerialPortManager instance = null;
    //请求命令回调
    CmdReplyListener mCmdReplyListener;
    //心电测试数据的游标
    int mTestEcgIndex;
    //
    Context mContext;
    //
    byte[] ecgTestData = new byte[0];

    long gettasktime = System.currentTimeMillis();

    public static SerialPortManager getInstance() {
        if (instance == null) {
            instance = new SerialPortManager();
        }
        return instance;
    }

    /**
     * 串口初始化
     *
     * @param devicePath 串口名 /dev/ttyS1
     * @param baudrate   波特率 480600
     */
    public void init(Context context, String devicePath, int baudrate) {
        AsyncTask.execute(() -> {
            try {
                //     Log.d("SerialPortManager", "初始化串口");
                //打开串口
                SerialPort serialPort = SerialPort //
                        .newBuilder(devicePath, baudrate) // 串口地址地址，波特率
                        .parity(0) // 校验位；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
                        .dataBits(8) // 数据位,默认8；可选值为5~8
                        .stopBits(1) // 停止位，默认1；1:1位停止位；2:2位停止位
                        .build();
                mSerialPort = serialPort;
                mInputStream = mSerialPort.getInputStream();
                mOutputStream = mSerialPort.getOutputStream();


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //开始定时获取心电图数据
        startGetEcgData();
        //设置任务监听
        mSerialPortDataTask = SerialPortDataTask.getInstance();
        mSerialPortDataTask.setOnTaskListener(onTaskListener);
        mContext = context;
    }

    /**
     * 开始读取串口数据
     */
    public void startGetEcgData() {

        if (mScheduledThreadPoolExecutor == null) {
            mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            mScheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (SerialContent.IS_TEST_DATA) {//测试模式
                     //       sendTestEcgDataFile();
                        } else {//正式数据
                            if (mInputStream == null) return;
                            byte[] buffer = FileUtil.readStream(mInputStream);
                            SerialTaskBean serialTaskBean = new SerialTaskBean();
                            serialTaskBean.data = buffer;
                            BaseTaskBean<SerialTaskBean> baseTaskBean = new BaseTaskBean<>();
                            baseTaskBean.taskBaen = serialTaskBean;
                            baseTaskBean.taskNo = String.valueOf(System.currentTimeMillis());
                            // 添加到排队列表中去
                            mSerialPortDataTask.addTask(baseTaskBean);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, 10, 100, TimeUnit.MILLISECONDS);//每100毫秒获取一次数据
        }
    }

    /**
     * 向串口写入数据
     */
    public void serialSendData(byte[] sendbyte, CmdReplyListener cmdReplyListener) {
        try {
            mCmdReplyListener = cmdReplyListener;
            OutputStream mOutputStream;
            mOutputStream = mSerialPort.getOutputStream();
            for (int i = 0; i < sendbyte.length; i++) {
                mOutputStream.write(sendbyte[i]);
            }
            mOutputStream.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (mCmdReplyListener != null) {
                mCmdReplyListener.onFail(sendbyte[6]);
            }
        }
    }

    /**
     * 关闭串口 结束读取任务
     */
    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        if (mInputStream != null) {
            try {
                mInputStream.close();
                mInputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
                mOutputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mScheduledThreadPoolExecutor != null) {
            try {
                // shutdown只是起到通知的作用
                // 只调用shutdown方法结束线程池是不够的
                mScheduledThreadPoolExecutor.shutdown();
                // (所有的任务都结束的时候，返回TRUE)
                if (!mScheduledThreadPoolExecutor.awaitTermination(0, TimeUnit.MILLISECONDS)) {
                    // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                    mScheduledThreadPoolExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
                e.printStackTrace();
            } finally {
                mScheduledThreadPoolExecutor.shutdownNow();
                mScheduledThreadPoolExecutor = null;
            }
        }

    }


    byte[] surplusData;//用于记录任务剩余的数据 放入下一个任务继续遍历
    int taskindex = 0;
    OnTaskListener<BaseTaskBean<SerialTaskBean>> onTaskListener = new OnTaskListener<BaseTaskBean<SerialTaskBean>>() {
        @Override
        public void exNextTask(BaseTaskBean<SerialTaskBean> task) {
            taskindex++;
            //    Log.d("收到数据", StringtoHexUitl.byteArrayToHexStr(task.taskBaen.data));

            byte[] data;
            //如果有剩余的数据，需要把剩余的剩余的数据重新处理
            if (surplusData != null) {
                data = new byte[surplusData.length + task.taskBaen.data.length];
                System.arraycopy(surplusData, 0, data, 0, surplusData.length);
                System.arraycopy(task.taskBaen.data, 0, data, surplusData.length, task.taskBaen.data.length);
            } else {
                data = task.taskBaen.data;
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
                        //校验数据  测试模式就不用数据校验了
                        if (CRCUitl.CRC8(completeData) || SerialContent.IS_TEST_DATA) {
                            //越过已处理数据
                            i = i + completeData.length - 1;
                            //分发数据
                            distributeMsg(completeData);
                        }
                    }
                }
            }
            if ((taskindex % 500) == 0) {
                Log.d("500次任务", "----");
            }
            mSerialPortDataTask.exOk(task);

        }

        @Override
        public void noTask() {
          /*  if ((System.currentTimeMillis() - gettasktime) > 50) {
                Log.d("noTask", "任务完成时间-----" + (System.currentTimeMillis() - gettasktime));
            } else {
                Log.d("noTask", "任务完成时间" + (System.currentTimeMillis() - gettasktime));
            }*/

        }
    };


    /**
     * 分发消息
     *
     * @param msgdata
     */
    public void distributeMsg(byte[] msgdata) {
        //解析包
        SerialMsg serialMsg = new SerialMsg(msgdata);
        //报文类型（Class字节）用于区分不同的报文便于处理。其范围为0xF0 ~ 0xFB。
        byte classByte = serialMsg.getType();
        //Token：模块类型，用于区分不同的模块，如总模块，心电，血氧等
        byte tokenByte = serialMsg.getContent().token;
        //Type:内容种类，用于识别不同的内容，一个模块里有多种内容。
        byte typeByte = serialMsg.getContent().type;


        switch (classByte) {
            case SerialMsg.TYPE_CMD: {//命令包 0xF0

            }
            break;
            case SerialMsg.TYPE_ACK: {//命令确认包（若有回复包，就不发确认包）0xF1
                if (mCmdReplyListener != null) {
                    mCmdReplyListener.onSuccess(typeByte);
                }
            }
            break;
            case SerialMsg.TYPE_REPLY: {//回复包 0xF2
                CmdReply cmdReply = new CmdReply(typeByte);
                LiveEventBus.get(EventMsgConst.CmdReplyData)
                        .post(cmdReply);
            }
            break;
            case SerialMsg.TYPE_DATA: {//数据包 0xF3
                switch (tokenByte) {
                    case SerialContent.TOKEN_ECG: {
                        //上传心电数据
                        EcgData1 ecgData1 = new EcgData1(serialMsg.getContent().data);
                        LiveEventBus.get(EventMsgConst.MsgEcgData1)
                                .post(ecgData1);
                        //分发到保存心电图数据
                   /*     EcgSaveTaskBean ecgSaveTaskBean = new EcgSaveTaskBean();
                        ecgSaveTaskBean.setEcgSaveTaskBeanType(EcgSaveTaskBean.EcgSaveTaskBeanType.ECG_SAVE_TASK_BEAN_TYPE_ADD_CACHE_DATA);
                        ecgSaveTaskBean.setEcgdata(msgdata);
                        BaseTaskBean<EcgSaveTaskBean> baseTaskBean = new BaseTaskBean<>();
                        baseTaskBean.taskNo = String.valueOf(System.currentTimeMillis());
                        baseTaskBean.taskBaen = ecgSaveTaskBean;
                        EcgDataSaveManager.getInstance().dataSaveTask.addTask(baseTaskBean);*/
                    }
                    break;
                    case SerialContent.TOKEN_RESP: {
                        //上传呼吸RESP
                        RespData respData = new RespData(serialMsg.getContent().data);
                        LiveEventBus.get(EventMsgConst.MsgRespData)
                                .post(respData);
                    }
                    break;
                    case SerialContent.TOKEN_TEMP: {
                        //上传体温数据
                        TempData tempData = new TempData(serialMsg.getContent().data);
                        LiveEventBus.get(EventMsgConst.MsgTempData)
                                .post(tempData);
                    }
                    break;
                    case SerialContent.TOKEN_NIBP: {
                        //血压NIBP
                        if (typeByte == SerialContent.TYPE_DATA_NIBP) {
                            //上传实时袖带压
                            NibpData nibpData = new NibpData(serialMsg.getContent().data);
                            LiveEventBus.get(EventMsgConst.MsgNibpData)
                                    .post(nibpData);
                        } else if (typeByte == SerialContent.TYPE_DATA_NIBP_ORIGINAL) {
                            //上传实时袖带压原始数据
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
                            SpO2OriginalData spO2OriginalData = new SpO2OriginalData(serialMsg.getContent().data);
                            LiveEventBus.get(EventMsgConst.MsgSpO2OriginalData)
                                    .post(spO2OriginalData);
                        } else if (typeByte == SerialContent.TYPE_DATA_SP02_ORIGINAL) {
                            //上传SpO2数据
                            SpO2Data spO2Data = new SpO2Data(serialMsg.getContent().data);
                            LiveEventBus.get(EventMsgConst.MsgSpO2Data)
                                    .post(spO2Data);

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
            default:
        }


    }

    /**
     * 设置测试模式
     *
     * @param testMode
     */
    public void setTestMode(boolean testMode) {
        SerialContent.IS_TEST_DATA = testMode;
    }

    /**
     * 发送测试数据
     */
    private void sendTestEcgData() {
        gettasktime = System.currentTimeMillis();
        //500个数据 4个发送 分11次发完
        int sendCount = 12;
        if (mTestEcgIndex == 480) {
            sendCount = 5;
        } else {
            sendCount = 12;
        }
        for (int k = 0; k < sendCount; k++) {
            if (mTestEcgIndex == 500) {
                mTestEcgIndex = 0;
            }
            //测数据 拼接数据
            byte[] ecgdata = new byte[39];
            System.arraycopy(SerialContent.TEST_ECG_DATA_HEAD, 0, ecgdata, 0, 14);
            for (int i = 0; i < 4; i++) {
                System.arraycopy(ByteUtils.short2byte(EcgDemoWave.INSTANCE.getWaveI()[i + mTestEcgIndex]), 0, ecgdata, 14 + (i * 6), 2);
                System.arraycopy(ByteUtils.short2byte(EcgDemoWave.INSTANCE.getWaveII()[i + mTestEcgIndex]), 0, ecgdata, 14 + 2 + (i * 6), 2);
                System.arraycopy(ByteUtils.short2byte(EcgDemoWave.INSTANCE.getWaveV()[i + mTestEcgIndex]), 0, ecgdata, 14 + 4 + (i * 6), 2);
            }
            ecgdata[38] = CRCUitl.getCRC8(ecgdata, ecgdata.length - 1);

            //测试游标
            mTestEcgIndex = mTestEcgIndex + 4;
            SerialTaskBean serialTaskBean = new SerialTaskBean();
            serialTaskBean.data = ecgdata;
            BaseTaskBean<SerialTaskBean> baseTaskBean = new BaseTaskBean<>();
            baseTaskBean.taskBaen = serialTaskBean;
            baseTaskBean.taskNo = String.valueOf(System.currentTimeMillis());
            // 添加到排队列表中去
            mSerialPortDataTask.addTask(baseTaskBean);

        }

        // Log.d("noTask", "单组处理时间发送时间" + (System.currentTimeMillis() - gettasktime)+"---"+mTestEcgIndex+"---");


    }

    int fileindex = 0;

    /**
     * 发送测试数据
     */
    private void sendTestEcgDataFile() {


        try {
            if (ecgTestData.length == 0) {
                ecgTestData = FileUtil.getFromAssets(mContext);
            }
            //要发送的数据
            int ecgdataLength = 0;
            if (487 > (ecgTestData.length - fileindex)) {
                ecgdataLength = ecgTestData.length - fileindex;
            } else {
                ecgdataLength = 487;
            }
            byte[] ecgdata = new byte[ecgdataLength];
             System.arraycopy(ecgTestData, fileindex, ecgdata, 0, ecgdataLength);
            fileindex = fileindex + 487;
            if (ecgdata.length<487){
                fileindex=0;
            }
            SerialTaskBean serialTaskBean = new SerialTaskBean();
            serialTaskBean.data = ecgdata;
            BaseTaskBean<SerialTaskBean> baseTaskBean = new BaseTaskBean<>();
            baseTaskBean.taskBaen = serialTaskBean;
            baseTaskBean.taskNo = String.valueOf(System.currentTimeMillis());
            // 添加到排队列表中去
            mSerialPortDataTask.addTask(baseTaskBean);

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Log.d("noTask", "单组处理时间发送时间" + (System.currentTimeMillis() - gettasktime)+"---"+mTestEcgIndex+"---");


    }


    public static void main(String[] args) {
        byte[] data = {(byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0x2F, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x3C
                , (byte) 0x00, (byte) 0x00, (byte) 0xC9, (byte) 0xFF, (byte) 0xC5, (byte) 0xFF, (byte) 0xEC, (byte) 0xFF, (byte) 0xCA, (byte) 0xFF, (byte) 0xC5, (byte) 0xFF
                , (byte) 0xEA, (byte) 0xFF, (byte) 0xCC, (byte) 0xFF, (byte) 0xC9, (byte) 0xFF, (byte) 0xED, (byte) 0xFF, (byte) 0xCA, (byte) 0xFF, (byte) 0xCA, (byte) 0xFF, (byte) 0xEF
                , (byte) 0xFF, (byte) 0x33};


    }


    /**
     * 请求命令回调
     */
    public interface CmdReplyListener {
        //请求成功
        void onSuccess(byte cmdType);

        //请求失败
        void onFail(byte cmdType);

    }


}
