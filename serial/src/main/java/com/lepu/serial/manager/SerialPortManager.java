package com.lepu.serial.manager;

import android.content.Context;
import android.serialport.SerialPort;
import android.util.Log;

import com.lepu.serial.constant.ConfigConst;
import com.lepu.serial.constant.SerialContent;
import com.lepu.serial.enums.ModelEnum;
import com.lepu.serial.listener.CmdNibpReplyListener;
import com.lepu.serial.listener.CmdReplyListener;
import com.lepu.serial.listener.SerialConnectListener;
import com.lepu.serial.obj.CmdNibpReply;
import com.lepu.serial.obj.CmdReply;
import com.lepu.serial.obj.EcgDemoWave;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.task.CmdReplyTimeOutTask;
import com.lepu.serial.task.DataToObjTask;
import com.lepu.serial.uitl.ByteUtils;
import com.lepu.serial.uitl.CRCUitl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
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

    //单例
    private static SerialPortManager instance = null;
    //请求命令回调
    CmdReplyListener mCmdReplyListener;
    List<CmdReplyTimeOutTask> mCmdReplyTimeOutTaskList = new ArrayList<>();
    //血压请求命令回调
    CmdNibpReplyListener mCmdNibpReplyListener;
    ThreadPoolExecutor executorECG = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    ThreadPoolExecutor executorRESP = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    ThreadPoolExecutor executorTEMP = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    ThreadPoolExecutor executorSpO2 = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    ThreadPoolExecutor executorNibp = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    //心电测试数据的游标
    int mTestEcgIndex;
    //上下文
    Context mContext;
    //测试数据
    byte[] mEcgTestData = null;
    //模式
    ModelEnum mModelEnum = ModelEnum.MODEL_NORMAL;
    //关闭标志
    private boolean closeFlag = false;


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
     * @param baudRate   波特率 480600
     */
    public void init(Context context, String devicePath, int baudRate, SerialConnectListener serialConnentListener) {
        try {
            Log.d("SerialPortManager", "初始化串口");
            //打开串口
            SerialPort serialPort = SerialPort //
                    .newBuilder(devicePath, baudRate) // 串口地址地址，波特率
                    .parity(0) // 校验位；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
                    .dataBits(8) // 数据位,默认8；可选值为5~8
                    .stopBits(1) // 停止位，默认1；1:1位停止位；2:2位停止位
                    .build();
            mSerialPort = serialPort;
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
            serialConnentListener.onSuccess();
            //开始定时获取心电图数据
            startGetEcgData();
            mContext = context;
            Log.d("SerialPortManager", "初始化串口成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SerialPortManager", "初始化串口失败");
            serialConnentListener.onFail();
        }


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
                    if (closeFlag) {
                        closeSerialTask();
                        return;
                    }
                    try {
                        //正式数据
                        byte[] buffer = ByteUtils.readStream(mInputStream);
                        if (mModelEnum == ModelEnum.MODEL_TEST) {
                            //测试模式
                            buffer = sendTestEcgDataFile();
                        } else if (mModelEnum == ModelEnum.MODEL_STOP) {
                            buffer = null;
                        }

                        if (buffer!=null){
                            //处理数据
                            dataProcess(buffer);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        closeSerialTask();
                    }

                }
            }, 10, 100, TimeUnit.MILLISECONDS);//每100毫秒获取一次数据
        }
    }

    /**
     * 向串口写入数据
     */
    public void serialSendData(byte[] bytes, CmdReplyListener cmdReplyListener) {
        try {
            mCmdReplyListener = cmdReplyListener;
            writeBytes (bytes);
            CmdReplyTimeOutTask cmdReplyTimeOutTask=
                    new CmdReplyTimeOutTask(cmdReplyListener,new CmdReply(bytes[5],bytes[6]), ConfigConst.CMD_TIMEOUT);
            mCmdReplyTimeOutTaskList.add(cmdReplyTimeOutTask);
            cmdReplyTimeOutTask.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (mCmdReplyListener != null) {
                mCmdReplyListener.onFail(new CmdReply(bytes[5],bytes[6]));
            }
        }
    }

    /**
     * 向串口写入数据 
     */
    public void serialSendData(byte[] bytes,   CmdNibpReplyListener cmdNibpReplyListener) {
        try {
            mCmdNibpReplyListener = cmdNibpReplyListener;
            writeBytes (bytes);
        /*    CmdReplyTimeOutTask cmdReplyTimeOutTask=
                    new CmdReplyTimeOutTask(cmdNibpReplyListener,new CmdReply(bytes[5],bytes[6]), ConfigConst.CMD_NIBP_TIMEOUT);
            mCmdReplyTimeOutTaskList.add(cmdReplyTimeOutTask);
            cmdReplyTimeOutTask.start();*/
        } catch (Exception ex) {
            ex.printStackTrace();
            if (mCmdNibpReplyListener != null) {
                mCmdNibpReplyListener.onFail(new CmdReply(bytes[5],bytes[6]));
            }
        }
    }

    /**
     * 向串口写入数据 血压
     */
    public void serialSendData(byte[] bytes) {
        try {
            writeBytes (bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeBytes (byte[] bytes) throws IOException {
        OutputStream mOutputStream;
        mOutputStream = mSerialPort.getOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            mOutputStream.write(bytes[i]);
        }
        mOutputStream.flush();
    }

    /**
     * 关闭标志
     */
    public void Close() {
        this.closeFlag = false;
    }
    /**
     * 关闭串口 结束读取任务
     */
    private void closeSerialTask() {
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



        mSerialPort.tryClose();


    }


    byte[] surplusData;//用于记录任务剩余的数据 放入下一个任务继续遍历
    int taskindex = 0;
    long time = 0;

    public void dataProcess(byte[] dataArr) {
        time = System.currentTimeMillis();
    //    Log.e("接收到数据时间", time + "");
        byte[] data;
        //如果有剩余的数据，需要把剩余的剩余的数据重新处理
        if (surplusData != null) {
            data = new byte[surplusData.length + dataArr.length];
            System.arraycopy(surplusData, 0, data, 0, surplusData.length);
            System.arraycopy(dataArr, 0, data, surplusData.length, dataArr.length);
            surplusData = null;
        } else {
            data = dataArr;
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
                        taskindex++;
                        System.arraycopy(data, i, completeData, 0, completeData.length);
                        //校验数据
                        if (CRCUitl.CRC8(completeData)  ) {
                            //越过已处理数据
                            i = i + completeData.length - 1;
                            //分发数据
                            distributeMsg(completeData);

                        } else {
                            Log.d("taskindex", taskindex + "");
                        }
                    }
                }

        }
        time = System.currentTimeMillis() - time;
      //  Log.e("接收到处理完数据时间", time + "----"+taskindex);

    }



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
            case SerialMsg.TYPE_ACK: //命令确认包（若有回复包，就不发确认包）0xF1
            case SerialMsg.TYPE_REPLY: {//命令确认包 回复包 0xF2
                for (int i=0;i<mCmdReplyTimeOutTaskList.size();i++){
                    if(mCmdReplyTimeOutTaskList.get(i).getCmdReply().getCmdReplyType()
                            ==new CmdReply(serialMsg ).getCmdReplyType()){
                        mCmdReplyTimeOutTaskList.get(i).cencel();
                        mCmdReplyTimeOutTaskList.remove(i);
                        i--;
                    }
                }

                if (mCmdReplyListener != null) {
                    mCmdReplyListener.onSuccess(new CmdReply(serialMsg ));
                }

            }
            break;
            case SerialMsg.TYPE_DATA: {//数据包 0xF3
                switch (tokenByte) {
                    case SerialContent.TOKEN_ECG: {
                        //上传心电数据
                        executorECG.execute(new DataToObjTask(serialMsg));

                    }
                    break;
                    case SerialContent.TOKEN_RESP: {
                        //上传呼吸RESP
                        executorRESP.execute(new DataToObjTask(serialMsg));

                    }
                    break;
                    case SerialContent.TOKEN_TEMP: {
                        //上传体温数据
                        executorTEMP.execute(new DataToObjTask(serialMsg));

                    }
                    break;

                    case SerialContent.TOKEN_SP02: {
                        //血氧SpO2
                         executorSpO2.execute(new DataToObjTask(serialMsg));

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
                                CmdNibpReply cmdNibpReply = new CmdNibpReply(serialMsg.getContent().data);
                                serialMsg.getContent().type = cmdNibpReply.getCmdType();
                              /*  for (int i = 0; i < mCmdReplyTimeOutTaskList.size(); i++) {
                                    if (mCmdReplyTimeOutTaskList.get(i).getCmdReply().getCmdReplyType()
                                            == new CmdReply(serialMsg).getCmdReplyType()) {
                                        mCmdReplyTimeOutTaskList.get(i).cencel();
                                    }
                                }*/
                                if (mCmdNibpReplyListener!=null){
                                    switch (cmdNibpReply.getACK()){
                                        case  SerialContent.NIBP_REPLY_PACKET_0:{
                                            mCmdNibpReplyListener.obtain_O(new CmdReply(serialMsg));
                                        }
                                        break;
                                        case  SerialContent.NIBP_REPLY_PACKET_K:{
                                            mCmdNibpReplyListener.obtain_K(new CmdReply(serialMsg));
                                        }
                                        break;
                                        case  SerialContent.NIBP_REPLY_PACKET_B:{
                                            mCmdNibpReplyListener.obtain_B(new CmdReply(serialMsg));
                                        }
                                        break;
                                        case  SerialContent.NIBP_REPLY_PACKET_A:{
                                            mCmdNibpReplyListener.obtain_A(new CmdReply(serialMsg));
                                        }
                                        break;
                                        case  SerialContent.NIBP_REPLY_PACKET_N:{
                                            mCmdNibpReplyListener.obtain_N(new CmdReply(serialMsg));
                                        }
                                        break;
                                        case  SerialContent.NIBP_REPLY_PACKET_S:{
                                            mCmdNibpReplyListener.obtain_S(new CmdReply(serialMsg));
                                        }
                                        break;
                                        case  SerialContent.NIBP_REPLY_PACKET_R:{
                                            mCmdNibpReplyListener.obtain_R(new CmdReply(serialMsg));
                                        }
                                        break;
                                    }


                                }
                            }
                            break;
                            case SerialContent.TOKEN_NIBP_DATA_5HZ: //血压NIBP 实时袖带压（5Hz）
                            case SerialContent.TOKEN_NIBP_DATA_200HZ: //实时原始数据（200Hz）
                            case SerialContent.TOKEN_NIBP_BLOOD_PRESSURE_PARAM_MODULE_STATUS: //血压参数和模块状态
                            case SerialContent.TOKEN_NIBP_WORKING_STATUS_OF_BLOOD_PRESSURE_MODULE: //血压模块工作状态
                            case SerialContent.TOKEN_NIBP_BLOOD_PRESSURE_MODULE_INFO: //血压模块信息
                            /*    for (int i = 0; i < mCmdReplyTimeOutTaskList.size(); i++) {
                                    if (mCmdReplyTimeOutTaskList.get(i).getCmdReply().getCmdReplyType()
                                            == new CmdReply(serialMsg).getCmdReplyType()) {
                                        mCmdReplyTimeOutTaskList.get(i).cencel();
                                    }
                                }*/
                                executorNibp.execute(new DataToObjTask(serialMsg));
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

    /**
     * 设置测试模式
     *
     * @param modelEnum
     */
    public void setModel(ModelEnum modelEnum) {
        if (modelEnum==ModelEnum.MODEL_NORMAL){
            mEcgTestData = null;

        }else if (modelEnum==ModelEnum.MODEL_TEST){

        }

        mModelEnum = modelEnum;

    }

    /**
     * 发送测试数据
     */
    private void sendTestEcgData() {

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


        }

    }

    int fileindex = 0;

    /**
     * 发送测试数据
     */
    private byte[] sendTestEcgDataFile() {

        try {
            if (mEcgTestData == null) {
                mEcgTestData = ByteUtils.getFromAssets(mContext);
            }
            //要发送的数据
            int ecgdataLength = 0;
            if (1100 > (mEcgTestData.length - fileindex)) {
                ecgdataLength = mEcgTestData.length - fileindex;
            } else {
                ecgdataLength = 1100;
            }
            byte[] ecgdata = new byte[ecgdataLength];
            System.arraycopy(mEcgTestData, fileindex, ecgdata, 0, ecgdataLength);
            fileindex = fileindex + 1100;
            if (ecgdata.length < 1100) {
                fileindex = 0;
            }
            return ecgdata;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 制作定标
     */
    public short[] makeScale() {

        short[] headArray = new short[50 * 2];
        short[] dataArray = new short[50 * 2];
        short[] endArray = new short[50 * 2];

        //short value = (short) (409*Const.DATA_HIEGHT / Const.SMART_ECG_EXTRA_VALUE);//1mv*2048/5=409
        short value = (short) (ConfigConst.MV_1_SHORT);
        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] = value;
        }

        short[] destArray = new short[headArray.length + dataArray.length + endArray.length];

        System.arraycopy(headArray, 0, destArray, 0, headArray.length);
        System.arraycopy(dataArray, 0, destArray, headArray.length, dataArray.length);
        System.arraycopy(endArray, 0, destArray, headArray.length + dataArray.length, endArray.length);

        return destArray;
    }



    public static void main(String[] args) {
     /*   byte[] data = {(byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0x2F, (byte) 0xF3, (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x3C
                , (byte) 0x00, (byte) 0x00, (byte) 0xC9, (byte) 0xFF, (byte) 0xC5, (byte) 0xFF, (byte) 0xEC, (byte) 0xFF, (byte) 0xCA, (byte) 0xFF, (byte) 0xC5, (byte) 0xFF
                , (byte) 0xEA, (byte) 0xFF, (byte) 0xCC, (byte) 0xFF, (byte) 0xC9, (byte) 0xFF, (byte) 0xED, (byte) 0xFF, (byte) 0xCA, (byte) 0xFF, (byte) 0xCA, (byte) 0xFF, (byte) 0xEF
                , (byte) 0xFF, (byte) 0x33};


        byte[] data1 = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

        System.out.println(data1);*/

     /*   int[] a=new int[]{1,2,3,4,5,6,7,8,9,10};
        int[] b=new int[5];
        System.arraycopy(a,5  ,b,0,5);
        System.out.println(Arrays.toString(b));*/
        System.out.println(aa(0)+"\n");
        System.out.println(aa(50)+"\n");
        System.out.println(aa(60)+"\n");

        int i=1000;
       i=i--;
        System.out.println( i+"\n");
    }

    public static int aa(int spo2){
        int a=((100-spo2)>>1)<<1;

        return a;
    }





}
