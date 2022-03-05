package com.lepu.vx_data;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static boolean createDir(String dir) {
        File fileDir = new File(dir);
        if (fileDir.exists() && fileDir.isDirectory()) {
            return true;
        } else {
            return fileDir.mkdirs();
        }
    }

    public static File createFile(String fileDir,String fileName) {
        File file = new File(fileDir, fileName);
        if (file.exists() && file.isFile()) {
            try {
                file.delete();
                if (file.createNewFile()) {
                    Log.i(TAG, "write2File: 创建成功 " + file.getAbsolutePath());
                    return file;
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    public static void write2File( File file, String data) {
        OutputStream ou = null;
        try {

            ou = new FileOutputStream(file,true);
            byte[] buffer = data.getBytes();
            ou.write(buffer);
            ou.flush();
//            Toast.makeText(context,"写入成功",Toast.LENGTH_LONG).show();
//            Log.i(TAG, "write2File: 写入成功 " + MainActivity.index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ou != null) {
                    ou.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void write3File( File file, byte[] data) {
        OutputStream ou = null;
        try {

            ou = new FileOutputStream(file,true);

            ou.write(data);
            ou.flush();
//            Toast.makeText(context,"写入成功",Toast.LENGTH_LONG).show();
//            Log.i(TAG, "write2File: 写入成功 " + MainActivity.index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {


                if (ou != null) {
                    ou.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}