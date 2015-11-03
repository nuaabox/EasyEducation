package com.easyeducation.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;

public class FileService {
	private Context context;

	public FileService(Context context) {
		this.context = context;
	}

//	/**
//	 * 本应用追加保存文�?
//	 * 
//	 * @param filename
//	 *            文件名称
//	 * @param content
//	 *            文件内容
//	 */
//	public void MODE_APPEND_Save(String filename, String alldata)
//			throws Exception {
//		// 第二次写入的内容会追加到第一次的后面
//		FileOutputStream outStream = context.openFileOutput(filename,
//				Context.MODE_APPEND);
//		outStream.write(alldata.getBytes());
//		outStream.close();
//	}

	/**
	 * 覆盖文件
	 * 
	 * @param filename
	 *            文件名称
	 * @param alldata
	 *            文件内容
	 */
	public void MODE_PRIVATE_Sava(String filename, String alldata)
			throws Exception {
		FileOutputStream outStream = context.openFileOutput(filename,
				Context.MODE_PRIVATE);
		outStream.write(alldata.getBytes());
		outStream.close();
	}
	
	/**
	 * 空字符串覆盖�?有文件信息达到删除效�?
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 */
	public void Clean_all_files() throws Exception {
		String clean_data = "";
		FileOutputStream outStream;
		// TODO 通过空字符串来覆盖以前存储的数据
		/*
		 * 文件�?
		 * browsePcInfo----信息列表json
		 * browseButton----按钮json数据
		 * getAppAllTop----顶部广告文本
		 * getAppAllTopI---顶部图片
		 * getAppAllDownI--底部图片
		 * getAppAllDownT--底部文本
		 */
		outStream = context.openFileOutput("", Context.MODE_PRIVATE);
		outStream = context.openFileOutput("", Context.MODE_PRIVATE);
		outStream = context.openFileOutput("", Context.MODE_PRIVATE);
		outStream = context.openFileOutput("", Context.MODE_PRIVATE);
		outStream = context.openFileOutput("", Context.MODE_PRIVATE);

		outStream.write(clean_data.getBytes());
		outStream.close();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filename
	 *            文件名称
	 * @return 文件内容
	 * @throws Exception
	 */
	public String read(String filename) throws Exception {
		FileInputStream inStream = context.openFileInput(filename);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		return new String(data);
	}

}
