package com.phlofy.mygoldmark;

import java.util.LinkedList;
import java.util.Scanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;

public class MyBaseExpandableListAdapter {
	private ExpandableListAdapter adapter = null;
	MyCreateList myCreateList = null;
	LinkedList<Type> mylink = null;
	Activity activity = null;
	float sum = 0;
	
	public MyBaseExpandableListAdapter(Activity activity,MyCreateList myCreateList,LinkedList<Type> mylink ,float sum) {
		this.activity = activity; 
		this.myCreateList = myCreateList;
		this.mylink = mylink;
		this.sum = sum;
	}
	public ExpandableListAdapter getAdapter(){
		adapter = new BaseExpandableListAdapter() {
			@Override
			public boolean isChildSelectable(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
				// TODO Auto-generated method stub
				if(arg0==0){
					LayoutInflater inflater = LayoutInflater.from(activity);
					View view = inflater.inflate(R.layout.expandable_top, null);
					TextView t1 = (TextView) view.findViewById(R.id.top_textView2);
					TextView t2 = (TextView) view.findViewById(R.id.top_textView7);
					TextView t3 = (TextView) view.findViewById(R.id.top_textView6);
					//金额、最大、最小
					t1.setText(sum+"");
					t3.setText(getMax());
					t2.setText(getMin());
					return view;
				}
				LayoutInflater inflater = LayoutInflater.from(activity);
				View view = inflater.inflate(R.layout.expandable_one, null);
				TextView t1 = (TextView) view.findViewById(R.id.expandable_text_a);
				TextView t2 = (TextView) view.findViewById(R.id.expandable_text_money);
				TextView t3 = (TextView) view.findViewById(R.id.expandable_text_c_a);
				t1.setText(((Result) getGroup(arg0)).getBuildtime());
				t2.setText(((Result) getGroup(arg0)).getSum()+"");
				week(t3,((Result) getGroup(arg0)).getBuildtime());
				return view;
			}
			
			@Override
			public long getGroupId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}
			
			@Override
			public int getGroupCount() {
				// TODO Auto-generated method stub
				return myCreateList.getMyList().size()+1;
			}
			
			@Override
			public Object getGroup(int arg0) {
				// TODO Auto-generated method stub
				return myCreateList.getMyList().get(arg0-1);
			}
			
			@Override
			public int getChildrenCount(int arg0) {
				// TODO Auto-generated method stub
				if(arg0==0)
					return 0;
				return ((Result) getGroup(arg0)).getbillList().size();
			}
			@Override
			public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
					ViewGroup arg4) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater.from(activity);
				View view = inflater.inflate(R.layout.expandable_two, null);
				ImageView iv = (ImageView) view.findViewById(R.id.expandable_tow_ima);
				String typeName = ((Bill) getChild(arg0, arg1)).getType();
				
				new AsyncIcon(iv,typeName,mylink).work();
				
				
				TextView t1 = (TextView) view.findViewById(R.id.expandable_two_text_typename);
				TextView t2 = (TextView) view.findViewById(R.id.expandable_two_text_money);
				t1.setText(typeName);
				t2.setText(((Bill) getChild(arg0, arg1)).getMoney()+"");
				return view;
			}
			
			@Override
			public long getChildId(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return (long) ((Result) getGroup(arg0)).getbillList().get(arg1).getId();
			}
			
			@Override
			public Object getChild(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return ((Result) getGroup(arg0)).getbillList().get(arg1);
			}
		};
		if(myCreateList.getMyList().size()==0)
			return null;
		return adapter;
	}
	private void week(TextView t3,String buildtime){
		Scanner scanner = new Scanner(buildtime);
		scanner.useDelimiter("[^0123456789]+"); 
		int year,month,day;
		year = scanner.nextInt();
		month = scanner.nextInt();
		day = scanner.nextInt();
		if(month==1||month==2){
			year-=1;
			month+=12;
		}
		String weekString = null;
		switch((day+1+2*month+3*(month+1)/5+year+(year/4)-year/100+year/400)%7){
		case 1:weekString = "一";
		break;
		case 2:weekString = "二";
		break;
		case 3:weekString = "三";
		break;
		case 4:weekString = "四";
		break;
		case 5:weekString = "五";
		break;
		case 6:weekString = "六";
		t3.setBackgroundResource(R.color.saturday);
		break;
		case 0:weekString = "日";
		t3.setBackgroundResource(R.color.sunday);
		}
		t3.setText(weekString);
	}
	public String getMax(){
		int index = 0;
		for(int i=0;i<myCreateList.getMyList().size();i++){
			Result r1 = myCreateList.getMyList().get(i);
			Result r2 = myCreateList.getMyList().get(index);
			if(r2.getSum()<r1.getSum()){
				index = i;
			}
		}
		return myCreateList.getMyList().get(index).getBuildtime();
		
	}
	public String getMin(){
		int index = 0;
		for(int i=0;i<myCreateList.getMyList().size();i++){
			Result r1 = myCreateList.getMyList().get(i);
			Result r2 = myCreateList.getMyList().get(index);
			if(r2.getSum()>r1.getSum()){
				index = i;
			}
		}
		return myCreateList.getMyList().get(index).getBuildtime();
		
	}
}
