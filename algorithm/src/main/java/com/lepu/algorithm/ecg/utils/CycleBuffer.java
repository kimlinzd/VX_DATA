package com.lepu.algorithm.ecg.utils;

import android.util.Log;

//import com.lib.common.util.log.KLog;

/**
 * @author wxd
 */
public class CycleBuffer {

    private short[][] bufferData;//滤波缓冲的心电数据
    private int currPos = 0;// 记录的 位置
    private int  dataLength = 0;// 记录的 位置

    public CycleBuffer(int leadNum,int dataLen){
        bufferData = new short[leadNum][dataLen];
    }

    /**
     * 清理内存中的数据
     */
    public void clearCycleBuffer() {
        currPos = 0;
        dataLength =0;
    }

    public void putECGData(short[][] dataArray) {
        try {
            int newDataLen = dataArray[0].length;
            //每次读取数据的长度
            for (int i = 0; i < dataArray.length; i++) {
                if (currPos + newDataLen >= bufferData[0].length) {
                    int firstCopyLen =  bufferData[0].length - currPos;
                    int secondCopyLen = newDataLen - firstCopyLen;
                    //拷贝数据到头部

                    System.arraycopy(dataArray[i], 0, bufferData[i], currPos, firstCopyLen);
                    System.arraycopy(dataArray[i], firstCopyLen, bufferData[i], 0, secondCopyLen);
                } else {
                    System.arraycopy(dataArray[i], 0, bufferData[i], currPos, newDataLen);
                }
            }

            currPos += newDataLen;
            currPos %= bufferData[0].length;

            dataLength += dataArray[0].length;
        } catch (Exception e) {
//            KLog.e(Log.getStackTraceString(e));
        }
    }

    public short[][] getFilterECGData(int filterLen)
    {
        int totalGetLen  =  filterLen + 400;
        if(totalGetLen > dataLength){
            return null;
        }

        short[][] retData = new short[bufferData.length][totalGetLen];

        for (int i = 0; i < retData.length; i++) {
            int retDataLen = totalGetLen;
            if (currPos  < retDataLen) {
                int firstCopyLen =  retDataLen - currPos;
                int secondCopyLen = retDataLen - firstCopyLen;//拷贝数据到头部

                System.arraycopy(bufferData[i], bufferData[i].length - firstCopyLen , retData[i], 0,
                        firstCopyLen);
                System.arraycopy(bufferData[i], 0, retData[i], firstCopyLen, secondCopyLen);

            } else {
                System.arraycopy( bufferData[i], currPos -retDataLen ,retData[i] , 0, retDataLen);
            }
        }

        dataLength -= filterLen;

        return retData;
    }

    public int getDataLen(){
        return dataLength;
    }
}