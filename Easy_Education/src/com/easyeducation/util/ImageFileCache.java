package com.easyeducation.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class ImageFileCache {
	private static final String TAG = "ImageFileCache";
	// 目录名称
	private static final String CACHDIR = "ImgCach";
	// 图片缓存后缀
	private static final String WHOLESALE_CONV = ".cach";
	private static final int MB = 1024 * 1024;
	private static final int CACHE_SIZE = 10;
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

	public ImageFileCache() {
		// 清理文件缓存
		removeCache(getDirectory());
	}
	/** 从缓存中获取图片 **/
	public Bitmap getImage(final String url) {
		Log.d(TAG, "getImage()--");
		final String path = getDirectory() + "/" + convertUrlToFileName(url);
//		System.out.println(path);
		File file = new File(path);
		if (file.exists()) {
			Bitmap bmp = BitmapFactory.decodeFile(path);
			if (bmp == null) {
				file.delete();
			} else {
				updateFileTime(path);
				return bmp;
			}
		}
		return null;
	}
	/** 将图片存入文件缓�? **/
	public void saveBitmap(Bitmap bm, String url) {
		Log.d(TAG, "saveBitmap()--");
		if (bm == null) {
			return;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			// SD空间不足
			return;
		}
		String filename = convertUrlToFileName(url);
		String dir = getDirectory();
		File dirFile = new File(dir);
		// 判断缓存目录是否存在
		if (!dirFile.exists())
			dirFile.mkdirs();
		// 构�?�文件实�?
		File file = new File(dir + "/" + filename);
		try {
			// 创建该文�?
			file.createNewFile();
			// 以该文件构�?�输出流
			OutputStream outStream = new FileOutputStream(file);
			// 将该bitmap写入到outStream输出�?
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			System.out.println("------------464654");
		} catch (FileNotFoundException e) {
			Log.w("ImageFileCache", "FileNotFoundException");
		} catch (IOException e) {
			Log.w("ImageFileCache", "IOException");
		}
	}
	/**
	 * 计算存储目录下的文件大小�?
	 * 当文件�?�大小大于规定的CACHE_SIZE或�?�sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规�?
	 * 那么删除40%�?近没有被使用的文�?
	 */
	private boolean removeCache(String dirPath) {
		// 以路径dirPath构�?�File实例
		File dir = new File(dirPath);
		// 图片文件数组，dir不是路径时，返回null
		File[] files = dir.listFiles();
		if (files == null) {
			return true;
		}
		// 判断 sd卡的存储状�?�必须可读可写，否则返回false
		if (!android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return false;
		}
		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			// 判断该文件名是否存在WHOLESALE_CONV后缀
			// 存在则获取文件长度，长度�?0说明文件不存�?
			if (files[i].getName().contains(WHOLESALE_CONV)) {
				dirSize += files[i].length();
			}
		}
		// 如果总文件长度大于CACHE_SIZE * MB�?10mb）|| 当前 freeSpaceOnSd() < 10mb

		if (dirSize > CACHE_SIZE * MB
				|| FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			// 要移除的文件个数
			int removeFactor = (int) ((0.4 * files.length) + 1);
			// 根据文件修改时间排序
			Arrays.sort(files, new FileLastModifSort());
			for (int i = 0; i < removeFactor; i++) {
				if (files[i].getName().contains(WHOLESALE_CONV)) {
					files[i].delete();
				}
			}
		}
		// 如果sd卡剩余空�? <= 10mb
		if (freeSpaceOnSd() <= CACHE_SIZE) {
			return false;
		}

		return true;
	}
	/** 修改文件的最后修改时�? **/
	public void updateFileTime(String path) {
		File file = new File(path);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}
	/** 计算sdcard上的剩余空间 **/
	private int freeSpaceOnSd() {
		// StatFs查询文件系统相关的信�?
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}
	/** 将url转成文件�? **/
	private String convertUrlToFileName(String url) {
		String[] strs = url.split("/");
		String filename = strs[strs.length - 2] + "_" + strs[strs.length - 1]
				+ WHOLESALE_CONV;
		return filename;
	}
	/** 获得缓存目录 **/
	private String getDirectory() {
		String dir = getSDPath() + "/" + CACHDIR;
		return dir;
	}
	/** 取SD卡路�? **/
	private String getSDPath() {
		File sdDir = null;
		// getExternalStorageState() 获得当前外部储存媒体的状态�??
		// MEDIA_MOUNTED 存储媒体已经挂载，并且挂载点可读/写�??
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存�?
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory(); // 获取根目�?
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return "";
		}
	}
	/**
	 * 根据文件的最后修改时间进行排�?
	 */
	private class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}
