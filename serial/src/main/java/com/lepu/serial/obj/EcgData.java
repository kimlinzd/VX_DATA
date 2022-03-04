package com.lepu.serial.obj;

import androidx.annotation.NonNull;

import com.lepu.serial.uitl.ByteUtils;
import com.lepu.serial.uitl.StringtoHexUitl;

import java.io.Serializable;

/**
 * 心电数据
 * Token	Type
 * 0x01 	0x00
 */
public class EcgData implements Serializable {

    int len;//采样点数
    int set_1mv;//cal  0x00 ：关闭CAL输出       0x01 ：打开CAL输出
    int chn0Index;//当前通道数为1时有效,当前导联名称： I导联=0,II导联=1,III导联=2
    int chn;//当前导联通道数，3导联：chn=1,5导联：chn=3
    int[] leadOff;//导联脱落 1为脱落 0为不脱落 当全部为1时，说明说有导联都脱落，包括R在内。RA或RL脱落会导致所有导联都脱落。顺序为V6 V5 V4 V3 V2 V1 F L
    int[] dCout;//1为过高 0为正常 顺序为V6 V5 V4 V3 V2 V1 F L
    int hr;//心率值
    int[] flagPR;//博动音标志 0为无标志 1为有标志 顺序为PR0 PR1 PR2 PR3
    int[] flagPU;//博起标志 顺序为 PU0 PU1 PU2 PU3
    short[][] ecgWave;//通道数据
    byte[] originalData;//原始数据 用于保存
    //记录ecg游标 用于测试是否丢包 自定义参数 非参数板提供
    int ecgIndex;

    /**
     * 给缓存数据的时候使用
     */
    long startTime;

    /**
     * 导联数据， short数组  通过计算得出
     */
    private short[] I;
    private short[] II;
    private short[] III;
    private short[] aVR;
    private short[] aVL;
    private short[] aVF;
    private short[] V;

    public EcgData() {
    }

