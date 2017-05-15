package com.phlofy.mygoldmark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Mydatabase extends SQLiteOpenHelper{
	public Mydatabase(Context context, String name,int v) {
		super(context, name, null ,v);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("create table bill(id integer primary key autoincrement,buildtime varchar(10),money float,type nvarchar(20),remark nvarchar(100))");
		arg0.execSQL("create table type(name nvarchar(20) primary key,icon BLOB)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub		
	}

}
