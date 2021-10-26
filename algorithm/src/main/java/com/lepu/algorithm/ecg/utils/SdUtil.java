package com.lepu.algorithm.ecg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author wxd
 */
public class SdUtil {

	public static int STORGE_VALUE = 300;// 400MB

	@SuppressLint("NewApi")
	public static boolean checkSdCanUse() {
		boolean flag = false;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			flag = true;
		}
		return flag;
	}

	public static boolean checkReadWrite(File file) {
		boolean isYes = false;
		if (file != null && file.canRead() && file.canWrite()) {
			isYes = true;
		}
		return isYes;
	}

	/**
	 * 检查是否安装外置的SD卡
	 */
	private static boolean checkExternalSDExists() {
		Map<String, String> evn = System.getenv();
		return evn.containsKey("SECONDARY_STORAGE");
	}

	/**
	 * 获取手机 外置SD卡的根目录
	 * 
	 */
	public static String getExternalSDRoot() {
		String sdRootPath = "";
		if (checkExternalSDExists()) {
			Map<String, String> evn = System.getenv();
			sdRootPath = evn.get("SECONDARY_STORAGE");
		}
		return sdRootPath;
	}

	/**
	 * 获取存储路径
	 * 
	 * 优先获取外部的，如果不存在就取内部的存储空间
	 */
	@SuppressLint("InlinedApi")
	public static String getStorgePath(Context context) {

		boolean flag = false;
		String path = null;
		if (checkSdCanUse()) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();

			if (!checkIfFreeSpace(path)) {
				flag = true;
				path = null;
			}

		} else {
			flag = true;
		}

		if (flag) {
			StorageManager sm = (StorageManager) context
					.getSystemService(Context.STORAGE_SERVICE);
			try {
				Method method = sm.getClass().getMethod("getVolumePaths", new Class<?>[]{});
				Object object = method.invoke(sm, new Object[]{});
				String[] paths = (String[]) object;
				if (paths != null && paths.length > 0) {
					for (int i = 0; i < paths.length; i++) {
						File file = new File(paths[i]);
						if (checkReadWrite(file)) {
							path = paths[i];

							if (checkIfFreeSpace(path)) {
								break;
							}
						}
					}
				}
			} catch (Exception e) {
//				KLog.e(Log.getStackTraceString(e));
			}
		}

		return path;
	}

	// =======================================================================================================
	@SuppressWarnings("deprecation")
	public static boolean checkIfFreeSpace(String path) {
		boolean canUse = false;

		File rootFile = new File(path);
		long totalSpace = rootFile.getTotalSpace();
		long usableSpace = rootFile.getUsableSpace();

		//可用空间不足总空间的5%，提示可用空间将满
		if(usableSpace/1024/1024 < totalSpace/1024/1024*0.05){
			canUse = false;
		}else{
			canUse = true;
		}

//		KLog.d(String.format("总空间:%s,可用空间:%s",
//				FileSizeUtil.FormetFileSize(totalSpace),FileSizeUtil.FormetFileSize(usableSpace)));
		return canUse;
	}

	public static String getFilePath(Context context)
	{
		if(context == null)
		{
			return "";
		}
		return context.getFilesDir().getAbsolutePath();
	}
	
	public static String getTempIconPath(Context context)
	{
		if(context == null)
		{
			return "";
		}
		return context.getFilesDir().getAbsolutePath()+"/temp_icon.jpg";
	}
	
	public static String getTempImagePath(Context context, String fileName)
	{
		if(context == null)
		{
			return "";
		}
		return context.getFilesDir().getAbsolutePath()+"/"+fileName+".jpg";
	}
	
	public static String getFileCachePath(Context context)
	{
		if(context == null)
		{
			return "";
		}
		return context.getCacheDir().getAbsolutePath()+"/temp_icon.jpg";
	}

	public static String getCacheDataPath(Context context, String path) {
		if(context == null)
		{
			return "";
		}
		return StringUtil.combinePath(context.getFilesDir().getAbsolutePath(), path + ".txt");
	}

	/**
	 * 获取sd卡总容量
	 * @return
	 */
	public static long getSdTotalSize(Context context){
		long size = 0L;

		try {
			String path = getStorgePath(context);
			StatFs statfs = new StatFs(path);

			long blockSize = statfs.getBlockSizeLong();
			long totalCount = statfs.getBlockCountLong();

			size = totalCount*blockSize;
		}catch (Exception e){
//			KLog.e(Log.getStackTraceString(e));
		}

		return size;
	}

	/**
	 * 获取sd卡剩余容量
	 * @return 字节
	 */
	public static long getSdFreeSize(Context context){
		long size = 0L;

		try {
			String path = getStorgePath(context);
			StatFs statfs = new StatFs(path);

			long blockSize = statfs.getBlockSizeLong();
			long availCount = statfs.getBlockCountLong();

			size = availCount*blockSize;
		}catch (Exception e){
//			KLog.e(Log.getStackTraceString(e));
		}

		return size;
	}
}
