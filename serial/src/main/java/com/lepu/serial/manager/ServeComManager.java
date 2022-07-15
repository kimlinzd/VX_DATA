package com.lepu.serial.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.SerialManager;
import android.hardware.SerialPort;
import android.util.Log;

import com.lepu.serial.constant.ConfigConst;
import com.lepu.serial.constant.SerialCmd;
import com.lepu.serial.constant.SerialContent;
import com.lepu.serial.enums.ModelEnum;
import com.lepu.serial.enums.NibpMsmEnum;
import com.lepu.serial.listener.CmdNibpReplyListener;
import com.lepu.serial.listener.CmdReplyListener;
import com.lepu.serial.listener.SerialConnectListener;
import com.lepu.serial.obj.CmdNibpReply;
import com.lepu.serial.obj.CmdReply;
import com.lepu.serial.obj.NibpInfo;
import com.lepu.serial.obj.SerialMsg;
import com.lepu.serial.task.CmdReplyTimeOutTask;
import com.lepu.serial.task.DataToObjTask;
import com.lepu.serial.uitl.ByteUtils;
import com.lepu.serial.uitl.CRCUitl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  有篇文献把上位机说成主机(master),下位机说成从机(serve)
 *  serve communication  下位机通讯管理
 */
public class ServeComManager {
    private static String SERIAL_SERVICE = "serial";
    //定时获取串口数据任务
    ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;

    private ByteBuffer mInputBuffer;
    private ByteBuffer mOutputBuffer;
    private SerialManager mSerialManager;
    private SerialPort mSerialPort;

