package com.lepu.serial.uitl;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.lepu.serial.R;
import com.lepu.serial.constant.SerialContent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.lepu.serial.constant.SerialDataFlieContent.ECG_PATH;

public class FileUtil {
    //文件后缀
    private static final String CRASH_REPORTER_EXTENSION = "";
    public static String[] units = {"B", "KB", "MB", "GB", "TB"};

    public static int index = 0;

    public static void writeBytesToFile(Context context, byte[] bytes) throws IOException {
        File file = new File(getCrashFilePath(context) + ECG_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        String now = String.valueOf(System.currentTimeMillis());
        OutputStream out = new FileOutputStream(file.getAbsolutePath() + "/" + now);
        index++;
        InputStream is = new ByteArrayInputStream(bytes);
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            out.write(buff, 0, len);
        }
        is.close();
        out.close();
    }


    /**
     * 获取文件夹路径
     *
     * @param context
     * @return
     */
    private static String getCrashFilePath(Context context) {
        String path = null;
        try {
            path = Environment.getExternalStorageDirectory().getCanonicalPath() + "/"
                    + context.getResources().getString(R.string.app_name) + "/Crash/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("TAG", "getCrashFilePath: " + path);
        return path;
    }

    public static void queryStorage() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        //存储块总数量
        long blockCount = statFs.getBlockCountLong();
        //块大小
        long blockSize = statFs.getBlockSizeLong();
        //可用块数量
        long availableCount = statFs.getAvailableBlocksLong();
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        long freeBlocks = statFs.getFreeBlocksLong();
        //这两个方法是直接输出总内存和可用空间，也有getFreeBytes
        //API level 18（JELLY_BEAN_MR2）引入
        long totalSize = statFs.getTotalBytes();
        long availableSize = statFs.getAvailableBytes();

        Log.d("statfs", "total = " + getUnit(totalSize));
        Log.d("statfs", "availableSize = " + getUnit(availableSize));
        //这里可以看出 available 是小于 free ,free 包括保留块。
        Log.d("statfs", "total = " + getUnit(blockSize * blockCount));
        Log.d("statfs", "available = " + getUnit(blockSize * availableCount));
        Log.d("statfs", "free = " + getUnit(blockSize * freeBlocks));
    }

    public static String getUnit(float size) {
        int index = 0;
        while (size > 1024 && index < 4) {
            size = size / 1024;
            index++;
        }
        return String.format(Locale.getDefault(), " %.2f %s", size, units[index]);
    }

    /**
     * 获取目录下所有文件(按时间排序)
     *
     * @param path
     * @return
     */
    public static List<File> listFileSortByModifyTime(String path) {
        List<File> list = getFiles(path, new ArrayList());

        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    File file = (File) o1;
                    File newFile = (File) o2;
                    if (file.lastModified() < newFile.lastModified()) {
                        return -1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        }
        return list;
    }

    /**
     * 获取目录下所有文件
     *
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * 串口读取专用
     * @param inStream
     * @return 字节数组
     * @throws Exception
     * @功能 读取流
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        int count = 0;
        while (count == 0 && !SerialContent.IS_TEST_DATA) {
            count = inStream.available();
        }
        byte[] b = new byte[count];
        inStream.read(b);
        return b;
    }

    /**
     * 创建文件夹
     * @param pach
     */
   public static void createFile(String pach){
       File file=new File(pach);
       if (!file.exists()){
           file.mkdir();
       }
   }
}
