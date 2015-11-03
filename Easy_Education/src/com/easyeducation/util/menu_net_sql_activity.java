package com.easyeducation.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class menu_net_sql_activity {

	private DBOpenhelper dbopenhelper;

	public menu_net_sql_activity(Context context) {
		this.dbopenhelper = new DBOpenhelper(context);
	}

	public void save(String buttonNo,String buttonName) {
		// 取得数据库操作实�?
		SQLiteDatabase db = dbopenhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("buttonNo", buttonNo);
		values.put("buttonName", buttonName);
		db.insert("HNIA_menu", "id", values);
		db.close();
	}
	
	
	
	public void delete(String buttonNo) {
		SQLiteDatabase db = dbopenhelper.getReadableDatabase();
		SQLiteDatabase dbdelect = dbopenhelper.getWritableDatabase();
		if ((db.rawQuery("select * from books where buttonName=?",
				new String[] { buttonNo.toString() })) != null) {
			dbdelect.execSQL("delete from books where buttonName=?",
					new String[] { buttonNo.toString() });
		}
		dbdelect.close();
		db.close();
	}

}
