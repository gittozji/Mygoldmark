package com.phlofy.mygoldmark;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.util.Scanner;

import com.phlofy.mygoldmark.Painter.GetPasswordListener;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends Activity{
	Myapp app = null;
	boolean isFirst = true;
	String pass = "null";
	boolean one=false,two=false;//one表示加载，two表示密码
	Painter painter = null;
	ImageView iv = null;
	TextView tv = null;
	TextView tv_a = null;
	Mygolddatabase mygolddatabase = null;
	AssetManager assets = null;
	String[] string = null;
	int status = 0;
	String status_m = null;
	public Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0x111){
				tv_a.setText("已完成"+status_m);
			}
			if(msg.what == 0x112){
				tv_a.setText("历时"+status+"m");
			}
			if(msg.what == 0x113){
				Toast.makeText(WelcomeActivity.this, "手势正确，请等待数据初始化！",Toast.LENGTH_SHORT ).show();
			}
			if(msg.what == 0x114){
				byte[] be = (byte[]) app.getIcon();
				iv.setImageBitmap(BitmapFactory.decodeByteArray(be, 0, be.length));
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_welcome);
		mygolddatabase = new Mygolddatabase(WelcomeActivity.this, "mygolddatabase", 1);
		assets = getAssets();
		iv = (ImageView)findViewById(R.id.layout_welcome_imageview);
		tv = (TextView)findViewById(R.id.layout_welcome_text);
		tv_a = (TextView)findViewById(R.id.layout_welcome_text_a);
		painter = (Painter) findViewById(R.id.layout_welcome_painter);
		final SharedPreferences preferences = getSharedPreferences("phlofy",MODE_WORLD_READABLE);
		int count = preferences.getInt("count", 0);
		pass = preferences.getString("Gesture", "00");
		//count == 0的情况
		if(count != 12){
			tv.setText("第一次运行程序，正在初始化数据库...\n(请不要操作)");
			
			new Thread(){
				public void run(){
					try {
						mygolddatabase.getReadableDatabase().execSQL("DELETE FROM list_b");
						string = assets.list("Icon_a");
					}catch (IOException e) {}
					status=0;
					for(int i=0;i<string.length;i++){
						InputStream assetFile = null;
						try {
							assetFile = assets.open("Icon_a/"+string[i]);
							Scanner scanner_a = new Scanner(string[i]);
							scanner_a.useDelimiter("[^0123456789]+"); 
							int scaInt_a = scanner_a.nextInt();
							Bitmap b = BitmapFactory.decodeStream(assetFile);
							//转化为byte[]
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
						    b.compress(Bitmap.CompressFormat.PNG, 100, baos);
						    byte[] ba = baos.toByteArray();
						    //存入数据库
						    try{	
						    	insert_b(mygolddatabase.getReadableDatabase(),scaInt_a,ba);	
						    	status++;
						    	status_m = status+"/"+string.length;
						    	mHandler.sendEmptyMessage(0x111);
						    }catch(Exception e){}
						}catch(Exception e){}
					}
					try {
						string = assets.list("Icon_b");
					}catch (IOException e) {}
					status=0;
					for(int i=0;i<string.length;i++){
						InputStream assetFile = null;
						try {
							assetFile = assets.open("Icon_b/"+string[i]);
							Scanner scanner = new Scanner(string[i]);
							scanner.useDelimiter("[^0123456789]+"); 
							int scaInt = scanner.nextInt();

							Bitmap b = BitmapFactory.decodeStream(assetFile);
							//转化为byte[]
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
						    b.compress(Bitmap.CompressFormat.PNG, 100, baos);
						    byte[] ba = baos.toByteArray();
						    //存入数据库
						    try{
						    	insert_c(mygolddatabase.getReadableDatabase(),scaInt,ba);
						    	status++;
						    	status_m = status+"/"+string.length;
						    	mHandler.sendEmptyMessage(0x111);
						    }catch(Exception e){}
						}catch(Exception e){}
					}
					//tv.setText("数据库初始化成功");
					ComponentName comp = new ComponentName(WelcomeActivity.this,SignActivity.class);
					Intent intent = new Intent();
					intent.setComponent(comp);
					startActivity(intent);
					finish();
				}
			}.start();
		}
		//count != 0的情况
		else{
			final String p_account = preferences.getString("account", null);
			final String p_password = preferences.getString("password", null);
			if(p_account == null||p_password == null){
				//检测到SharedPreferences里没账号，转到账号登陆界面
				ComponentName comp = new ComponentName(WelcomeActivity.this,LandingActivity.class);
				Intent intent = new Intent();
				intent.setComponent(comp);
				startActivity(intent);
				finish();		
			}
			else{//检测到SharedPreferences里有账号，转到主界面
				tv.setText("欢迎回来，正在登陆...");
				
				new Thread(){
					public void run(){
						if(afferent(p_account, p_password)){
							one=true;
							mHandler.sendEmptyMessage(0x114);
							if(pass.equals("00")){
								Intent intent = new Intent(WelcomeActivity.this,InletActivity.class);		
								startActivity(intent);
								finish();
							}
						}
					}
				}.start();
			}
		}
		Editor editor = preferences.edit();
		editor.putInt("count", 12);
		editor.commit();
		//设置painter监听器完成接收密码并开启InlectActivity
		painter.setGetPasswordListener(new GetPasswordListener() {
			
			@Override
			public void getPass(String password) {
					if((password.equals(pass)||pass.equals("00"))&&one){
						Intent intent = new Intent(WelcomeActivity.this,InletActivity.class);		
						startActivity(intent);
						finish();
					}
					else{
						if((password.equals(pass)||pass.equals("00"))&&!one){
							mHandler.sendEmptyMessage(0x113);
							while(true){
								if(one){
									Intent intent = new Intent(WelcomeActivity.this,InletActivity.class);		
									startActivity(intent);
									finish();
									break;
								}
							}
						}
					}
			}
		});
	}
	
	private void insert_c(SQLiteDatabase readableDatabase,int i, byte[] ba) {
		// TODO Auto-generated method stub
		readableDatabase.execSQL("insert into list_c values(?,?)",new Object[]{i,ba});
	}
	private void insert_b(SQLiteDatabase readableDatabase,int i,byte[] ba) {
		// TODO Auto-generated method stub
		readableDatabase.execSQL("insert into list_b values(?,?)",new Object[]{i,ba});
	}
	
	public boolean afferent(String acc,String pas){
		mygolddatabase=new Mygolddatabase(WelcomeActivity.this, "mygolddatabase", 1);
		Cursor cursor = mygolddatabase.getReadableDatabase().rawQuery("select account,password from list_a where account=?",new String[]{acc});
		try{
			cursor.moveToFirst();
	    	String r_account = cursor.getString(cursor.getColumnIndex("account"));
	    	String r_password = cursor.getString(cursor.getColumnIndex("password"));
	    	if(acc.equals(r_account)&&pas.equals(r_password)){
	    		app = (Myapp) getApplication();
	    		app.afferent(acc, pas);
	    		app.wayA(WelcomeActivity.this);
	    		return true;	
	    	}
	    	else{
				
			}
	    }
		catch(Exception e){
			Intent intent = new Intent(WelcomeActivity.this,LandingActivity.class);		
			startActivity(intent);
			finish();		
		}
		return false;
	}
	
	public void onDestroy(){
		super.onDestroy();
		if(mygolddatabase!=null){
			mygolddatabase.close();
		}
	}
	public Handler getHandler(){
		return mHandler;
	}
}
