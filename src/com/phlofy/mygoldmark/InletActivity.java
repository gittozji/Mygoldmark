package com.phlofy.mygoldmark;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

import javax.crypto.spec.IvParameterSpec;

import android.R.integer;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class InletActivity extends Activity implements OnTouchListener, OnGestureListener{
	Myapp app = null;

	TextView inletName = null;
	Fragment fragment = null;
	Fragment_two f = null; 
	final int FLIP_DISTANCE = 50;
	FrameLayout fl;
	GestureDetector detector;
	FormatTools formattools = null;
	AssetManager assets = null;
	String[] images = null;
	ToggleButton togglebutton;
	ImageButton ib;
	Mygolddatabase mygold;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_inlet);
		//获取Myapp
		app = (Myapp)getApplication();
		app.setInlet(this);
		inletName = (TextView)findViewById(R.id.inlet_name);
		inletName.setText(app.getName());
		//初始化设置输入法为无
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		fragment = new Fragment_main();
		fl = (FrameLayout)findViewById(R.id.fragment_a);
		detector = new GestureDetector(this,this);
		fl.setOnTouchListener(this);
		getFragmentManager().beginTransaction().replace(R.id.fragment_a, fragment).commit();
		f = new Fragment_two();
		getFragmentManager().beginTransaction().replace(R.id.fragment_b, f).commit();
		togglebutton = (ToggleButton)findViewById(R.id.inlet_toggleButton);
		togglebutton.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					fragment = new Fragment_three();
					getFragmentManager().beginTransaction().replace(R.id.fragment_a, fragment).commit();
					Fragment_four f = new Fragment_four();
					getFragmentManager().beginTransaction().replace(R.id.fragment_b, f).commit();
				}
				else{
					fragment = new Fragment_main();
					getFragmentManager().beginTransaction().replace(R.id.fragment_a, fragment).commit();
					f = new Fragment_two();
					getFragmentManager().beginTransaction().replace(R.id.fragment_b, f).commit();
				}
				try{
					if(f.getPopupExist()){
						f.getPopup().dismiss();
						f.setPopupExist(false);
					}	
				}catch(Exception e){}
			}	
		});
		ib=(ImageButton)findViewById(R.id.inlet_imagebutton);
		byte[] b = (byte[]) app.getIcon();
		BitmapDrawable bd = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
		ib.setBackgroundDrawable(bd);
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try{
					if(f.getPopupExist()){
						f.getPopup().dismiss();
						f.setPopupExist(false);
					}	
				}catch(Exception e){}
				ComponentName comp = new ComponentName(InletActivity.this,UserManager.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(f.getPopupExist()){
			f.getPopup().dismiss();
			f.setPopupExist(false);
		}	
		else
			super.onKeyDown(keyCode, event);
		return true;
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		inletName.setText(app.getName());
		byte[] b = (byte[]) app.getIcon();
		BitmapDrawable bd = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
		ib.setBackgroundDrawable(bd);
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mygold!=null){
			mygold.close();
		}
	}
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return detector.onTouchEvent(arg1);
	}
	@SuppressLint("NewApi")
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		if(arg0.getX()-arg1.getX()>FLIP_DISTANCE){
			if(fragment.toString().equals("main")){
				fragment = new Fragment_five();
				getFragmentManager().beginTransaction().replace(R.id.fragment_a, fragment).commit();
			}
			return true;
		}
		else{
			if(fragment.toString().equals("five")){
				fragment = new Fragment_main();
				getFragmentManager().beginTransaction().replace(R.id.fragment_a, fragment).commit();
			}
			return true;
		}
	}
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	public String toString(){
		return "InletActivity";
	}
}
