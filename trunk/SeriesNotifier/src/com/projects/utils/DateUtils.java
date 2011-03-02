package com.projects.utils;

import android.content.Context;
import android.database.Cursor;

import com.projects.database.DBAdapter;

public class DateUtils {
	
	public static long getEpochDate(Context context){
		DBAdapter db = new DBAdapter(context);
		db.open();
		long epochDate = -1, cols;
		Cursor cursor = db.getEpochDate();
		cursor.moveToFirst();
		cols = cursor.getCount();
		if(cols > 0){
			epochDate = cursor.getLong(0);
		}
		db.close();
		return epochDate;
	}
	
	public static void insertEpochDate(Context context, long epoch){
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.insertEpochDate(epoch);
		db.close();
	}
	
	
}
