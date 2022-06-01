package com.lepu.serial.jni;

public class SerialPortJni {
    private static SerialPortJni instance = null;
    public static SerialPortJni getInstance() {
        if (instance == null) {
            instance = new SerialPortJni();
        }
        return instance;
    }
    //JNI
    static {
        System.loadLibrary("lepuUart");
    }
    //打开串口库 jniUartOpen 返回-1打开串口失败
    public native int jniUartOpen();
    //关闭串口库
    public native int jniUartClose();
    //读取串口数据
    public native int jniUartRead(byte[] recvdata);
    //写入串口数据
    public native void jniUartWrite(int cmdLen, byte[] cmdData);
}
