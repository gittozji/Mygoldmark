package com.phlofy.mygoldmark;

import java.util.ArrayList;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Fragment_main extends Fragment{
	Myapp app = null;
	TextView textD = null;
	TextView textE = null;
	TextView textF = null;
	MyContainer contain =null;
	float proportion;
	public Fragment_main(){}
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==17){
				contain.setProportion(proportion);
			}
		}
	};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (Myapp)getActivity().getApplication();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		View view = inflater.inflate(R.layout.fragment_main, null);
		MyContainer t =(MyContainer) view.findViewById(R.id.fragment_main_mycontainer);
		t.setProportion((float)0.2);
		textD = (TextView) view.findViewById(R.id.fragment_main_text_d);
		textE = (TextView) view.findViewById(R.id.fragment_main_text_e);
		textF = (TextView) view.findViewById(R.id.fragment_main_text_f);
		contain = (MyContainer) view.findViewById(R.id.fragment_main_mycontainer);
		setContent();
		return view;
	}
	public String toString(){
		return "main";
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setContent();
	}
	public void setContent(){
		textD.setText("гд"+app.getIncome());
		textE.setText("гд"+app.getCost());
		textF.setText("гд"+app.getSurplus());
		//contain.setProportion(app.getSurplus()/app.getIncome());
		new Thread(){
			public void run() {
				proportion=0;
				while(proportion<app.getSurplus()/app.getIncome()){
					mHandler.sendEmptyMessage(17);
					proportion+=0.003;
					try {
						if(proportion>(0.7*(app.getSurplus()/app.getIncome())))
							Thread.sleep(10);
						else {
							if(proportion>(0.3*(app.getSurplus()/app.getIncome())))
								Thread.sleep(5);
							else {
								Thread.sleep(2);
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
