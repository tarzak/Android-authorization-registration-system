package com.example.android_39_sqliteregistrationsys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	final String LOG_TAG = "myLogs";
	
	public DBHelper(Context context) {
		// конструктор суперкласса
		super(context, "myDB", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LOG_TAG, "--- onCreate database ---");
		// создаем таблицу с полями
		db.execSQL("create table sqliteregistrationsys (" + "id integer primary key autoincrement," 
										+ "login text," + "password text," + "email text," 
										+ "personname text," + "age integer," + "skype text" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public void close() {
		
		Log.d(LOG_TAG, "--- onClose database ---");
		
	}
}