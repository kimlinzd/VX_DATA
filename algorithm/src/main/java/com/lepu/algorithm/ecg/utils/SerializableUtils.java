package com.lepu.algorithm.ecg.utils;
import android.util.Log;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableUtils {

    /**
     * 对象深拷贝
     * @param old
     * @return
     */
    public static Object copy(Object old) {
        Object clazz = null;
        try {
            // 写入字节流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(old);
            // 读取字节流
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clazz = (Object) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
//            KLog.d(Log.getStackTraceString(e));
        }
        return clazz;
    }
}