    public EcgData(byte[] buf) {
        originalData = buf;
        len = buf[0] & 0x0f;
        set_1mv = buf[1] >> 6;
        chn0Index = buf[1] >> 4 & 3;
        chn = buf[1] & 0x0f;
        leadOff = new int[]{buf[2] >> 7 & 0x1, buf[2] >> 6 & 0x1, buf[2] >> 5 & 0x1, buf[2] >> 4 & 0x1, buf[2] >> 3 & 0x1, buf[2] >> 2 & 0x1, buf[2] >> 1 & 0x1, buf[2] >> 0 & 0x1};
        dCout = new int[]{buf[3] >> 7 & 0x1, buf[3] >> 6 & 0x1, buf[3] >> 5 & 0x1, buf[3] >> 4 & 0x1, buf[3] >> 3 & 0x1, buf[3] >> 2 & 0x1, buf[3] >> 1 & 0x1, buf[3] >> 0 & 0x1};
       // hr = (buf[4] & 0xff + ((buf[5] & 0xff) << 8));
        hr = (buf[4] | (((short)buf[5]) << 8));
        flagPR = new int[]{buf[6] >> 4 & 0x1, buf[6] >> 5 & 0x1, buf[6] >> 6 & 0x1, buf[6] >> 7 & 0x1};
        flagPU = new int[]{buf[6] >> 0 & 0x1, buf[6] >> 1 & 0x1, buf[6] >> 2 & 0x1, buf[6] >> 3 & 0x1};
        ecgWave = new short[chn][len];
        //导联数据
        I = new short[len];
        II = new short[len];
        III = new short[len];
        aVR = new short[len];
        aVL = new short[len];
        aVF = new short[len];
        V = new short[len];

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < chn; j++) {
                int index1 = j * 2 + i * 2 * chn + 7;
                int index2 = j * 2 + i * 2 * chn + 8;
                short ecgWaveData = (short) ByteUtils.bytes2Short(buf[index1], buf[index2]);
                ecgWave[j][i] = ecgWaveData;
            }
        }

        //计算出其他通道数据
        switch (chn) {
            case 1: {
                //3导联
                for (int i = 0; i < len; i++) {
                    I[i] = (short) (ecgWave[0][i]);
                    II[i] = (short) 0;
                    // III = II - I
                    this.III[i] = (short) 0;
                    // aVR = -(I+II)/2
                    this.aVR[i] = (short) 0;
                    // aVL = (2*I-II)/2
                    this.aVL[i] = (short) 0;
                    // aVF = (2*II-I)/2
                    this.aVF[i] = (short) 0;
                    // v1
                    this.V[i] = (short) 0;
                }
            }
       break;
            case 3: {
                //五导联
                for (int i = 0; i < len; i++) {
                    I[i] = (short) (ecgWave[0][i]);
                    II[i] = (short) (ecgWave[1][i]);
                    // III = II - I
                    this.III[i] = (short) (II[i] - I[i]);
                    // aVR = -(I+II)/2
                    this.aVR[i] = (short) ((II[i] + I[i]) / -2);
                    // aVL = (2*I-II)/2
                    this.aVL[i] = (short) ((2 * I[i] - II[i]) / 2);
                    // aVF = (2*II-I)/2
                    this.aVF[i] = (short) ((2 * II[i] - I[i]) / 2);
                    // v1
                    this.V[i] = (short) (ecgWave[2][i]);
                }
            }
            break;
        }


    }

    /**
     * 这个方法只解析波形和心率
     * @param buf
     * @return
     */
    public EcgData getWave(byte[] buf){
        EcgData ecgData=new EcgData();
        ecgData.ecgWave = new short[chn][len];
        ecgData.len = buf[0] & 0x0f;
        ecgData.chn = buf[1] & 0x0f;
        ecgData.hr = (buf[4] & 0xff + ((buf[5] & 0xff) << 8));
        for (int i = 0; i < ecgData.len; i++) {
            for (int j = 0; j < ecgData.chn; j++) {
                int index1 = j * 2 + i * 2 * ecgData.chn + 7;
                int index2 = j * 2 + i * 2 * ecgData.chn + 8;
                short ecgWaveData = (short) ByteUtils.bytes2Short(buf[index1], buf[index2]);
                ecgData.ecgWave[j][i] = ecgWaveData;
            }
        }
        return ecgData;
    }


    public void toBytes(EcgData ecgData, short len) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (len & 0xff >> 8);
        bytes[1] = (byte) (len & 0xff);


     /*   len = buf[0] & 0x0f;
        set_1mv = buf[1] >> 6;
        chn0Index = buf[1] >> 4 & 3;
        chn = buf[1] & 0x0f;
        leadOff = new int[]{buf[2] >> 7 & 0x1, buf[2] >> 6 & 0x1, buf[2] >> 5 & 0x1, buf[2] >> 4 & 0x1, buf[2] >> 3 & 0x1, buf[2] >> 2 & 0x1, buf[2] >> 1 & 0x1, buf[2] >> 0 & 0x1};
        dCout = new int[]{buf[3] >> 7 & 0x1, buf[3] >> 6 & 0x1, buf[3] >> 5 & 0x1, buf[3] >> 4 & 0x1, buf[3] >> 3 & 0x1, buf[3] >> 2 & 0x1, buf[3] >> 1 & 0x1, buf[3] >> 0 & 0x1};
        hr = (buf[4] & 0xff + ((buf[5] & 0xff) << 8));
        flagPR = new int[]{buf[6] >> 4 & 0x1, buf[6] >> 5 & 0x1, buf[6] >> 6 & 0x1, buf[6] >> 7 & 0x1};
        flagPU = new int[]{buf[6] >> 0 & 0x1, buf[6] >> 1 & 0x1, buf[6] >> 2 & 0x1, buf[6] >> 3 & 0x1};
        ecgWave = new short[chn][len];
        //导联数据
        I = new short[len];
        II = new short[len];
        III = new short[len];
        aVR = new short[len];
        aVL = new short[len];
        aVF = new short[len];
        V = new short[len];

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < chn; j++) {
                int index1 = j * 2 + i * 2 * chn + 7;
                int index2 = j * 2 + i * 2 * chn + 8;
                short ecgWaveData = (short) ByteUtils.bytes2Short(buf[index1], buf[index2]);
                ecgWave[j][i] = ecgWaveData;
            }
        }*/
    }

    /**
     * 这个构造方法是给测试数据用的
     *
     * @param ecgdata
     */
    public EcgData(short[][] ecgdata) {
        set_1mv = 0;
        chn0Index = 0;
        leadOff = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        dCout = new int[]{0, 0, 0, 0, 0, 0, 1, 1};
        hr = 60;
        flagPR = new int[]{0, 0, 0, 0};
        flagPU = new int[]{0, 0, 0, 0};

        chn = 3;
        len = 4;
        this.ecgWave = ecgdata;
        //导联数据
        I = new short[4];
        II = new short[4];
        III = new short[4];
        aVR = new short[4];
        aVL = new short[4];
        aVF = new short[4];
        V = new short[4];
        //计算出其他通道数据
        if (chn == 3) {

            for (int i = 0; i < len; i++) {
                I[i] = (short) (ecgWave[0][i] - 0x8000);
                II[i] = (short) (ecgWave[1][i] - 0x8000);

                // III = II - I
                this.III[i] = (short) (II[i] - I[i]);
                // aVR = -(I+II)/2
                this.aVR[i] = (short) ((II[i] + I[i]) / -2);
                // aVL = (2*I-II)/2
                this.aVL[i] = (short) ((2 * I[i] - II[i]) / 2);
                // aVF = (2*II-I)/2
                this.aVF[i] = (short) ((2 * II[i] - I[i]) / 2);
                // v1
                this.V[i] = (short) (ecgWave[2][i] - 0x8000);
            }
        }

    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getSet_1mv() {
        return set_1mv;
    }

    public void setSet_1mv(int set_1mv) {
        this.set_1mv = set_1mv;
    }

    public int getChn0Index() {
        return chn0Index;
    }

    public void setChn0Index(int chn0Index) {
        this.chn0Index = chn0Index;
    }

    public int getChn() {
        return chn;
    }

    public void setChn(int chn) {
        this.chn = chn;
    }

    public int[] getLeadOff() {
        return leadOff;
    }

    public void setLeadOff(int[] leadOff) {
        this.leadOff = leadOff;
    }

    public int[] getdCout() {
        return dCout;
    }

    public void setdCout(int[] dCout) {
        this.dCout = dCout;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int[] getFlagPR() {
        return flagPR;
    }

    public void setFlagPR(int[] flagPR) {
        this.flagPR = flagPR;
    }

    public int[] getFlagPU() {
        return flagPU;
    }

    public void setFlagPU(int[] flagPU) {
        this.flagPU = flagPU;
    }

    public short[][] getEcgWave() {
        return ecgWave;
    }

    public void setEcgWave(short[][] ecgWave) {
        this.ecgWave = ecgWave;
    }

    public short[] getI() {
        return I;
    }

    public void setI(short[] i) {
        I = i;
    }

    public short[] getII() {
        return II;
    }

    public void setII(short[] II) {
        this.II = II;
    }

    public short[] getIII() {
        return III;
    }

    public void setIII(short[] III) {
        this.III = III;
    }

    public short[] getaVR() {
        return aVR;
    }

    public void setaVR(short[] aVR) {
        this.aVR = aVR;
    }

    public short[] getaVL() {
        return aVL;
    }

    public void setaVL(short[] aVL) {
        this.aVL = aVL;
    }

    public short[] getaVF() {
        return aVF;
    }

    public void setaVF(short[] aVF) {
        this.aVF = aVF;
    }

    public short[] getV() {
        return V;
    }

    public void setV(short[] v) {
        V = v;
    }

    public byte[] getOriginalData() {
        return originalData;
    }

    public void setOriginalData(byte[] originalData) {
        this.originalData = originalData;
    }

    public int getEcgIndex() {
        return ecgIndex;
    }

    public void setEcgIndex(int ecgIndex) {
        this.ecgIndex = ecgIndex;
    }

    @NonNull
    @Override
    protected EcgData clone() throws CloneNotSupportedException {
        return (EcgData) super.clone();
    }

    public static void main(String[] args) {
        byte[] data = {(byte) 0xAA, (byte) 0x55, (byte) 0x27, (byte) 0x6B, (byte) 0xF3, (byte) 0x01, (byte) 0x00
                , (byte) 0x04, (byte) 0x03, (byte) 0x07, (byte) 0x03, (byte) 0x3C, (byte) 0x00, (byte) 0x00

                , (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                , (byte) 0x03, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                , (byte) 0x05, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                , (byte) 0x07, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x00,
                (byte) 0x9A};


        SerialMsg serialMsg = new SerialMsg(data);
        //     EcgData ecgData = new EcgData(serialMsg.content.data);

      //  System.out.println(data.length + "");
        short len = 600;
        System.out.println(StringtoHexUitl.shortToBit(len));
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (len>> 8& 0xff);
        bytes[1] = (byte) (len & 0xff);
        System.out.println(StringtoHexUitl.byteToBit(bytes[0]));
        System.out.println(StringtoHexUitl.byteToBit( bytes[1]));

        System.out.println(ByteUtils.bytes2Short(bytes[1], bytes[0]) + "");


    }


}
