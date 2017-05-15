package com.phlofy.mygoldmark;

import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;

public class SeleteType {
	Context context = null;
	Myapp app = null;
	String name[] = null;
	boolean boo[] = null;
	Mydatabase db = null;
	String return_string = "";
	LinkedList<Type> mylink = null;
	//构造方法
	SeleteType(Context context,Myapp app){
		this.context=context;
		this.app=app;
	}
	//初始化
	public void initialize(){
		mylink = app.getMylink();
		name = new String[mylink.size()];
		boo = new boolean[mylink.size()];
		for(int i=0;i<mylink.size();i++){
			name[i] = mylink.get(i).name;
			boo[i] =true;
		}
		setReturn_string();
	}
	//运行窗口
	public void run(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("选择类型")
		.setIcon(R.drawable.ic_launcher)
		.setMultiChoiceItems(name, boo, new OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				return_string="";
				setReturn_string();
			}
		})
		.create()
		.show();
	}
	public String getReturnType(){
		return return_string;
	}
	private void setReturn_string(){
		boolean booIsFirstTrue = true;//是否为第一个，是就不必加“or”
		for(int i=0;i<boo.length;i++){
			if(boo[i]==true){
				if(booIsFirstTrue){
					return_string="type="+"'"+name[i]+"'";
					booIsFirstTrue=false;
				}
				else{
					return_string = return_string+" or type="+"'"+name[i]+"'";
				}
			}
			if(booIsFirstTrue==true){
				return_string = "1";
			}
		}
	}
}
