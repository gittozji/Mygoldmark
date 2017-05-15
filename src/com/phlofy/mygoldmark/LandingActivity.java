package com.phlofy.mygoldmark;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LandingActivity extends Activity{
	Myapp app = null;
	EditText e_a;
	EditText e_p;
	Mygolddatabase mygolddatabase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_landing);
		app = (Myapp) getApplication();
		e_a = (EditText)findViewById(R.id.landing_edit_b);
		e_p = (EditText)findViewById(R.id.landing_edit_c);
		mygolddatabase = new Mygolddatabase(LandingActivity.this, "mygolddatabase", 1);
	}
	public void button_returnOnClick(View source){
		finish();
		
	}
	public void button_aOnClick(View source){
		String a = e_a.getText().toString();
		String p = e_p.getText().toString();
		if(afferent(a,p)){
			Intent intent = new Intent(LandingActivity.this,WelcomeActivity.class);		
			startActivity(intent);
			finish();
		}
	}
	public void button_bOnClick(View source){
		Intent intent = new Intent(LandingActivity.this,SignActivity.class);		
		startActivity(intent);
		finish();
	}
	
	public boolean afferent(String acc,String pas){
		mygolddatabase=new Mygolddatabase(LandingActivity.this, "mygolddatabase", 1);
		Cursor cursor = mygolddatabase.getReadableDatabase().rawQuery("select account,password from list_a where account=?",new String[]{acc});
		try{
			cursor.moveToFirst();
	    	String r_account = cursor.getString(cursor.getColumnIndex("account"));
	    	String r_password = cursor.getString(cursor.getColumnIndex("password"));
	    	if(acc.equals(r_account)&&pas.equals(r_password)){
	    		try{
	    			Myapp app = (Myapp) getApplication();
	    			app.getInlet().finish();
	    		}catch(Exception e){}
	    		
	    		final SharedPreferences preferences = getSharedPreferences("phlofy",MODE_WORLD_READABLE);
	    		Editor editor = preferences.edit();
	    		editor.putString("account", r_account);
	    		editor.putString("password", r_password);
	    		editor.commit();
	    		return true;	
	    	}
	    	else{
	    		Toast.makeText(LandingActivity.this,"密码错误", Toast.LENGTH_SHORT).show();
	    	}
	    }
		catch(Exception e){
			Toast.makeText(LandingActivity.this,"该账户不存在", Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	public void onDestroy(){
		super.onDestroy();
		if(mygolddatabase!=null){
			mygolddatabase.close();
		}
	}
}
