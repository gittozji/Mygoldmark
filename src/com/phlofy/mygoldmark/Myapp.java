package com.phlofy.mygoldmark;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Myapp extends Application{
	private Mygolddatabase mygolddatabase = null;
	private Mydatabase mydatabase = null;
	
	//反常规代码，用于特殊销毁InletActivity
	public InletActivity inlet = null;
	//wayA
	private String account = null;//i=1              Y   list_a,SharedPreferences,user
	private String password = null;//i=1             Y   list_a,SharedPreferences,user
	private String name = null;//i=1				 Y	 list_a
	private byte[] icon = null;//i=1				 Y   list_a
	private String buildtime = null;//i=1			 Y	 list_a
	private float income = 0;//i=1					 Y	 list_a
	private float cost = 0;//i=1					 Y	 bill
	private float surplus = 0;//i=1			calculateY
	private long vip1 = 0;//密钥vip值
	private long vip2 = 0;//记账vip值
	//wayB
	private String reputation = null;//i=3           N   list_a,bill
	private float history_cost = 0;//i=3			 N	 bill
	private float history_proportion = 0;//i=3		 N	 bill
	private LinkedList<Type> mylink;//i=1            Y type的数据保存，用于提供类型的数据源
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
	}
	//传入账号、密码
	public void afferent(String acc,String pas){
		account=acc;
		password=pas;
	}
	public boolean wayA(Context con){
		//初始化昵称、头像、vip、建号时间、每月消费预设
		try{
			mygolddatabase = new Mygolddatabase(con, "mygolddatabase", 1);
			Cursor cursor = mygolddatabase.getReadableDatabase().rawQuery("select * from list_a where account=?",new String[]{account});
			cursor.moveToFirst();
			name=cursor.getString(cursor.getColumnIndex("name"));
			icon=cursor.getBlob(cursor.getColumnIndex("icon"));
			//得到通过密钥提升的vip经验值
			vip1=cursor.getLong(cursor.getColumnIndex("vip"));
			buildtime=cursor.getString(cursor.getColumnIndex("buildtime"));
			income=cursor.getFloat(cursor.getColumnIndex("income"));
		}catch(Exception e){
			System.out.println("错误：初始化昵称、头像、vip、建号时间、每月消费预设");
			return false;
		}
		//初始化Type数据
		try{
			mylink = new LinkedList<Type>();
			mydatabase = new Mydatabase(con, account,1);
			
			
			Cursor cursor = mydatabase.getReadableDatabase().rawQuery("select * from type", null);
			while(cursor.moveToNext()){
				Type p = new Type(cursor.getString(cursor.getColumnIndex("name")),cursor.getBlob(cursor.getColumnIndex("icon")));
				mylink.add(p);
				Type q = mylink.getLast();
			}
		}catch(Exception e){
			System.out.println("错误：初始化Type数据");
			return false;
		}
		//初始化vip、月消费
		try{
			//vip，得到通过记账得到的vip经验值
			mydatabase = new Mydatabase(con, account,1);
			Cursor cursor = mydatabase.getReadableDatabase().rawQuery("select count(*) as num from bill",null);
			cursor.moveToFirst();
			vip2 = cursor.getLong(cursor.getColumnIndex("num"));
			//月消费
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String todayTime = sdf.format(new Date());
			Cursor cursor1 = mydatabase.getReadableDatabase().rawQuery("select sum(money) as num from bill where buildtime like ?",new String[]{todayTime+"%"});
			cursor1.moveToFirst();
			cost = cursor1.getFloat(cursor1.getColumnIndex("num"));
		}catch(Exception e){
			System.out.println("错误：初始化vip、月消费");
			return false;
		}
		return true;
	}
	public boolean wayB(Context con){
		//初始化history_cost
		try{
			mydatabase = new Mydatabase(con, account,1);
			Cursor cursor = mydatabase.getReadableDatabase().rawQuery("select sum(money) as num from bill",null);
			cursor.moveToFirst();
			history_cost = cursor.getFloat(cursor.getColumnIndex("num"));
			Cursor cursor1 = mydatabase.getReadableDatabase().rawQuery("select sum(money) as num from bill where type=? or type=? or type=?",new String[]{"早餐","午餐","晚餐"} );
			cursor1.moveToFirst();
			Caculate caculate = new Caculate();
			history_proportion = caculate.exchange(100*cursor1.getFloat(cursor.getColumnIndex("num"))/history_cost,2);
		}catch(Exception e){
			System.out.println("错误：初始化history_cost");
			return false;
		}
		
		return false;
		
	}
	public String getAccount(){
		return account;
	}
	public void setAccount(String s){
		account = s;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String s){
		password = s;
	}
	public String getName(){
		return name;
	}
	public void setName(String s){
		name = s;
	}
	public byte[] getIcon(){
		return icon;
	}
	public void setIcon(byte[] s){
		icon = s;
	}
	public String getBuildtime(){
		return buildtime;
	}
	public void setBuildtime(String s){
		buildtime = s;
	}
	public float getIncome(){
		return new Caculate().exchange(income,1);
	}
	public void setIncome(float s){
		income = s;
	}
	public float getCost(){
		return new Caculate().exchange(cost, 1);
	}
	public void setCost(float s){
		cost = s;
	}
	public float getSurplus(){
		return new Caculate().exchange(income-cost,1);
	}
	public String getReputasion(){
		//生成reputation的算法
		reputation = "败家子";
		return reputation;
	}
	public void setReputasion(String s){
		
		reputation = s;
	}
	public float getHistory_cost(){
		return new Caculate().exchange(history_cost, 1);
	}
	public void setHistory_cost(float s){
		history_cost = s;
	}
	public float getHistory_proportion(){
		return history_proportion;
	}
	public void setHistory_proportion(float s){
		history_proportion = s;
	}
	public long getVip(){
		long vip = vip1+vip2;
		if(vip>=168)
			return 5;
		if(vip>=105)
			return 4;
		if(vip>=63)
			return 3;
		if(vip>=42)
			return 2;
		if(vip>=21)
			return 1;
		return 0;
	}
	public void setVip1(long s){
		vip1 = s;
	}
	public void setVip2(long s){
		vip2 = s;
	}
	public long getVip1(){
		return vip1;
	}
	public long getVip2(){
		return vip2;
	}
	public LinkedList<Type> getMylink(){
		return mylink;
	}
	public void setMylink(LinkedList<Type> l){
		mylink = l;
	}
	public void setInlet(InletActivity i){
		inlet=i;
	}
	public InletActivity getInlet(){
		return inlet;
	}
}