    //单例
    private static ServeComManager instance = null;
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
    //串口写入线程
    ThreadPoolExecutor executorWrite= (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    //上下文
    Context mContext;
    //测试数据
    byte[] mEcgTestData = null;
    //模式
    ModelEnum mModelEnum = ModelEnum.MODEL_NORMAL;
    //关闭标志
    private boolean closeFlag = false;
    //NIBP
    public NibpInfo nibpInfo = new NibpInfo();


    public static ServeComManager getInstance() {
        if (instance == null) {
            instance = new ServeComManager();
        }
        return instance;
    }

    /**
     * 串口初始化
     */
    @SuppressLint("WrongConstant")
    public void init(Context context, SerialConnectListener serialConnentListener) {
        try {
            Log.d("SerialPortManager", "初始化串口");
            mContext=context;
            mInputBuffer = ByteBuffer.allocateDirect(4096);
            mOutputBuffer = ByteBuffer.allocateDirect(4096);

            mSerialManager = (SerialManager)mContext.getSystemService(SERIAL_SERVICE);

            String[] ports = mSerialManager.getSerialPorts();
         //   Log.e("pingyh", "serialPortInit lenth="+ports.length+" ports="+ports[0]);
            if (ports != null && ports.length > 0) {
                try {
                    mSerialPort = mSerialManager.openSerialPort("/dev/ttyS1"/*ports[0]*/, 460800);
             //       Log.e("pingyh", "pingyh mSerialPort="+mSerialPort);
                    if (mSerialPort != null) {
                        serialConnentListener.onSuccess();
                        //开始定时获取心电图数据
                        startGetEcgData();
                        Log.d("SerialPortManager", "初始化串口成功");
                    }else {
                        serialConnentListener.onFail();
                    }
                } catch (IOException e) {
                    Log.e("pingyh", "onEditorAction IOException="+e);
                    serialConnentListener.onFail();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SerialPortManager", "初始化串口失败");
            serialConnentListener.onFail();
        }


    }



    /**
     * 开始读取串口数据
     */
    int readTimeOut=5;//超时未获取到参数板次数 在正确模式下（MODEL_NORMAL） 超过次数需要发送启动数据传输命令
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
                        mInputBuffer.clear();
                        int ret = mSerialPort.read(mInputBuffer);
                        byte[] buffer = new byte[ret];
                        if (ret!=0){
                            mInputBuffer.get(buffer, 0, ret);
                        }
                        if (mModelEnum == ModelEnum.MODEL_TEST) {
                            //测试模式
                            buffer = sendTestEcgDataFile();
                        } else if (mModelEnum == ModelEnum.MODEL_STOP) {
                            buffer = null;
                        }

                        //判断是否在正常模式下获取不到数据
                        if (mModelEnum==ModelEnum.MODEL_NORMAL&&buffer==null){
                            readTimeOut-=1;
                            if (readTimeOut<=0){
                                serialSendData(SerialCmd.cmdDataStart());
                            }
                        }else {
                            readTimeOut=5;
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
            }, 10, 50, TimeUnit.MILLISECONDS);//每100毫秒获取一次数据
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

    private void  writeBytes (byte[] bytes)  {

        executorWrite.execute(new Runnable() {
            @Override
            public void run() {
                try {
                mOutputBuffer.clear();
                mOutputBuffer.put(bytes);
                mSerialPort.write(mOutputBuffer, bytes.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 关闭标志
     */
    public void Close() {
        this.closeFlag = true;
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
    int index=0;
    public void distributeMsg(byte[] msgdata) {
        //解析包
        SerialMsg serialMsg = new SerialMsg(msgdata);
        //报文类型（Class字节）用于区分不同的报文便于处理。其范围为0xF0 ~ 0xFB。
        byte classByte = serialMsg.getType();
        //Token：模块类型，用于区分不同的模块，如总模块，心电，血氧等
        byte tokenByte = serialMsg.getContent().token;
        //Type:内容种类，用于识别不同的内容，一个模块里有多种内容。
        byte typeByte = serialMsg.getContent().type;

    //    Log.e("lzd","index=="+index+"----="+(Math.abs(serialMsg.getIndex())- Math.abs(index)) );
        if ( Math.abs((Math.abs(serialMsg.getIndex())- Math.abs(index)))!=1){
            Log.e("lzd","丢包了");
        }
        index=serialMsg.getIndex();

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
                                handNibpReply(serialMsg);
                            }
                            break;
                            case SerialContent.TOKEN_NIBP_DATA_5HZ: //血压NIBP 实时袖带压（5Hz）
                            case SerialContent.TOKEN_NIBP_DATA_200HZ: //实时原始数据（200Hz）
                            case SerialContent.TOKEN_NIBP_BLOOD_PRESSURE_PARAM_MODULE_STATUS: //血压参数和模块状态
                            case SerialContent.TOKEN_NIBP_WORKING_STATUS_OF_BLOOD_PRESSURE_MODULE: //血压模块工作状态
                            case SerialContent.TOKEN_NIBP_BLOOD_PRESSURE_MODULE_INFO: //血压模块信息
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
            serialSendData(SerialCmd.cmdDataStart());
        }else if (modelEnum==ModelEnum.MODEL_TEST){
       //    serialSendData(SerialCmd.cmdDataStop());
        }else if (modelEnum==ModelEnum.MODEL_STOP){
       //     serialSendData(SerialCmd.cmdDataStop());
        }

        mModelEnum = modelEnum;

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
            if (550 > (mEcgTestData.length - fileindex)) {
                ecgdataLength = mEcgTestData.length - fileindex;
            } else {
                ecgdataLength = 550;
            }
            byte[] ecgdata = new byte[ecgdataLength];
            System.arraycopy(mEcgTestData, fileindex, ecgdata, 0, ecgdataLength);
            fileindex = fileindex + 550;
            if (ecgdata.length < 550) {
                fileindex = 0;
            }
            return ecgdata;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理NIBP命令返回
     *
     * @param serialMsg
     */
    public void handNibpReply(SerialMsg serialMsg) {
        CmdNibpReply cmdNibpReply = new CmdNibpReply(serialMsg.getContent().data);
        serialMsg.getContent().type = cmdNibpReply.getCmdType();
        CmdReply cmdReply = new CmdReply(serialMsg);
        switch (cmdNibpReply.getACK()) {
            case SerialContent.NIBP_REPLY_PACKET_0: {
                switch (cmdReply.getCmdReplyType()) {
                    case CMD_TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT: {
                        //开始手动血压测量
                        nibpInfo.setNibpMsmEnum(NibpMsmEnum.MANUAL);
                    }
                    break;
                    case CMD_TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT: {
                        //开始连续测量
                        nibpInfo.setNibpMsmEnum(NibpMsmEnum.CONTINUOUS);
                    }
                    break;
                    case CMD_TOKEN_NIBP_AUXILIARY_VENIPUNCTURE: {
                        //静脉穿刺
                        nibpInfo.setNibpMsmEnum(NibpMsmEnum.AUXILIARY_VENIPUNCTURE);
                    }
                    break;
                    case CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1: {
                        //压力校验模式1（内部充气源）
                        nibpInfo.setNibpMsmEnum(NibpMsmEnum.PRESSURE_TEST_MODE_1);
                    }
                    break;
                    case CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2: {
                        //压力校验模式2（外部充气源
                        nibpInfo.setNibpMsmEnum(NibpMsmEnum.PRESSURE_TEST_MODE_2);
                    }
                    break;
                    case CMD_TOKEN_NIBP_LEAK_DETECTION: {
                        //漏气检测
                        nibpInfo.setNibpMsmEnum(NibpMsmEnum.AIR_LEAK_DETECTION);
                    }
                    break;
                }
                if (mCmdNibpReplyListener != null) {
                    mCmdNibpReplyListener.obtain_O(cmdReply);
                }

            }
            break;
            case SerialContent.NIBP_REPLY_PACKET_K: {
                if (mCmdNibpReplyListener != null) {
                    mCmdNibpReplyListener.obtain_K(new CmdReply(serialMsg));
                }

            }
            break;
            case SerialContent.NIBP_REPLY_PACKET_B: {
                if (mCmdNibpReplyListener != null) {
                    mCmdNibpReplyListener.obtain_B(new CmdReply(serialMsg));
                }

            }
            break;
            case SerialContent.NIBP_REPLY_PACKET_A: {
                nibpInfo.setNibpMsmEnum(NibpMsmEnum.IDLE);
                if (mCmdNibpReplyListener != null) {
                    mCmdNibpReplyListener.obtain_A(new CmdReply(serialMsg));
                }

            }
            break;
            case SerialContent.NIBP_REPLY_PACKET_N: {
                if (mCmdNibpReplyListener != null) {
                    mCmdNibpReplyListener.obtain_N(new CmdReply(serialMsg));
                }

            }
            break;
            case SerialContent.NIBP_REPLY_PACKET_S: {
                if (mCmdNibpReplyListener != null) {
                    mCmdNibpReplyListener.obtain_S(new CmdReply(serialMsg));
                }

            }
            break;
            case SerialContent.NIBP_REPLY_PACKET_R: {
                if (mCmdNibpReplyListener != null) {
                    mCmdNibpReplyListener.obtain_R(new CmdReply(serialMsg));
                }

            }
            break;
        }


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
