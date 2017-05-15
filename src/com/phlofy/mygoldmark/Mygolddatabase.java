package com.phlofy.mygoldmark;

import junit.runner.Version;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Mygolddatabase extends SQLiteOpenHelper{
	public Mygolddatabase(Context context, String name,int v) {
		super(context, name, null, v);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("create table list_a(account varchar(50) primary key,password varchar(50),name nvarchar(20),icon BLOB,vip integer,buildtime varchar(20),income float)");
		arg0.execSQL("create table list_b(id integer primary key,icon BLOB)");
		arg0.execSQL("create table list_c(id integer primary key,icon BLOB)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
}
