package com.phlofy.mygoldmark;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("NewApi")
public class Fragment_three extends Fragment {
	Myapp app = null;
	String nullStr = null;
	Button timeStart;
	Button timeEnd;
	Button search = null;
	Mydatabase db;
	ToggleButton tb = null;
	EditText et = null;
	Button selectLei = null;
	Button model = null;
	SeleteType st = null;
	boolean isNotContain = false;
	MyCreateList myCreateList = null;
	float sum = 0;
	boolean deleteRight = false;
	ExpandableListAdapter adapter = null;
	ExpandableListView e = null;
	
	public Fragment_three(){}
	
	Handler handle = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0x111){
				try{
					changeViewByAdapter();
				}catch(Exception e){}	
			}
			
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (Myapp) getActivity().getApplication();
		db = new Mydatabase(getActivity(), app.getAccount(), 1);
		st = new SeleteType(getActivity(), app);
		st.initialize();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//view等组件获取id
		View view = inflater.inflate(R.layout.fragment_three, null);
		timeStart = (Button)view.findViewById(R.id.fragment_three_button_a);
		timeEnd = (Button)view.findViewById(R.id.fragment_three_button_b);
		search = (Button) view.findViewById(R.id.fragment_three_button_f);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		timeStart.setText(sdf.format(new Date()));
		timeEnd.setText(sdf.format(new Date()));
		tb = (ToggleButton) view.findViewById(R.id.fragment_three_d);
		et = (EditText) view.findViewById(R.id.editText1);
		nullStr = et.getText().toString();
		selectLei = (Button) view.findViewById(R.id.fragment_three_c);
		model = (Button) view.findViewById(R.id.fragment_three_button_e);
		//model清单按钮事件
		model.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "该功还在开发中...", Toast.LENGTH_SHORT).show();
			}
		});
		
		//start时间按钮事件
		timeStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
						String arg2_s = null,arg3_s = null;
						if(arg2+1<10){
							arg2_s="0"+(arg2+1);
						}else{
							arg2_s=(arg2+1)+"";
						}
						if(arg3<10){
							arg3_s="0"+arg3;
						}else{
							arg3_s=arg3+"";
						}
						timeStart.setText(arg1+"-"+arg2_s+"-"+arg3_s);
					}
				}
				,c.get(Calendar.YEAR)
				,c.get(Calendar.MONTH)
				,c.get(Calendar.DAY_OF_MONTH)).show();
			}		
		});
		//end时间按钮事件
		timeEnd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
						String arg2_s = null,arg3_s = null;
						if(arg2+1<10){
							arg2_s="0"+(arg2+1);
						}else{
							arg2_s=(arg2+1)+"";
						}
						if(arg3<10){
							arg3_s="0"+arg3;
						}else{
							arg3_s=arg3+"";
						}
						timeEnd.setText(arg1+"-"+arg2_s+"-"+arg3_s);
					}
				}
				,c.get(Calendar.YEAR)
				,c.get(Calendar.MONTH)
				,c.get(Calendar.DAY_OF_MONTH)).show();
			}		
		});
		//搜索按钮事件
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) { 
				
				new Thread(){
					public void run(){
						try{
							codeTwoUse();
						}catch(Exception e){
							Toast.makeText(getActivity(), "未搜索到任何数据", Toast.LENGTH_SHORT).show();
						}
						
					}
				}.start();
			}
		});
		//选择类型集合按钮事件
		selectLei.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//st.initialize();
				st.run();
			}
		});
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				isNotContain = arg1;
			}
		});
		return view;
	}
	public String remarkStringYes(String s){
		if(s.equals(nullStr)){
			return "1";
		}
		Scanner scanner = new Scanner(s);
		String return_string = "";
		boolean booIsFirstTrue = true;//是否为第一个，是就不必加“or”
		while(scanner.hasNext()){
			if(booIsFirstTrue){
				return_string="remark like "+"'%"+scanner.next()+"%'";
				booIsFirstTrue=false;
			}else{
				return_string = return_string+" or remark like "+"'%"+scanner.next()+"%'";
			}
			if(booIsFirstTrue==true){
				return_string = "1";
			}
		}
		if(return_string.equals("")){
			return "1";
		}
		return return_string;
		
	}
	public String remarkStringNot(String s){
		if(s.equals(nullStr)){
			return "1";
		}
		String return_string = "";
		Scanner scanner = new Scanner(s);
		boolean booIsFirstTrue = true;//是否为第一个，是就不必加“and”
	    while(scanner.hasNext()){
	  		if(booIsFirstTrue){
	  			return_string="remark not like "+"'%"+scanner.next()+"%'";
	  			booIsFirstTrue=false;
	  		}else{
	  			return_string = return_string+" and remark not like "+"'%"+scanner.next()+"%'";
	  		}
		  	if(booIsFirstTrue==true){
		  		return_string = "1";
		  	}
	  	}
	    if(return_string.equals("")){
			return "1";
		}
		return return_string;
	}
	
	public String toString(){
		return "three";
	}
	private void codeTwoUse(){
		String stringType = st.getReturnType();
		if(st.getReturnType().equals("")){
			stringType = null;
		}
		String stringRemark = "";
		if(isNotContain){
			stringRemark = remarkStringNot(et.getText().toString());
		}else{
			stringRemark = remarkStringYes(et.getText().toString());
		}
		Cursor cursor1 = db.getReadableDatabase().rawQuery("select * from bill where (buildtime >= ? and buildtime <= ?) and("+stringType+") and("+stringRemark+") order by buildtime DESC,id ASC",new String[]{timeStart.getText().toString(),timeEnd.getText().toString()});
		
		//+++++++++++++++++++++++++至此++++++++++++++++++++++++++++++++++
		//______________________生成LinkedList<Result>___________________
		myCreateList = new MyCreateList();
		sum = 0;
		while(cursor1.moveToNext()){
			//生成总金额
			sum+=cursor1.getFloat(cursor1.getColumnIndex("money"));
			myCreateList.add(cursor1.getInt(cursor1.getColumnIndex("id")), cursor1.getString(cursor1.getColumnIndex("buildtime")), cursor1.getFloat(cursor1.getColumnIndex("money")), cursor1.getString(cursor1.getColumnIndex("type")), cursor1.getString(cursor1.getColumnIndex("remark")));
		}
		//发送消息改变View
		handle.sendEmptyMessage(0x111);
	}
	private void changeViewByAdapter(){
		//生成Adapter
		adapter = new MyBaseExpandableListAdapter(getActivity(), myCreateList, app.getMylink(),new Caculate().exchange(sum, 1)).getAdapter();
		e = (ExpandableListView) getActivity().findViewById(R.id.expand);
		e.setAdapter(adapter);
		e.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
					int arg3, final long arg4) {
				Bundle bundle = new Bundle();
				bundle.putFloat("money", myCreateList.getMyList().get(arg2-1).getbillList().get(arg3).getMoney());
				bundle.putString("type", myCreateList.getMyList().get(arg2-1).getbillList().get(arg3).getType());
				bundle.putString("remark", myCreateList.getMyList().get(arg2-1).getbillList().get(arg3).getRemark());
				bundle.putString("time", myCreateList.getMyList().get(arg2-1).getBuildtime());
				bundle.putLong("id", myCreateList.getMyList().get(arg2-1).getbillList().get(arg3).getId());
				Intent intent = new Intent(getActivity(),SaveOrDelete.class);		
				intent.putExtra("extra", bundle);
				startActivity(intent);
				return false;
			}
		});
	}
}
