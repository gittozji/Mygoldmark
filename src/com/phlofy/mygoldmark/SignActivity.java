package com.phlofy.mygoldmark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignActivity extends Activity{
	ImageButton ig;
	RelativeLayout lay=null;
	EditText e_name;
	EditText e_account;
	EditText e_password_a;
	EditText e_password_b;
	Mygolddatabase mdb;
	Mydatabase db;
	Cursor cursor=null;
	List<Map<String,Object>> listItems2;//listItems放头像，listItems2放图标
	ChooseIcon ci = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sign);
		ig=(ImageButton)findViewById(R.id.sign_imagebutton);
		mdb = new Mygolddatabase(SignActivity.this, "mygolddatabase", 1);
		ci = new ChooseIcon(SignActivity.this, mdb);
		
		//====================创建图像listItems2集合===============================
		Cursor c = mdb.getReadableDatabase().rawQuery("select * from list_c", null);
		listItems2 = new ArrayList<Map<String,Object>>();
		while(c.moveToNext()){
			Map<String,Object>listItem = new HashMap<String,Object>();
			listItem.put("id", c.getInt(c.getColumnIndex("id")));
			listItem.put("icon", c.getBlob(c.getColumnIndex("icon")));
	    	listItems2.add(listItem);
	    }
		
		//====================选择头像事件========================
		
		
		ig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ci.ininitialize(ig);
			}
		});
	}
	//取消按钮
	public void button_bOnClick(View source){
		finish();
	}
	//注册按钮
	public void button_aOnClick(View Source){
		e_name = (EditText)findViewById(R.id.sign_edit_a);
		e_account = (EditText)findViewById(R.id.sign_edit_b);
		e_password_a = (EditText)findViewById(R.id.sign_edit_c);
		e_password_b = (EditText)findViewById(R.id.sign_edit_d);
		if((e_password_a.getText().toString().equals(e_password_b.getText().toString()))){
			//======================注册账号========================
			String exceptionMassage = "该用户已存在！";
			try{
				//准备头像byte[]对象
				if(ci.getIndex()==-1){
					exceptionMassage = "不能使用 ‘My金迹’ 作为头像！";
				}
				byte[] b = (byte[]) ci.getListItems().get(ci.getIndex()).get("icon");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				mdb.getReadableDatabase().execSQL("insert into list_a values(?,?,?,?,?,?,?)",new Object[]{e_account.getText().toString(),e_password_a.getText().toString(),e_name.getText().toString(),b,0,sdf.format(new Date()),700});
				db = new Mydatabase(SignActivity.this,e_account.getText().toString(),1);

				db.getReadableDatabase().execSQL("insert into type values(?,?)",new Object[]{"早餐",getByteFromListItems2(1)});
				db.getReadableDatabase().execSQL("insert into type values(?,?)",new Object[]{"午餐",getByteFromListItems2(2)});
				db.getReadableDatabase().execSQL("insert into type values(?,?)",new Object[]{"晚餐",getByteFromListItems2(3)});
				db.getReadableDatabase().execSQL("insert into type values(?,?)",new Object[]{"宵夜",getByteFromListItems2(4)});
				db.getReadableDatabase().execSQL("insert into type values(?,?)",new Object[]{"其他",getByteFromListItems2(5)});
				Intent intent = new Intent(SignActivity.this,LandingActivity.class);		
				startActivity(intent);
				finish();
			}catch(Exception e){
				Toast.makeText(SignActivity.this, exceptionMassage, Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(SignActivity.this, "两次密码输入不匹配！", Toast.LENGTH_SHORT).show();
		}
	}
	//获取listItems2的指定“id”的byte[]
	public byte[] getByteFromListItems2(int i){
		for(int j=0;j<listItems2.size();j++){
			Map<String,Object>listItem = listItems2.get(j);
			int integer = (Integer) listItem.get("id");
			if(integer==i){
				return (byte[]) listItem.get("icon");
			}
		}
		return null;
		
	}
	public void onDestroy(){
		super.onDestroy();
		if(mdb!=null){
			mdb.close();
		}
		if(db!=null){
			db.close();
		}
	}
}
