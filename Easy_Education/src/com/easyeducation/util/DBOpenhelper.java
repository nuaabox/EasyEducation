package com.easyeducation.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenhelper extends SQLiteOpenHelper {

	public DBOpenhelper(Context context) {
		super(context, "books.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * 数据库第�?次被创建的时候调用的
	 * 功能：创建数据库�?
	 */
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL("CREATE TABLE HNIA_menu (id integer primary key autoincrement,buttonName  varchar(20)");
		// 创建按钮表，包含相关的josn数据
		db.execSQL("CREATE TABLE MENU_URL (Menu_id integer primary key autoincrement,appId integer,buttonName varchar(50),id integer,imageUrl varchar(1024),sort varchar(10),spiderId integer,url varchar(10),userId integer,vipuserId integer");
		//创建底部文本�?
		db.execSQL("CREATE TABLE bottomText (BottomText_id integer primary key autoincrement,appId integer,id integer,text varchar(256),userId integer");
		
		db.execSQL("CREATE TABLE bottomText (BottomText_id integer primary key autoincrement,appId integer,id integer,text varchar(256),userId integer");
	}

	/**
	 * 数据库版本号发生变化是调�?
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
