package com.phlofy.mygoldmark;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.*;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Fragment_five extends Fragment{
	Myapp app = null;
	TextView textD = null;
	TextView textE = null;
	TextView textF = null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (Myapp) getActivity().getApplication();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		View view = inflater.inflate(R.layout.fragment_five, null);
		textD = (TextView) view.findViewById(R.id.fragment_five_text_d);
		textE = (TextView) view.findViewById(R.id.fragment_five_text_e);
		textF = (TextView) view.findViewById(R.id.fragment_five_text_f);
		textD.setText("£¤"+new Caculate().exchange(app.getSurplus()/(returnDay()-Integer.parseInt(new SimpleDateFormat("dd").format(new Date()))), 2));
		try{
			textE.setText(new Caculate().exchange(getThisMonthCost(), 2)+"%");
		}catch(Exception e){
			textE.setText(0+"%");
		}
		
		textF.setText("£¤"+new Caculate().exchange(app.getIncome()-app.getCost()*returnDay()/Integer.parseInt(new SimpleDateFormat("dd").format(new Date())), 2));
		return view;
	}
	public String toString(){
		return "five";
	}
	private float getThisMonthCost() {
		Mydatabase mydatabase = new Mydatabase(getActivity(), app.getAccount(), 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String todayTime = sdf.format(new Date());
		Cursor cursor = mydatabase.getReadableDatabase().rawQuery("select sum(money) as num from bill where buildtime like ? and (type=? or type=? or type=?)",new String[]{todayTime+"%","Ôç²Í","Îç²Í","Íí²Í"});
		cursor.moveToFirst();
		float moneyNoSan = cursor.getFloat(cursor.getColumnIndex("num"));
		cursor = mydatabase.getReadableDatabase().rawQuery("select sum(money) as num from bill where buildtime like ?",new String[]{todayTime+"%"});
		cursor.moveToFirst();
		float moneyAll = cursor.getFloat(cursor.getColumnIndex("num"));
		return (100*moneyNoSan)/moneyAll;
		
	}
	private int returnDay(){
		int todayTimeM,todayTimeY;
		int[][] dayNumber={
			{31,28,31,30,31,30,31,31,30,31,30,31},
			{31,29,31,30,31,30,31,31,30,31,30,31}
			};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String todayTime = sdf.format(new Date());
		todayTimeY = Integer.parseInt(todayTime);
		sdf = new SimpleDateFormat("M");
		todayTime = sdf.format(new Date());
		todayTimeM = Integer.parseInt(todayTime);
		if((todayTimeY%4==0&&todayTimeY%100!=0)||todayTimeY%400==0){
			return dayNumber[1][todayTimeM-1];
		}
		return dayNumber[0][todayTimeM-1];
	}
	
}

